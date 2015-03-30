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
