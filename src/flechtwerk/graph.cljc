(ns flechtwerk.graph
  (:require [konserve.core :as k]
            #?@(:clj [[full.async :refer [<? go-try <<?]]
                      [full.lab :refer [go-for]]]))
  #?(:cljs (:require-macros [full.async :refer [<? go-try <<?]]
                            [full.lab :refer [go-for]])))


(def test-graph {1 []
                 2 [1]
                 3 [2]
                 4 [2]
                 5 [4]
                 6 [5]
                 7 [5]
                 8 [6 2]
                 9 [8 7]
                 10 [5 3]
                 11 [8]
                 12 [11]
                 13 [11]
                 14 [13 10]
                 15 [13]
                 16 [14 15]
                 17 [4]
                 18 [17]
                 19 [18]
                 20 [19]
                 21 [20]
                 22 [21]
                 23 [22]
                 24 [23]
                 25 [23]
                 26 [24]
                 27 [9 26]
                 28 [25]})


(defn invert [dag]
  (reduce (fn [inv [n ps]]
            (let [res (reduce (fn [inv p]
                                (update inv p #(conj (or % #{}) n)))
                              inv ps)]
              (if (= ps [])
                (assoc res :root n)
                res)))
          {}
          dag))


(defn igraph->tracks
  ([ig] (igraph->tracks ig [(:root ig)] (:root ig)))
  ([ig track node]
   (let [cs (seq (ig node))
         [f s] cs]
     (cond (and f (not s))
           (recur ig (conj track f) f)

           (and f s)
           (conj track (mapv #(igraph->tracks ig [%] %) cs))

           :else
           track))))


(defn tracks->indexed-nodes
  ([lg] (first (tracks->indexed-nodes {} 1 lg)))
  ([lg pos [f & r]]
   (cond (not f) [lg pos]

         (not (vector? f))
         (recur (assoc lg pos [f]) (inc pos) r)

         :else
         (let [res (mapv #(tracks->indexed-nodes {} pos %) f)
               npos (apply max (map second res))
               lg (apply (partial merge-with (comp vec concat)) lg (map first res))]
           (recur lg (inc pos) r)))))





(defn only-last-merge-point
  ([indexed-pos] (only-last-merge-point #{} {} (sort-by first > indexed-pos)))
  ([visited clean [[k v] & r]]
   (if (not k) clean
       (recur (into visited v)
              (assoc clean k (vec (filter (comp not visited) v)))
              r))))




(defn indexed-nodes->positioned-nodes [indexed-nodes]
  (let [maxp (apply max (keys indexed-nodes))]
    (reduce (fn [pos-g [pos ns]]
              (let [c (count ns)]
                (reduce (fn [pos-g [n i]]
                          (assoc pos-g n {:y (/ (inc i)
                                                (inc c))
                                          :x (/ pos
                                                maxp)}))
                        pos-g
                        (map (fn [n i] [n i]) ns (range c)))))
            {}
            indexed-nodes)))




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
  [causal-order]
  {:nodes (-> causal-order
              invert
              igraph->tracks
              tracks->indexed-nodes
              only-last-merge-point
              indexed-nodes->positioned-nodes)
   :edges causal-order})



(defn load-commits [{:keys [nodes] :as graph} store]
  (go-try (assoc graph :nodes (->> (go-for [[id v] nodes]
                                           [id (merge v (<? (k/get-in store [id])))])
                                   <<?
                                   (into {})))))



(comment
  (require '[konserve.memory :refer [new-mem-store]]
           '[full.async :refer [<??]])

  (<?? (load-commits {1 {:x 1 :y 1}
                      2 {:x 2 :y 2}}
                     (<?? (new-mem-store (atom {1 {:author "foo@bar.com"
                                                   :branch "master"}
                                                2 {:author "eve@bar.com"
                                                   :branch "kaboom"}})))))

  (def store (<?? (new-mem-store (atom (into {} (map (fn [k v] [k v]) (range 16) (repeat {:branch "master"})))))))

  (<?? (load-commits (compute-positions test-graph) store))


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
  )
