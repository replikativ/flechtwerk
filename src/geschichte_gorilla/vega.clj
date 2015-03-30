(ns geschichte-gorilla.vega
  (:require [geschichte-gorilla.graph :as graph]))

(defn frame
  [width height]
  {:width width
   :height height
   :padding {:top 10, :left 50, :bottom 20, :right 10}})




(defn graph-marks
  "Build node marks"
  [colour opacity]
  {:marks [{:type "symbol"
            :from {:data "nodes"}
            :properties
            {:enter {:x {:field "data.x"}
                     :y {:field "data.y"}
                     :fill {:field "data.fill"}
                     :fillOpacity {:value "1"}}
             :update {:shape "circle"
                      :size {:value 90}
                      :stroke [:value "transparent"]}}}
           {:type "path"
            :from {:data "links"}
            :properties
            {:enter {:path {:field "data.path"}
                     :stroke {:value "grey"}
                     :fill "none"
                     :strokeWidth {:value 1}}}}]})



(defn graph-data
  "Build vega data"
  [{:keys [nodes links x-positions y-positions branches]}]
  {:data
   [{:name "nodes"
     :values
     (mapv
      (fn [id] {:value id
               :x (get x-positions id)
               :y (get y-positions id)
               :fill (if (contains? (into #{} (vals branches)) id)
                       "red"
                       "steelblue")}) nodes)}
    {:name "links"
     :values
     (mapv
      (fn [[source target]]
        {:path (str "M " (float (get x-positions source))
                    " " (float (get y-positions source))
                    " L " (float (get x-positions target))
                    " " (float (get y-positions target)))})
      links)}]})


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
                    140 [130]}
     :branches {"master" 110
                "fix" 50
                "dev" 120
                "fix-2" 140}})

  (graph-data
   (graph/compute-positions 500 300 10 test-repo))

  )
