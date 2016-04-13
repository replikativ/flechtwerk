(ns flechtwerk.demo
  (:require [flechtwerk.core :refer [quil-commit-graph]]
            [flechtwerk.graph :refer [test-graph]]
            ;; TODO needed for full.async macros atm.
            [full.async :refer [throw-if-exception]]))

(quil-commit-graph test-graph)
