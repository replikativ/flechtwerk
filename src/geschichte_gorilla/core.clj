(ns geschichte-gorilla.core
  (:require [geschichte-gorilla.vega :as vega]
            [gorilla-repl.vega :as v]
            [geschichte-gorilla.graph :as graph]))


(defn commit-graph
  [peer & {:keys [width aspect-ratio color opacity]
                   :or {width 500
                        aspect-ratio 1.618
                        color "steelblue"
                        opacity 1}}]
  (let [height (float (/ width aspect-ratio))]
    (v/vega-view
     (merge
      (vega/frame width height)
      (vega/graph-marks color opacity)
      (->> (graph/compute-positions width height 20 peer)
           vega/graph-data)))))


(comment
  (def test-peer
    {"konnys stuff"
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
                     140 [130]}
      :branches {"master" 110
                 "fix" 50
                 "dev" 120
                 "fix-2" 140}}})

  (commit-graph "konnys stuff" test-peer)

  (let [repo-id "konnys stuff"
        width 500
        height 300]
    (->> (get test-peer repo-id)
         (graph/compute-positions width height 20)
         (vega/graph-data repo-id)))

  )
