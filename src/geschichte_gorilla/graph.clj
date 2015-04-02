(ns geschichte-gorilla.graph
  (:require [clojure.set :refer [difference]]))


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


(defn commit-graph->nodes [{:keys [branches causal-order] :as cg}]
  (let [heads (into #{} (vals branches))]
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
           (map (fn [[k v]] (if (nil? (first v))
                             nil
                             {k (conj (vec (take 2 v)) k)})))
           concat
           (remove nil?)
           (apply merge-with conj)))))


(defn find-merge-links [{:keys [causal-order branches nodes] :as cg}]
  (let [nodes->branch (apply merge (map (fn [[b ns]] (zipmap ns (repeat (count ns) b))) nodes))]
    (assoc cg
      :merge-links
      (->> (select-keys causal-order (for [[k v] causal-order :when (> (count v) 1)] k))
           (map (fn [[k v]]
                  (remove #(= (ffirst %) (nodes->branch k))
                          (map (fn [n] {(nodes->branch n) [[n k (nodes->branch n)]]}) v))))
           (reverse)
           (apply concat)
           (apply merge-with (comp vec concat))))))


(defn nodes->links [{:keys [nodes] :as cg}]
  (assoc cg
    :links
    (->> nodes
         (map
          (fn [[k v]]
            (mapv
             (fn [i]
               [(get v i) (get v (inc i)) k])
             (range (count v)))))
         (apply concat))))


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
  [repo]
  (->> (select-keys repo [:branches :causal-order])
       commit-graph->nodes
       distinct-nodes
       nodes->order
       find-merge-links
       nodes->links
       nodes->x-y-order))


(defn compute-positions
  "Compute positions using width, height, circle size and repo data"
  [w h cs cg]
  (let [ecg (explore-commit-graph cg)
        eos (- w cs)]
    (loop [x-order (:x-order ecg)
           x-positions {}]
      (if (empty? x-order)
        (assoc ecg
          :nodes (apply concat (map (fn [[branch ns]] (map (fn [n] [n branch]) ns)) (:nodes ecg)))
          :links (remove
                  #(or (nil? %) (-> % second nil?))
                  (concat (:links ecg)
                          (vals (:branch-links ecg))
                          (apply concat (vals (:merge-links ecg)))))
          :x-positions x-positions
          :y-positions (let [m (count (:y-order ecg))
                             dy (/ (- h (* 2 cs)) m )]
                         (->> (range m)
                              (map
                               (fn [i]
                                 (->> (get-in ecg [:nodes (get (:y-order ecg) i)])
                                      (map (fn [id] [id (+ cs (/ dy 2) (* i dy))]))
                                      (into {}))))
                              (apply merge))))
        (let [branch (first x-order)
              nodes (get-in ecg [:nodes branch])
              n (count nodes)
              start (or (get x-positions (first (get-in ecg [:branch-links branch]))) cs)
              end (or (get x-positions (-> ecg (get-in [:merge-links branch]) first second) ) eos)
              dx (/ (- end start) (if (= start 0)
                                    (if (= end eos) (dec n) n)
                                    (if (= end eos) n (inc n))))
              offset (if (= start 0) 0 dx)
              branch-positions (->> (range n)
                                    (map (fn [i] [(get nodes i) (+ start offset (* i dx))]))
                                    (into {}))]
          (recur (rest x-order) (merge x-positions branch-positions)))))))


(comment

  (def test-repo
    {:causal-order {10 []
                    20 [10]
                    30 [20]
                    40 [20]
                    50 [40]
                    60 [30 50]
                    70 [60]
                    80 [30]
                    90 [80]
                    100 [70 140]
                    110 [100]
                    120 [90]
                    130 [30]
                    140 [130]
                    150 [50]
                    160 [150]
                    170 [160 110]}
     :branches {"master" 170
                "fix" 160
                "dev" 120
                "fix-2" 140}})


  (->> test-repo
       commit-graph->nodes
       distinct-nodes
       nodes->order
       find-merge-links)


  (compute-positions 500 400 20 test-repo)


  )
