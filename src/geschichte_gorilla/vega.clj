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
                              :b {:field "data.b"}}
                     #_{:value "grey"}}}}
           {:type "symbol"
            :from {:data "nodes"}
            :properties
            {:enter {:x {:field "data.x"}
                     :y {:field "data.y"}
                     :fill {:r {:field "data.r"}
                            :g {:field "data.g"}
                            :b {:field "data.b"}}
                     :fillOpacity {:value "1"}}
             :update {:shape "circle"
                      :size {:value 110}
                      :stroke [:value "transparent"]}}}
           {:type "text"
            :from {:data "node-labels"}
            :properties
            {:enter {:x {:field "data.x"}
                     :y {:field "data.y"}
                     :align {:value  "center"}
                     :dy {:value -20}
                     :fontSize {:value 15}
                     :fill {:value "black"}}
             :update {:text {:field "data.value"}}}}
           {:type "text"
            :from {:data "labels"}
            :properties
            {:enter {:x {:field "data.x"}
                     :y {:field "data.y"}
                     :align {:value  "right"}
                     :dx {:value 10}
                     :fontSize {:value 15}
                     :fill {:value "black"}}
             :update {:text {:field "data.value"}}}}]})



(defn graph-data
  "Build vega data structures"
  [{:keys [nodes links x-positions y-positions branches x-order]} w h]
  (let [color (zipmap x-order (take (count x-order)
                                    (repeatedly (fn [] {:r (rand-int 256)
                                                        :g (rand-int 256)
                                                        :b (rand-int 256)}))))
        w (* 0.95 w)
        h (* 0.95 h)]
    {:data
     [{:name "nodes"
       :values
       (mapv
        (fn [[id branch]] {:value id
                           :x (* (get x-positions id) w)
                           :y (* (get y-positions id) h)
                           :r (get-in color [branch :r])
                           :g (get-in color [branch :g])
                           :b (get-in color [branch :b])
                           })
        nodes)}
      {:name "links"
       :values
       (mapv
        (fn [[source target branch]]
          {:path (str "M " (* w (float (get x-positions source)))
                      " " (* h (float (get y-positions source)))
                      " L " (* w (float (get x-positions target)))
                      " " (* h (float (get y-positions target))))
           :r (get-in color [branch :r])
           :g (get-in color [branch :g])
           :b (get-in color [branch :b])})
        links)}
      {:name "node-labels"
       :values
       (mapv
        (fn [[id branch]]
          {:value id
           :x (* (get x-positions id) w)
           :y (* (get y-positions id) h)})
        nodes)}
      {:name "labels"
       :values
       (mapv
        (fn [[k v]]
          {:value k
           :x (* (get x-positions v) w)
           :y (* (get y-positions v) h)})
        branches)}]}))
