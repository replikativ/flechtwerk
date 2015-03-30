;; gorilla-repl.fileformat = 1

;; **
;;; # Gorilla REPL
;;; 
;;; Welcome to gorilla :-)
;;; 
;;; Shift + enter evaluates code. Hit alt+g twice in quick succession or click the menu icon (upper-right corner) for more commands ...
;;; 
;;; It's a good habit to run each worksheet in its own namespace: feel free to use the declaration we've provided below if you'd like.
;; **

;; @@
(ns wandering-sunset
  (:require [gorilla-plot.core :as plot]
            [geschichte-gorilla.core :as g]))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
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
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;wandering-sunset/test-peer</span>","value":"#'wandering-sunset/test-peer"}
;; <=

;; @@
(g/commit-graph "konnys stuff" test-peer)
;; @@
;; =>
;;; {"type":"vega","content":{"data":[{"name":"konnys stuff","values":[{"value":10,"x":85.71428571428571,"y":188.13968658447266,"fill":"steelblue"},{"value":20,"x":151.4285714285714,"y":188.13968658447266,"fill":"steelblue"},{"value":30,"x":217.1428571428571,"y":188.13968658447266,"fill":"steelblue"},{"value":60,"x":282.8571428571429,"y":188.13968658447266,"fill":"steelblue"},{"value":70,"x":348.5714285714286,"y":188.13968658447266,"fill":"steelblue"},{"value":100,"x":414.2857142857143,"y":188.13968658447266,"fill":"steelblue"},{"value":110,"x":480,"y":188.13968658447266,"fill":"red"},{"value":80,"x":304.7619047619048,"y":120.8838119506836,"fill":"steelblue"},{"value":90,"x":392.3809523809524,"y":120.8838119506836,"fill":"steelblue"},{"value":120,"x":480,"y":120.8838119506836,"fill":"red"},{"value":130,"x":282.8571428571429,"y":255.39556121826172,"fill":"steelblue"},{"value":140,"x":348.5714285714286,"y":255.39556121826172,"fill":"red"},{"value":40,"x":195.2380952380952,"y":53.62793731689453,"fill":"steelblue"},{"value":50,"x":239.047619047619,"y":53.62793731689453,"fill":"red"}]}],"marks":[{"type":"symbol","from":{"data":"konnys stuff"},"properties":{"update":{"stroke":["value","transparent"],"size":{"value":90},"shape":"circle"},"enter":{"y":{"field":"data.y"},"fill":{"field":"data.fill"},"fillOpacity":{"value":"1"},"x":{"field":"data.x"}},"hover":{"fill":{"value":"green"}}}}],"width":500,"height":309.02349853515625,"padding":{"bottom":20,"top":10,"right":10,"left":50}},"value":"#gorilla_repl.vega.VegaView{:content {:data [{:name \"konnys stuff\", :values [{:value 10, :x 600/7, :y 188.13968658447266, :fill \"steelblue\"} {:value 20, :x 1060/7, :y 188.13968658447266, :fill \"steelblue\"} {:value 30, :x 1520/7, :y 188.13968658447266, :fill \"steelblue\"} {:value 60, :x 1980/7, :y 188.13968658447266, :fill \"steelblue\"} {:value 70, :x 2440/7, :y 188.13968658447266, :fill \"steelblue\"} {:value 100, :x 2900/7, :y 188.13968658447266, :fill \"steelblue\"} {:value 110, :x 480N, :y 188.13968658447266, :fill \"red\"} {:value 80, :x 6400/21, :y 120.8838119506836, :fill \"steelblue\"} {:value 90, :x 8240/21, :y 120.8838119506836, :fill \"steelblue\"} {:value 120, :x 480N, :y 120.8838119506836, :fill \"red\"} {:value 130, :x 1980/7, :y 255.39556121826172, :fill \"steelblue\"} {:value 140, :x 2440/7, :y 255.39556121826172, :fill \"red\"} {:value 40, :x 4100/21, :y 53.62793731689453, :fill \"steelblue\"} {:value 50, :x 5020/21, :y 53.62793731689453, :fill \"red\"}]}], :marks [{:type \"symbol\", :from {:data \"konnys stuff\"}, :properties {:update {:stroke [:value \"transparent\"], :size {:value 90}, :shape \"circle\"}, :enter {:y {:field \"data.y\"}, :fill {:field \"data.fill\"}, :fillOpacity {:value \"1\"}, :x {:field \"data.x\"}}, :hover {:fill {:value \"green\"}}}}], :width 500, :height 309.0235, :padding {:bottom 20, :top 10, :right 10, :left 50}}}"}
;; <=

;; @@

;; @@
