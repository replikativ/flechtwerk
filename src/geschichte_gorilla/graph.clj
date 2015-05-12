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
                   {b [(first (remove #(= (get commits %) (get commits b)) link))] })
                 (filter #(> (count (val %)) 1) causal-order)))))


(defn get-prefix
  "doc-string"
  [{:keys [nodes commits causal-order branch-points] :as repo}]
  (loop [branches (:roots branch-points)
         prefix (into {} (map (fn [b] [b 0]) branches))
         current-nodes (get nodes (first branches))]
    (let [new-prefix (->> current-nodes count range
                          (map (fn [i]
                                 (->> (get current-nodes i)
                                      (get branch-points)
                                      (map (fn [b]
                                             [b (+ i
                                                   (get prefix (first branches)))])))))
                          (apply concat)
                          (into {}))
          new-branches (concat (rest branches) (keys new-prefix))]
      (if (empty? new-branches)
        (assoc repo :prefix (merge prefix new-prefix))
        (recur
         new-branches
         (merge prefix new-prefix)
         (->> branches rest first (get nodes)))))))


(defn get-x-order [{:keys [prefix] :as repo}]
  (assoc repo :x-order (->> prefix (sort-by val <) keys vec)))


(defn repo-pipeline
  "Run the pipeline"
  [repo]
  (->> (select-keys repo [:branches :causal-order :commits])
       unify-branch-heads
       commits->nodes
       node-order
       branch-points
       merge-links
       get-prefix
       get-x-order))


(defn compute-positions
  "Compute positions using width, height, circle size and repo data"
  [cs]
  (let [{:keys [nodes offset x-order] :as repo} (repo-pipeline cs)
        y-order (let [parts (split-at (/ (count x-order) 2) x-order)]
                  (vec (concat (-> parts last reverse) (first parts))))]
    (loop [branches x-order
           x-positions {}
           y-positions {}]
      (if (empty? branches)
        (let [all-nodes (vec (apply concat (vals nodes)))]
          (assoc repo
            :x-positions x-positions
            :y-positions y-positions
            :y-order y-order
            :nodes all-nodes
            :links (mapv vec (partition 2 1 all-nodes))))
        (let [current-branch (first branches)
              current-nodes (get nodes current-branch)
              current-offset (get-in offset [current-branch :prefix])
              new-y-positions (zipmap current-nodes
                                      (repeat (count current-nodes) (/ (first (positions #{current-branch} y-order))
                                                                       (count y-order))))
              new-x-positions (zipmap current-nodes
                                      (map
                                       (fn [i]
                                         (/ (+ current-offset  i)
                                            (+ current-offset (count current-nodes))))
                                       (-> current-nodes count range)))]
          (recur (rest branches)
                 (merge x-positions new-x-positions)
                 (merge y-positions new-y-positions)))))))


(comment

  (def repo-1 (repo-pipeline test-repo))


  repo-1

  (select-keys (compute-positions test-repo) [:nodes :links])


  (let [{:keys [nodes merge-links commits]} repo-1
        sorted-branches (keys (sort-by #(-> % val count) > nodes))]
    (loop [branches sorted-branches
           postfix (zipmap sorted-branches (repeat (count sorted-branches) 0))]
      (if (empty? branches)
        postfix
        (let [current-branch (first branches)
              current-nodes (-> (get nodes current-branch) reverse vec)
              new-postfix (->> current-nodes count range
                               (map
                                (fn [i]
                                  (let [current-node (get current-nodes i)]
                                    (if-let [merge-node (first (merge-links current-node))]
                                      (let [merge-from (get commits merge-node)
                                            offset (first (positions #{merge-node} (vec (reverse (get nodes merge-from)))))]
                                        {merge-from (max offset (+ i (get postfix current-branch)))})))))
                               (apply merge-with max))]
          (println postfix new-postfix current-branch)
          (recur (rest branches)
                 (merge-with max postfix new-postfix))))))

  (ap)


  )
