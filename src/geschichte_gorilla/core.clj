(ns geschichte-gorilla.core
  (:require [gorilla-repl.vega :as v]))

(defn- uuid [] (str (java.util.UUID/randomUUID)))


(defn commit-graph [data & {}])


(comment
  (v/vega-view
   {:width 400 :height 250 :pading {:bottom 20 :top 10 :right 10 :left 50}
    :data [{:name "commits" :values [{:x 10 :y 10} {:x 20 :y 15}]}]
    :marks [{:type "symbol"
             :from {:data "commits"}
             :properties
             {:enter {:x {:field "data.x"}
                      :y {:field "data.y"}
                      :fill {:value "blue"}
                      :fillOpacity {:value "1"}}
              :update {:shape "circle"
                       :size {:value 90}
                       :stroke [:value "transparent"]}
              :hover {:stroke {:value "green"}}}}]}))
