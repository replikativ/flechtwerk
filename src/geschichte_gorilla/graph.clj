(ns geschichte-gorilla.graph
  (:require [clojure.set :refer [difference]]
            [aprint.core :refer [ap]]))
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
                    170 [160 110]
                    180 [120]
                    190 [180]
                    200 [190]
                    210 [200]
                    220 [210]
                    230 [140]
                    240 [120 220]
                    250 [240]}
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
      120 "dev"
      130 "fix-2"
      140 "fix-2"
      150 "fix"
      160 "fix"
      170 "master"
      180 "fix-dev"
      190 "fix-dev"
      200 "fix-dev"
      210 "fix-dev"
      220 "fix-dev"
      230 "fix-2"
      240 "dev"
      250 "dev"}
     :branches {"master" #{170}
                "fix" #{160}
                "dev" #{250}
                "fix-dev" #{220}
                "fix-2" #{230}}})

(defn positions
  "Find item index
   http://stackoverflow.com/questions/4830900/how-do-i-find-the-index-of-an-item-in-a-vector"
  [pred coll]
  (keep-indexed (fn [idx x]
                  (when (pred x)
                    idx))
                coll))


(defn unify-branch-heads [repo]
  (update-in repo [:branches] (fn [b] (into {} (map (fn [[k v]] [k (first v)]) b)))))


(defn commits->nodes
  "Extract nodes of each branch"
  [repo]
  (assoc repo :nodes
         (apply merge-with (comp set concat)
                (map (fn [[n b]] {b [n]}) (:commits repo)))))

(defn branches->nodes
  "Find ordered nodes in branch"
  [c causal-order c-nodes]
  (loop [parents (get causal-order c)
           node c
           order (list c)]
      (let [next-node (first (filter c-nodes parents))]
        (if next-node
          (recur (get causal-order next-node) next-node (conj order next-node))
          order))))


(defn node-order
  "Create node order from given branches, nodes and causal-order"
  [{:keys [branches causal-order nodes] :as repo}]
  (assoc repo :nodes
         (apply merge
                (map
                 (fn [[b h]]
                   {b (vec (branches->nodes h causal-order (get nodes b)))})
                 branches))))


(defn branch-points
  "Find branch-links in all branches"
  [{:keys [causal-order nodes commits] :as repo}]
  (assoc repo :branch-points
         (apply merge-with concat
                (map
                 (fn [[b ns]]
                   (let [root (first ns)
                         root-head (first (get causal-order root))]
                     (if root-head
                       {root-head [b]}
                       {:roots [b]})))
                 nodes))))


(defn merge-links
  "Find merge-links in all branches"
  [{:keys [causal-order commits] :as repo}]
  (assoc repo :merge-links
         (apply merge-with concat
                (map
                 (fn [[b link]]
                   {(first (remove #(= (get commits %) (get commits b)) link)) [b]})
                 (filter #(> (count (val %)) 1) causal-order)))))

(defn repo-pipeline
  "Run the pipeline"
  [repo]
  (->> (select-keys repo [:branches :causal-order :commits])
       unify-branch-heads
       commits->nodes
       node-order
       branch-points
       merge-links))


(defn compute-positions
  "Compute positions using width, height, circle size and repo data"
  [w h cs cg]
  (let [ecg (repo-pipeline cg)
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


(defn update-x-positions
  "doc-string"
  [{:keys [nodes commits] :as repo} affected-nodes start]
  nil)


(defn get-offsets
  "doc-string"
  [{:keys [nodes commits causal-order branch-points] :as repo}]
  (loop [branches (:roots branch-points)
         offset (into {} (map (fn [b] [b {:prefix 0}]) branches))
         current-nodes (get nodes (first branches))]
    (let [prefixes (->> current-nodes count range
                          (map (fn [i]
                                 (->> i (get current-nodes) (get branch-points)
                                      (map (fn [b]
                                             [b {:prefix (+ i (get-in offset [(first branches) :prefix]))}])))))
                          (apply concat)
                          (into {}))
          postfixes (->> current-nodes count range
                          (map (fn [i]
                                 (->> i (get current-nodes) (get merge-links)
                                      (map (fn [b]
                                             [b {:postfix (- (count current-nodes) i)}])))))
                          (apply concat)
                          (into {}))
          new-branches (concat (rest branches) (keys prefixes))]
      (if (empty? new-branches)
        (assoc repo :offset (merge offset prefixes postfixes))
        (recur
         new-branches
         (merge offset prefixes postfixes)
         (->> branches rest first (get nodes)))))))

(comment

  (def repo-1
    (->> test-repo
         unify-branch-heads
         commits->nodes
         node-order
         branch-points
         merge-links
         get-offsets))

  (:merge-links repo-1)

  (:offset repo-1)

  (->> repo-1
       :offset
       (map (fn [[k v]] [k (reduce + (vals v))])))

  (ap)

  )
