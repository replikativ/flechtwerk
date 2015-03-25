(ns geschichte-gorilla.core
  (:require [clojure.set :refer [difference]]
            [strokes :refer [d3]]
            [cljs.reader :refer [read-string] :as read]))

(.log js/console "All hail to Kordano!")

(strokes/bootstrap)

(def test-cg
  {:causal-order {10 [] 20 [10] 30 [20] 40 [20] 50 [40] 60 [30 50] 70 [60] 80 [30] 90 [80] 100 [70 140] 110 [100] 120 [90] 130 [30] 140 [130]}
   :branches {"master" 110 "fix" 50 "dev" 120 "fix-2" 140}})

(defn branches->nodes [c causal-order heads]
  (loop [parents (get causal-order c)
         node c
         order (list c)]
    (if (nil? parents)
      order
      (if (< 2 (count parents))
        (recur (get causal-order  (first parents))
               (first parents)
               (conj order node))
        (let [next-node (first (remove heads parents))]
          (recur (get causal-order next-node)
                 next-node
                 (conj order next-node)))))))


(defn distinct-nodes [{:keys [nodes] :as cg}]
  (assoc cg
    :nodes
    (loop [[b l-order] (first nodes)
           b-list (rest nodes)
           links []]
      (if (empty? b-list)
        (into {} (conj links [b l-order]))
        (let [branch-diffs (map (fn [[k v]] [k (difference (set v) (set l-order))]) b-list)]
          (recur
           (first branch-diffs)
           (rest branch-diffs)
           (conj links [b (set l-order)])))))))


