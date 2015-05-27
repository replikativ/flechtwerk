(ns geschichte-gorilla.core
  (:require [geschichte-gorilla.vega :as vega]
            [geschichte-gorilla.quilesque :as quilesque]
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


(defn sketch-graph [peer & {:keys [width aspect-ratio color opacity]
                          :or {width 800
                               aspect-ratio 1.618
                               opacity 1}}]
  (let [height (float (/ width aspect-ratio))]
    (quilesque/sketch (graph/compute-positions peer) :width width :height height)))
