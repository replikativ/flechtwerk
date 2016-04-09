;; gorilla-repl.fileformat = 1

;; **
;;; # Replikativ Commit Graph Plotting
;; **

;; @@
(ns harmonious-creek
  (:require [gorilla-repl.vega :as v]
            [full.async :refer [<??]]
            [konserve.memory :refer [new-mem-store]]
            [flechtwerk.core :as g]
            [flechtwerk.graph :as graph]))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
graph/test-graph
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>7</span>","value":"7"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>5</span>","value":"5"}],"value":"[5]"}],"value":"[7 [5]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>20</span>","value":"20"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>19</span>","value":"19"}],"value":"[19]"}],"value":"[20 [19]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>27</span>","value":"27"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>9</span>","value":"9"},{"type":"html","content":"<span class='clj-long'>26</span>","value":"26"}],"value":"[9 26]"}],"value":"[27 [9 26]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>1</span>","value":"1"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[],"value":"[]"}],"value":"[1 []]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>24</span>","value":"24"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>23</span>","value":"23"}],"value":"[23]"}],"value":"[24 [23]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>4</span>","value":"4"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>2</span>","value":"2"}],"value":"[2]"}],"value":"[4 [2]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>15</span>","value":"15"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>13</span>","value":"13"}],"value":"[13]"}],"value":"[15 [13]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>21</span>","value":"21"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>20</span>","value":"20"}],"value":"[20]"}],"value":"[21 [20]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>13</span>","value":"13"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>11</span>","value":"11"}],"value":"[11]"}],"value":"[13 [11]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>22</span>","value":"22"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>21</span>","value":"21"}],"value":"[21]"}],"value":"[22 [21]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>6</span>","value":"6"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>5</span>","value":"5"}],"value":"[5]"}],"value":"[6 [5]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>28</span>","value":"28"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>25</span>","value":"25"}],"value":"[25]"}],"value":"[28 [25]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>25</span>","value":"25"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>23</span>","value":"23"}],"value":"[23]"}],"value":"[25 [23]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>17</span>","value":"17"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>4</span>","value":"4"}],"value":"[4]"}],"value":"[17 [4]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>3</span>","value":"3"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>2</span>","value":"2"}],"value":"[2]"}],"value":"[3 [2]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>12</span>","value":"12"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>11</span>","value":"11"}],"value":"[11]"}],"value":"[12 [11]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>2</span>","value":"2"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>1</span>","value":"1"}],"value":"[1]"}],"value":"[2 [1]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>23</span>","value":"23"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>22</span>","value":"22"}],"value":"[22]"}],"value":"[23 [22]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>19</span>","value":"19"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>18</span>","value":"18"}],"value":"[18]"}],"value":"[19 [18]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>11</span>","value":"11"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>8</span>","value":"8"}],"value":"[8]"}],"value":"[11 [8]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>9</span>","value":"9"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>8</span>","value":"8"},{"type":"html","content":"<span class='clj-long'>7</span>","value":"7"}],"value":"[8 7]"}],"value":"[9 [8 7]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>5</span>","value":"5"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>4</span>","value":"4"}],"value":"[4]"}],"value":"[5 [4]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>14</span>","value":"14"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-long'>10</span>","value":"10"}],"value":"[13 10]"}],"value":"[14 [13 10]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>26</span>","value":"26"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>24</span>","value":"24"}],"value":"[24]"}],"value":"[26 [24]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>16</span>","value":"16"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>14</span>","value":"14"},{"type":"html","content":"<span class='clj-long'>15</span>","value":"15"}],"value":"[14 15]"}],"value":"[16 [14 15]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>10</span>","value":"10"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>5</span>","value":"5"},{"type":"html","content":"<span class='clj-long'>3</span>","value":"3"}],"value":"[5 3]"}],"value":"[10 [5 3]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>18</span>","value":"18"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>17</span>","value":"17"}],"value":"[17]"}],"value":"[18 [17]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>8</span>","value":"8"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>6</span>","value":"6"},{"type":"html","content":"<span class='clj-long'>2</span>","value":"2"}],"value":"[6 2]"}],"value":"[8 [6 2]]"}],"value":"{7 [5], 20 [19], 27 [9 26], 1 [], 24 [23], 4 [2], 15 [13], 21 [20], 13 [11], 22 [21], 6 [5], 28 [25], 25 [23], 17 [4], 3 [2], 12 [11], 2 [1], 23 [22], 19 [18], 11 [8], 9 [8 7], 5 [4], 14 [13 10], 26 [24], 16 [14 15], 10 [5 3], 18 [17], 8 [6 2]}"}
;; <=