(defn commit-graph->nodes [cg]
  (let [{:keys [branches causal-order]} cg
        heads (into #{} (vals branches))]
    (assoc cg :nodes
           (loop [[b c] (first branches)
                  b-list (rest branches)
                  result []]
             (if-not b
               (sort-by second #(> (count %1) (count %2)) result)
               (recur (first b-list)
                      (rest b-list)
                      (conj result [b (branches->nodes c causal-order heads)])))))))

(defn nodes->order
  "Calculate commit order in time"
  [{:keys [nodes causal-order branches] :as cg}]
  (let [new-nodes (map
                   (fn [[b b-nodes]]
                     [b (branches->nodes
                         (get branches b)
                         (select-keys causal-order b-nodes)
                         (into #{} (vals branches)))])
                   nodes)]
    (assoc cg
      :nodes
      (->> new-nodes
           (map (fn [[k v]] [k (vec (rest v))]))
           (into {}))
      :branch-links
      (->> new-nodes
           (map (fn [[k v]] [k (if (nil? (first v))
                                nil
                                (vec (take 2 v)))]))
           (into {})))))


(defn find-merge-links [{:keys [causal-order branches] :as cg}]
  (let [branch-heads (into {} (map (fn [[k v]] [v k]) branches))]
    (assoc cg :merge-links
      (->> (select-keys causal-order
                (for [[k v] causal-order
                      :when (> (count v) 1)]
                  k))
           (into {})
           (map (fn [[k v]] (map (fn [b] [b [(branches b) k]]) (remove nil? (map branch-heads v)))))
           (apply concat)
           (into {})))))


(defn nodes->links [{:keys [nodes] :as cg}]
  (assoc cg
    :links
    (->> nodes
         (map
          (fn [[k v]]
            [k
             (mapv
              (fn [i]
                [(get v i) (get v (inc i))])
              (range (count v)))]))
         (into {}))))

(defn nodes->x-y-order [{:keys [nodes] :as cg}]
  (let [x-order (mapv first (sort-by val #(> (count %1) (count %2)) nodes))]
    (assoc cg
      :x-order x-order
      :y-order (vec (loop [order x-order
                           y-order []
                           i true]
                      (if (empty? order)
                        y-order
                        (recur (rest order) (if i
                                              (concat y-order [(first order)])
                                              (concat [(first order)] y-order))
                               (not i))))))))

(defn explore-commit-graph
  "Run the pipeline"
  [cg]
  (->> cg
       commit-graph->nodes
       distinct-nodes
       nodes->order
       find-merge-links
       nodes->links
       nodes->x-y-order))


(defn compute-positions
  "Compute positions using widht, height, circle size and improved commit graph"
  [w h cs cg]
  (let [icg (explore-commit-graph cg)
        eos (- w cs)]
    (loop [x-order (:x-order icg)
           x-positions {}]
      (if (empty? x-order)
        (assoc icg
          :nodes (apply concat (vals (:nodes icg)))
          :links (remove
                  #(or (nil? %) (-> % second nil?))
                  (concat (apply concat (vals (:links icg)))
                          (vals (:branch-links icg))
                          (vals (:merge-links icg))))
          :x-positions x-positions
          :y-positions (let [m (count (:y-order icg))
                             dy (/ (- h (* 2 cs)) m )]
                         (->> (range m)
                              (map
                               (fn [i]
                                 (->> (get-in icg [:nodes (get (:y-order icg) i)])
                                      (map (fn [id] [id (+ cs (/ dy 2) (* i dy))]))
                                      (into {}))))
                              (apply merge))))
        (let [branch (first x-order)
              nodes (get-in icg [:nodes branch])
              n (count nodes)
              start (or (get x-positions (first (get-in icg [:branch-links branch]))) cs)
              end (or (get x-positions (last (get-in icg [:merge-links branch]))) eos)
              dx (/ (- end start) (if (= start 0)
                                    (if (= end eos) (dec n) n)
                                    (if (= end eos) n (inc n))))
              offset (if (= start 0) 0 dx)
              branch-positions (->> (range n)
                                    (map (fn [i] [(get nodes i) (+ start offset (* i dx))]))
                                    (into {}))]
          (recur (rest x-order) (merge x-positions branch-positions)))))))



(defn clear-canvas [container]
  (.. d3
      (select container)
      (select "svg")
      remove))


(defn render-graph
  "Render the commit graph using d3"
  [graph-data container]
  (let [width (* 0.4 (.-width js/screen))
        height (* 0.5 (.-height js/screen))
        circle-size 10
        {:keys [nodes x-positions y-positions links branches]}
        (compute-positions width height circle-size graph-data)
        svg (.. d3
                (select container)
                (append "svg")
                (attr {:width width
                       :height height}))
        tooltip (.. svg
                    (append "text")
                    (style {:visibility "hidden"
                            :position "absolute"
                            :text-anchor "middle"
                            :color "black"}))]
    (do
      (.. svg
          (selectAll "link")
          (data links)
          enter
          (append "line")
          (attr {:x1 (fn [[v1 _]] (x-positions v1))
                 :y1 (fn [[v1 _]] (y-positions v1))
                 :x2 (fn [[_ v2]] (x-positions v2))
                 :y2 (fn [[_ v2]] (y-positions v2))})
          (style {:stroke-with 2
                  :stroke "black"}))
      (.. svg
          (selectAll "circle")
          (data nodes)
          enter
          (append "circle")
          (attr {:cx (fn [d] (get x-positions d))
                 :cy (fn [d] (get y-positions d))
                 :fill (fn [d] (if (contains? (into #{} (vals branches)) d)
                                "red"
                                "steelblue"))
                 :r circle-size})
          (on "mouseover" (fn [d] (do (.. tooltip
                                     (style {:visibility "visible"})
                                     (attr {:y (- (get y-positions d) 15)
                                            :x (get x-positions d)})
                                     (text d)))))
          (on "mouseout" (fn [d] (do (.. tooltip
                                     (style {:visibility "hidden"})
                                     (attr {:y (- (get y-positions d) 15)
                                            :x (get x-positions d)})
                                     (text d)))))))))




(defn ^:export renderGraph
  [data container]
  (let [cg (read-string data)]
    (render-graph cg container)))
