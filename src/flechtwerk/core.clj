(ns flechtwerk.core
  (:require [flechtwerk.vega :as vega]
            [flechtwerk.quilesque :as quilesque]
            [flechtwerk.graph :as graph]
            [full.async :refer [go-try <?]]))


(defn vega-commit-graph
  "Create vega structure to embed in a gorilla-repl view."
  [commit-graph & {:keys [width height store]
                   :or {width 500
                        height 500}}]
  (go-try
   (let [g (graph/compute-positions commit-graph)
         g (if store (<? (graph/load-commits g store)) g)]
     (merge
      (vega/frame width height)
      (vega/graph-marks)
      (vega/graph-data g width height)))))


(defn quil-commit-graph
  "Draw commit graph using quil.
  Provide width or aspect ratio for the frame."
  [commit-graph & {:keys [width height outfile store]
                   :or {width 500
                        height 500}}]
  (go-try
   (let [g (graph/compute-positions commit-graph)
         g (if store (<? (graph/load-commits g store)) g)]
     (quilesque/sketch g :width width :height height :outfile outfile))))





(comment

  (def causal-order {10 [] 20 [10] 30 [20] 40 [20]})

  (quilesque/sketch (graph/compute-positions causal-order))

  (quil-commit-graph causal-order)


  (quil-commit-graph {1 []
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
                      28 [25]}
                     {"master" #{28}
                      "eve" #{16}}
                     {0 "master", 7 "master", 20 "eve", 27 "eve", 1 "master", 24 "eve", 4 "master", 15 "master", 21 "eve", 13 "master", 22 "eve", 6 "master", 28 "eve", 25 "eve", 17 "eve", 3 "master", 12 "master", 2 "master", 23 "eve", 19 "eve", 11 "master", 9 "master", 5 "master", 14 "master", 26 "eve", 16 "master", 10 "master", 18 "eve", 8 "master"}))
