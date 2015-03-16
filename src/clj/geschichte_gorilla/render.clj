(ns geschichte-gorilla.render
  (:require [gorilla-renderable.core :as render]
            [clojure.data.json :as json]))

(defrecord CommitGraphView [content opts])

(defn commit-graph-view [content & opts] (CommitGraphView. content opts))

(extend-type CommitGraphView
  render/Renderable
  (render [self]
    (let [content (str "geschichte-gorilla.core.graph-view(" (json/write-str (:content self)) ");")]
        {:type :js
         :content content
         :value (pr-str self)})))
