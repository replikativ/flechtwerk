(ns geschichte-gorilla.render
  (:require [gorilla-renderable.core :as render]))


(defrecord CommitGraphView [content opts])

(defn commit-graph-view [content & opts] (CommitGraphView. content opts))

(extend-type CommitGraphView
  render/Renderable
  (render [self]
    {:type :js
     :content (format  )
     :value}))
