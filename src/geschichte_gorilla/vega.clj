(ns geschichte-gorilla.vega
  (:require [geschichte-gorilla.graph :as graph]))

(defn frame
  "Build container that holds the svg"
  [width height]
  {:width width
   :height height
   :padding {:top 10, :left 50, :bottom 20, :right 10}})




(defn graph-marks
  "Build node and link marks"
  []
  {:marks [{:type "path"
            :from {:data "links"}
            :properties
            {:enter {:path {:field "data.path"}
                     :strokeWidth {:value 2}
                     :stroke {:r {:field "data.r"}
                              :g {:field "data.g"}
                              :b {:field "data.b"}}}}}
           {:type "symbol"
            :from {:data "nodes"}
            :properties
            {:enter {:x {:field "data.x"}
                     :y {:field "data.y"}
                     :fill {:field "data.fill"}
                     :fillOpacity {:value "1"}}
             :update {:shape "circle"
                      :size {:value 90}
                      :stroke [:value "transparent"]}}}]})



(defn graph-data
  "Build vega data structures"
  [{:keys [nodes links x-positions y-positions branches x-order]}]
  (let [color (zipmap x-order (take (count x-order)
                                    (repeatedly (fn [] {:r (rand-int 256)
                                                       :g (rand-int 256)
                                                       :b (rand-int 256)}))))]
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
        (fn [[source target branch]]
          {:path (str "M " (float (get x-positions source))
                      " " (float (get y-positions source))
                      " L " (float (get x-positions target))
                      " " (float (get y-positions target)))
           :r (get-in color [branch :r])
           :g (get-in color [branch :g])
           :b (get-in color [branch :b])})
        links)}]}))
