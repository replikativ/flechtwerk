(ns geschichte-gorilla.core
  (:require [geschichte-gorilla.vega :as vega]
            [geschichte-gorilla.graph :as graph]))


(defn commit-graph
  [peer & {:keys [width aspect-ratio color opacity]
           :or {width 600
                aspect-ratio 1.618
                opacity 1}}]
  (let [height (float (/ width aspect-ratio))]
    (merge
     (vega/frame width height)
     (vega/graph-marks)
     (-> (graph/compute-positions peer)
         (vega/graph-data width height)))))
