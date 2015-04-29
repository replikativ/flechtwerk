(ns geschichte-gorilla.graph
  (:require [clojure.set :refer [difference]]))


(defn branches->nodes [c causal-order c-nodes]
  (loop [parents (get causal-order c)
           node c
           order (list c)]
      (let [next-node (first (filter c-nodes parents))]
        (if next-node
          (recur (get causal-order next-node) next-node (conj order next-node))
          order))))


(defn repo->node-order [{:keys [branches causal-order nodes] :as repo}]
  (assoc repo :nodes
         (apply merge (map (fn [[b h]] {b (branches->nodes h causal-order (get nodes b))}) branches))))

(defn repo->branch-links [{:keys [causal-order nodes] :as repo}]
  (assoc repo :branch-links
           (apply merge
                  (map (fn [[b ns]] (let [root (first ns)]
                                     {b [(first (get causal-order root)) root]})) nodes))))

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
  (let [nodes->branch (apply merge (map (fn [[b ns]] (zipmap ns (repeat (count ns) b))) nodes))
        merge-links (->> (select-keys causal-order (for [[k v] causal-order :when (> (count v) 1)] k))
                         (map (fn [[k v]]
                                (remove #(= (ffirst %) (nodes->branch k))
                                        (map (fn [n] {(nodes->branch n) [[n k (nodes->branch n)]]}) v))))
                         (reverse)
                         (apply concat)
                         (apply merge-with (comp vec concat)))]
    (assoc cg :merge-links (dissoc merge-links nil))))


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


(defn unify-branch-heads [cg]
  (update-in cg [:branches] (fn [b] (into {} (map (fn [[k v]] [k (first v)]) b)))))


(defn commits->nodes
  "Extract nodes of each branch"
  [repo]
  (assoc repo :nodes
         (apply merge-with (comp set concat)
                (map (fn [[n b]] {b [n]}) (:commits repo)))))


(defn explore-commit-graph
  "Run the pipeline"
  [repo]
  (->> (select-keys repo [:branches :causal-order])
       unify-branch-heads
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
        eos (- w cs 50)]
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
     :commits
     {10 "master"
      20 "master"
      30 "master"
      40 "fix"
      50 "fix"
      60 "master"
      70 "master"
      80 "dev"
      90 "dev"
      100 "master"
      110 "master"
      120 "master"
      130 "fix-2"
      140 "fix-2"
      150 "fix"
      160 "fix"
      170 "master"}
     :branches {"master" #{170}
                "fix" #{160}
                "dev" #{120}
                "fix-2" #{140}}})


  (let [{:keys [causal-order nodes branches commits] :as repo}
        (->> test-repo
             unify-branch-heads
             commits->nodes
             repo->node-order
             repo->branch-links)]
    (map
     (fn [[b link]]
       {(get commits b) link})
     (filter #(> (count (val %)) 1) causal-order)))


  )