;; @@
(def store (<?? (new-mem-store (atom (into {} (map (fn [k v] [k v]) (range 17) (repeat {:branch "master"})))))))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;harmonious-creek/store</span>","value":"#'harmonious-creek/store"}
;; <=

;; @@
(v/vega-view (<?? (g/vega-commit-graph graph/test-graph :store store)))
;; @@
;; =>
;;; {"type":"vega","content":{"width":500,"height":500,"padding":{"top":10,"left":50,"bottom":20,"right":10},"signals":[{"name":"tooltip","init":{},"streams":[{"type":"rect:mouseover","expr":"datum"},{"type":"rect:mouseout","expr":"{}"}]}],"predicates":[{"name":"tooltip","type":"==","operands":[{"signal":"tooltip._id"},{"arg":"id"}]}],"marks":[{"type":"path","from":{"data":"edges"},"properties":{"enter":{"path":{"field":"data.path"},"strokeWidth":{"value":2},"stroke":{"value":"grey"}}},"update":{"fill":{"value":"red"}}},{"type":"symbol","from":{"data":"nodes"},"properties":{"enter":{"x":{"field":"data.x"},"y":{"field":"data.y"},"fill":{"r":{"field":"data.r"},"g":{"field":"data.g"},"b":{"field":"data.b"}},"fillOpacity":{"value":"1"}},"update":{"shape":"circle","size":{"value":110},"strokeWidth":{"value":2},"stroke":{"value":"grey"}}}},{"type":"text","from":{"data":"nodes"},"properties":{"enter":{"x":{"field":"data.x"},"y":{"field":"data.y"},"align":{"value":"center"},"dy":{"value":-20},"fontSize":{"value":15},"fill":{"value":"#333"}},"update":{"text":{"field":"data.value"}}}},{"type":"text","from":{"data":"nodes"},"properties":{"enter":{"x":{"field":"data.x"},"y":{"field":"data.y"},"align":{"value":"right"},"dx":{"value":10},"fontSize":{"value":15}},"update":{"text":{"field":"data.branch"}}}}],"data":[{"name":"nodes","values":[{"value":7,"branch":"master","x":182.69230769230768,"y":190.0,"r":209,"g":289,"b":369},{"value":20,"branch":null,"x":255.7692307692308,"y":118.75,"r":0,"g":80,"b":160},{"value":27,"branch":null,"x":475.0,"y":237.5,"r":0,"g":80,"b":160},{"value":1,"branch":"master","x":36.53846153846153,"y":237.5,"r":209,"g":289,"b":369},{"value":24,"branch":null,"x":401.9230769230769,"y":158.33333333333331,"r":0,"g":80,"b":160},{"value":4,"branch":"master","x":109.61538461538464,"y":158.33333333333331,"r":209,"g":289,"b":369},{"value":15,"branch":"master","x":328.8461538461538,"y":237.5,"r":209,"g":289,"b":369},{"value":21,"branch":null,"x":292.3076923076923,"y":118.75,"r":0,"g":80,"b":160},{"value":13,"branch":"master","x":292.3076923076923,"y":237.5,"r":209,"g":289,"b":369},{"value":22,"branch":null,"x":328.8461538461538,"y":118.75,"r":0,"g":80,"b":160},{"value":6,"branch":"master","x":182.69230769230768,"y":285.0,"r":209,"g":289,"b":369},{"value":28,"branch":null,"x":438.4615384615385,"y":316.6666666666667,"r":0,"g":80,"b":160},{"value":25,"branch":null,"x":401.9230769230769,"y":316.6666666666667,"r":0,"g":80,"b":160},{"value":17,"branch":null,"x":146.15384615384616,"y":158.33333333333331,"r":0,"g":80,"b":160},{"value":3,"branch":"master","x":109.61538461538464,"y":316.6666666666667,"r":209,"g":289,"b":369},{"value":12,"branch":"master","x":292.3076923076923,"y":356.25,"r":209,"g":289,"b":369},{"value":2,"branch":"master","x":73.07692307692305,"y":237.5,"r":209,"g":289,"b":369},{"value":23,"branch":null,"x":365.38461538461536,"y":118.75,"r":0,"g":80,"b":160},{"value":19,"branch":null,"x":219.23076923076923,"y":158.33333333333331,"r":0,"g":80,"b":160},{"value":11,"branch":"master","x":255.7692307692308,"y":237.5,"r":209,"g":289,"b":369},{"value":9,"branch":"master","x":255.7692307692308,"y":356.25,"r":209,"g":289,"b":369},{"value":5,"branch":"master","x":146.15384615384616,"y":316.6666666666667,"r":209,"g":289,"b":369},{"value":14,"branch":"master","x":328.8461538461538,"y":356.25,"r":209,"g":289,"b":369},{"value":26,"branch":null,"x":438.4615384615385,"y":158.33333333333331,"r":0,"g":80,"b":160},{"value":16,"branch":"master","x":365.38461538461536,"y":356.25,"r":209,"g":289,"b":369},{"value":10,"branch":"master","x":182.69230769230768,"y":380.0,"r":209,"g":289,"b":369},{"value":18,"branch":null,"x":182.69230769230768,"y":95.0,"r":0,"g":80,"b":160},{"value":8,"branch":"master","x":219.23076923076923,"y":316.6666666666667,"r":209,"g":289,"b":369}]},{"name":"edges","values":[{"path":"M 182.69231095910072 190.00000283122063 L 146.1538515985012 316.66667610406876"},{"path":"M 255.7692438364029 118.75 L 219.23077031970024 158.33333805203438"},{"path":"M 475.0 237.5 L 255.7692438364029 356.25"},{"path":"M 475.0 237.5 L 438.4615406394005 158.33333805203438"},{"path":"M 401.92308127880096 158.33333805203438 L 365.38462191820145 118.75"},{"path":"M 109.61538515985012 158.33333805203438 L 73.0769257992506 237.5"},{"path":"M 328.84616255760193 237.5 L 292.3077031970024 237.5"},{"path":"M 292.3077031970024 118.75 L 255.7692438364029 118.75"},{"path":"M 292.3077031970024 237.5 L 255.7692438364029 237.5"},{"path":"M 328.84616255760193 118.75 L 292.3077031970024 118.75"},{"path":"M 182.69231095910072 285.0000113248825 L 146.1538515985012 316.66667610406876"},{"path":"M 438.4615406394005 316.66667610406876 L 401.92308127880096 316.66667610406876"},{"path":"M 401.92308127880096 316.66667610406876 L 365.38462191820145 118.75"},{"path":"M 146.1538515985012 158.33333805203438 L 109.61538515985012 158.33333805203438"},{"path":"M 109.61538515985012 316.66667610406876 L 73.0769257992506 237.5"},{"path":"M 292.3077031970024 356.25 L 255.7692438364029 237.5"},{"path":"M 73.0769257992506 237.5 L 36.5384628996253 237.5"},{"path":"M 365.38462191820145 118.75 L 328.84616255760193 118.75"},{"path":"M 219.23077031970024 158.33333805203438 L 182.69231095910072 95.00000141561031"},{"path":"M 255.7692438364029 237.5 L 219.23077031970024 316.66667610406876"},{"path":"M 255.7692438364029 356.25 L 219.23077031970024 316.66667610406876"},{"path":"M 255.7692438364029 356.25 L 182.69231095910072 190.00000283122063"},{"path":"M 146.1538515985012 316.66667610406876 L 109.61538515985012 158.33333805203438"},{"path":"M 328.84616255760193 356.25 L 292.3077031970024 237.5"},{"path":"M 328.84616255760193 356.25 L 182.69231095910072 380.00000566244125"},{"path":"M 438.4615406394005 158.33333805203438 L 401.92308127880096 158.33333805203438"},{"path":"M 365.38462191820145 356.25 L 328.84616255760193 356.25"},{"path":"M 365.38462191820145 356.25 L 328.84616255760193 237.5"},{"path":"M 182.69231095910072 380.00000566244125 L 146.1538515985012 316.66667610406876"},{"path":"M 182.69231095910072 380.00000566244125 L 109.61538515985012 316.66667610406876"},{"path":"M 182.69231095910072 95.00000141561031 L 146.1538515985012 158.33333805203438"},{"path":"M 219.23077031970024 316.66667610406876 L 182.69231095910072 285.0000113248825"},{"path":"M 219.23077031970024 316.66667610406876 L 73.0769257992506 237.5"}]}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 500, :height 500, :padding {:top 10, :left 50, :bottom 20, :right 10}, :signals [{:name \"tooltip\", :init {}, :streams [{:type \"rect:mouseover\", :expr \"datum\"} {:type \"rect:mouseout\", :expr \"{}\"}]}], :predicates [{:name \"tooltip\", :type \"==\", :operands [{:signal \"tooltip._id\"} {:arg \"id\"}]}], :marks [{:type \"path\", :from {:data \"edges\"}, :properties {:enter {:path {:field \"data.path\"}, :strokeWidth {:value 2}, :stroke {:value \"grey\"}}}, :update {:fill {:value \"red\"}}} {:type \"symbol\", :from {:data \"nodes\"}, :properties {:enter {:x {:field \"data.x\"}, :y {:field \"data.y\"}, :fill {:r {:field \"data.r\"}, :g {:field \"data.g\"}, :b {:field \"data.b\"}}, :fillOpacity {:value \"1\"}}, :update {:shape \"circle\", :size {:value 110}, :strokeWidth {:value 2}, :stroke {:value \"grey\"}}}} {:type \"text\", :from {:data \"nodes\"}, :properties {:enter {:x {:field \"data.x\"}, :y {:field \"data.y\"}, :align {:value \"center\"}, :dy {:value -20}, :fontSize {:value 15}, :fill {:value \"#333\"}}, :update {:text {:field \"data.value\"}}}} {:type \"text\", :from {:data \"nodes\"}, :properties {:enter {:x {:field \"data.x\"}, :y {:field \"data.y\"}, :align {:value \"right\"}, :dx {:value 10}, :fontSize {:value 15}}, :update {:text {:field \"data.branch\"}}}}], :data [{:name \"nodes\", :values [{:value 7, :branch \"master\", :x 182.69230769230768, :y 190.0, :r 209, :g 289, :b 369} {:value 20, :branch nil, :x 255.7692307692308, :y 118.75, :r 0, :g 80, :b 160} {:value 27, :branch nil, :x 475.0, :y 237.5, :r 0, :g 80, :b 160} {:value 1, :branch \"master\", :x 36.53846153846153, :y 237.5, :r 209, :g 289, :b 369} {:value 24, :branch nil, :x 401.9230769230769, :y 158.33333333333331, :r 0, :g 80, :b 160} {:value 4, :branch \"master\", :x 109.61538461538464, :y 158.33333333333331, :r 209, :g 289, :b 369} {:value 15, :branch \"master\", :x 328.8461538461538, :y 237.5, :r 209, :g 289, :b 369} {:value 21, :branch nil, :x 292.3076923076923, :y 118.75, :r 0, :g 80, :b 160} {:value 13, :branch \"master\", :x 292.3076923076923, :y 237.5, :r 209, :g 289, :b 369} {:value 22, :branch nil, :x 328.8461538461538, :y 118.75, :r 0, :g 80, :b 160} {:value 6, :branch \"master\", :x 182.69230769230768, :y 285.0, :r 209, :g 289, :b 369} {:value 28, :branch nil, :x 438.4615384615385, :y 316.6666666666667, :r 0, :g 80, :b 160} {:value 25, :branch nil, :x 401.9230769230769, :y 316.6666666666667, :r 0, :g 80, :b 160} {:value 17, :branch nil, :x 146.15384615384616, :y 158.33333333333331, :r 0, :g 80, :b 160} {:value 3, :branch \"master\", :x 109.61538461538464, :y 316.6666666666667, :r 209, :g 289, :b 369} {:value 12, :branch \"master\", :x 292.3076923076923, :y 356.25, :r 209, :g 289, :b 369} {:value 2, :branch \"master\", :x 73.07692307692305, :y 237.5, :r 209, :g 289, :b 369} {:value 23, :branch nil, :x 365.38461538461536, :y 118.75, :r 0, :g 80, :b 160} {:value 19, :branch nil, :x 219.23076923076923, :y 158.33333333333331, :r 0, :g 80, :b 160} {:value 11, :branch \"master\", :x 255.7692307692308, :y 237.5, :r 209, :g 289, :b 369} {:value 9, :branch \"master\", :x 255.7692307692308, :y 356.25, :r 209, :g 289, :b 369} {:value 5, :branch \"master\", :x 146.15384615384616, :y 316.6666666666667, :r 209, :g 289, :b 369} {:value 14, :branch \"master\", :x 328.8461538461538, :y 356.25, :r 209, :g 289, :b 369} {:value 26, :branch nil, :x 438.4615384615385, :y 158.33333333333331, :r 0, :g 80, :b 160} {:value 16, :branch \"master\", :x 365.38461538461536, :y 356.25, :r 209, :g 289, :b 369} {:value 10, :branch \"master\", :x 182.69230769230768, :y 380.0, :r 209, :g 289, :b 369} {:value 18, :branch nil, :x 182.69230769230768, :y 95.0, :r 0, :g 80, :b 160} {:value 8, :branch \"master\", :x 219.23076923076923, :y 316.6666666666667, :r 209, :g 289, :b 369}]} {:name \"edges\", :values [{:path \"M 182.69231095910072 190.00000283122063 L 146.1538515985012 316.66667610406876\"} {:path \"M 255.7692438364029 118.75 L 219.23077031970024 158.33333805203438\"} {:path \"M 475.0 237.5 L 255.7692438364029 356.25\"} {:path \"M 475.0 237.5 L 438.4615406394005 158.33333805203438\"} {:path \"M 401.92308127880096 158.33333805203438 L 365.38462191820145 118.75\"} {:path \"M 109.61538515985012 158.33333805203438 L 73.0769257992506 237.5\"} {:path \"M 328.84616255760193 237.5 L 292.3077031970024 237.5\"} {:path \"M 292.3077031970024 118.75 L 255.7692438364029 118.75\"} {:path \"M 292.3077031970024 237.5 L 255.7692438364029 237.5\"} {:path \"M 328.84616255760193 118.75 L 292.3077031970024 118.75\"} {:path \"M 182.69231095910072 285.0000113248825 L 146.1538515985012 316.66667610406876\"} {:path \"M 438.4615406394005 316.66667610406876 L 401.92308127880096 316.66667610406876\"} {:path \"M 401.92308127880096 316.66667610406876 L 365.38462191820145 118.75\"} {:path \"M 146.1538515985012 158.33333805203438 L 109.61538515985012 158.33333805203438\"} {:path \"M 109.61538515985012 316.66667610406876 L 73.0769257992506 237.5\"} {:path \"M 292.3077031970024 356.25 L 255.7692438364029 237.5\"} {:path \"M 73.0769257992506 237.5 L 36.5384628996253 237.5\"} {:path \"M 365.38462191820145 118.75 L 328.84616255760193 118.75\"} {:path \"M 219.23077031970024 158.33333805203438 L 182.69231095910072 95.00000141561031\"} {:path \"M 255.7692438364029 237.5 L 219.23077031970024 316.66667610406876\"} {:path \"M 255.7692438364029 356.25 L 219.23077031970024 316.66667610406876\"} {:path \"M 255.7692438364029 356.25 L 182.69231095910072 190.00000283122063\"} {:path \"M 146.1538515985012 316.66667610406876 L 109.61538515985012 158.33333805203438\"} {:path \"M 328.84616255760193 356.25 L 292.3077031970024 237.5\"} {:path \"M 328.84616255760193 356.25 L 182.69231095910072 380.00000566244125\"} {:path \"M 438.4615406394005 158.33333805203438 L 401.92308127880096 158.33333805203438\"} {:path \"M 365.38462191820145 356.25 L 328.84616255760193 356.25\"} {:path \"M 365.38462191820145 356.25 L 328.84616255760193 237.5\"} {:path \"M 182.69231095910072 380.00000566244125 L 146.1538515985012 316.66667610406876\"} {:path \"M 182.69231095910072 380.00000566244125 L 109.61538515985012 316.66667610406876\"} {:path \"M 182.69231095910072 95.00000141561031 L 146.1538515985012 158.33333805203438\"} {:path \"M 219.23077031970024 316.66667610406876 L 182.69231095910072 285.0000113248825\"} {:path \"M 219.23077031970024 316.66667610406876 L 73.0769257992506 237.5\"}]}]}}"}
;; <=

;; @@
(q/sketch (<?? (g/quil-commit-graph graph/test-graph :store store)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;flechtwerk.quilesque/commit-graph</span>","value":"#'flechtwerk.quilesque/commit-graph"}
;; <=

;; @@

;; @@
