(ns geschichte-gorilla.graph)

(defn- positions
  "Find index in given collection by given predicat

   Thanks to: http://stackoverflow.com/questions/4830900/how-do-i-find-the-index-of-an-item-in-a-vector"
  [pred coll]
  (keep-indexed (fn [idx x] (when (pred x) idx)) coll))

(defn- unify-branch-heads
  "Merge all possible branch heads into single master branch"
  [repo]
  (update-in repo [:branches] (fn [b] (into {} (map (fn [[k v]] [k (first v)]) b)))))

(defn- commits->nodes
  "Extract nodes of each branch"
  [repo]
  (assoc repo :nodes
         (apply merge-with (comp set concat)
                (map (fn [[n b]] {b #{n}}) (:commits repo)))))

(defn- branches->nodes
  "Find ordered nodes in branch"
  [c causal-order c-nodes]
  (loop [parents (get causal-order c)
         node c
         order (list c)]
    (let [next-node (first (filter c-nodes parents))]
      (if next-node
        (recur (get causal-order next-node) next-node (conj order next-node))
        order))))

(defn- node-order
  "Create node order from given branches, nodes and causal-order"
  [{:keys [branches causal-order nodes] :as repo}]
  (assoc repo :nodes
         (apply merge
                (map
                 (fn [[b h]]
                   {b (vec (branches->nodes h causal-order (get nodes b)))})
                 branches))))

(defn- branch-points
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

(defn- merge-links
  "Find merge-links in all branches"
  [{:keys [causal-order commits] :as repo}]
  (assoc repo :merge-links
         (apply merge-with concat
                (map
                 (fn [[b link]]
                   {b (first (remove #(= (get commits %) (get commits b)) link)) })
                 (filter #(> (count (val %)) 1) causal-order)))))

(defn- prepare-graph
  "Create all links from nodes, find start and end points"
  [repo]
  (let [{:keys [nodes merge-links branch-points branches commits]} repo
        pure-links (->> nodes
                        (map (fn [[b ns]] (map vec (partition 2 1 ns))))
                        (apply concat))
        branch-links (->> branch-points
                          (map (fn [[id bs]]
                                 (map (fn [b] [id (-> nodes (get b) (get 0))]) bs)))
                          (apply concat)
                          (remove #(= (first %) :roots)))
        start-points (into #{} (map #(-> nodes (get %) (get 0)) (get branch-points :roots)))
        end-points (into #{} (remove (into #{} (concat (vals merge-links) (keys branch-points))) (vals branches)))
        clean-nodes (apply concat (map (fn [[_ ns]] (remove (into start-points end-points) ns)) nodes))
        all-links (vec (concat pure-links branch-links (map vec merge-links)))]
    {:links (->> clean-nodes
                 (map (fn [n] {n (->> all-links
                                     (filter #(contains? (into #{} %) n))
                                     flatten
                                     (remove #{n} )
                                     (into #{}))}))
                 (apply merge))
     :all-links all-links
     :all-nodes nodes
     :nodes clean-nodes
     :start-points start-points
     :end-points end-points}))

(defn- repo-pipeline
  "Run the pipeline"
  [repo]
  (->> repo
       unify-branch-heads
       commits->nodes
       node-order
       branch-points
       merge-links))

(defn- force-x-pos [repo]
  (let [{:keys [links nodes end-points start-points] :as graph} (prepare-graph repo)]
    (loop [counter 0
           current-nodes nodes
           x-positions (merge
                        (zipmap start-points (repeat (count start-points) 0.05))
                        (zipmap end-points (repeat (count end-points) 0.95))
                        (zipmap nodes (repeatedly (count nodes) rand)))]
      (if (= counter 1000)
        (assoc graph :x-positions x-positions)
        (if (empty? current-nodes)
          (recur (inc counter) nodes x-positions)
          (let [current-node (first current-nodes)
                current-position (get x-positions current-node)
                delta (reduce + (map (fn [id] (* 0.05 (* 1.8 (- (get x-positions id) current-position)))) (get links current-node)))]
            (recur counter (rest current-nodes) (update-in x-positions [current-node] + delta))))))))

(defn compute-positions
  "Compute x-y positions from given causal-order branches and commits of a repo. The resulting positions are relative to a given screen width and height.

  Example:
  (def causal-order {10 [] 20 [10]})
  (def commits {10 :master 20 :master})
  (def branches {:master #{20}})
  (compute-positions causal-order branches commits)

  => {:branches [[:master 20]]
      :links ([10 20 :master])
      :nodes [[10 :master] [20 :master]]
      :x-order (:master)
      :x-positions {10 0.05, 20 0.95}
      :y-positions {10 1/2, 20 1/2}}"
  [causal-order branches commits]
  (let [pipeline (repo-pipeline {:causal-order causal-order :branches branches :commits commits})
        {:keys [all-links all-nodes x-positions] :as graph-data} (force-x-pos pipeline)
        x-order (let [sorted-nodes (split-at (/ (count (keys branches)) 2) (keys (sort-by #(count (val %)) > all-nodes)))]
                  (concat (reverse (first sorted-nodes)) (second sorted-nodes) ))]
    {:nodes (vec (apply concat (map (fn [[b ns]] (map (fn [n] [n b]) ns)) all-nodes)))
     :links (map (fn [[s t]] [s t (get commits t)]) all-links)
     :x-order x-order
     :x-positions x-positions
     :y-positions (apply merge (apply concat (map (fn [[b ns]] (map (fn [n] {n (/ (inc (first (positions #{b} x-order))) (inc (count x-order)))}) ns)) all-nodes)))
     :branches (mapv (fn [[b ns]] [b (last ns)]) all-nodes)}))
