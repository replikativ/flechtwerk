(ns flechtwerk.quilesque
  (:require [quil.core :as q]
            [flechtwerk.graph :as g]
            [quil.middleware :as m]))

(declare commit-graph)

(defn- key-handler
  "Handles key events"
  [{:keys [output-file] :as state} {:keys [raw-key]}]
  (case raw-key
    \q (quil.applet/applet-close commit-graph)
    \p (when output-file
         (q/save-frame output-file)
         (println "Current frame saved to:" output-file))
    :else nil)
  state)


(defn setup [graph output-file]
  (q/frame-rate 1)
                                        ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
                                        ; setup function returns initial state. It contains
                                        ; circle color and position.
  graph)

(defn draw-graph [{:keys [nodes edges]}]
  (q/smooth)
  (q/background 240)

  (let [w (q/width)
        h (q/height)
        xp (fn [x] (+ (* 0.9 w x) 0.05))
        yp (fn [y] (+ (* 0.9 h y) 0.05))]
    (doseq [[n ps] edges
            p ps]
      (let [{x1 :x y1 :y} (nodes n)
            {x2 :x y2 :y} (nodes p)]
        (q/line (xp x1) (yp y1) (xp x2) (yp y2))))

    (doseq [[id {:keys [x y branch]}] nodes]
      (q/fill (mod (hash branch) 255) 255 255)
      (q/text (str id) (xp x) (yp y))
      (q/ellipse (xp x) (yp y) 10 10))))

(defn sketch [graph & {:keys [width height output-file update-fn]
                       :or {width 500
                            height 500
                            update-fn identity
                            output-file "/tmp/commit_graph.png"}}]

  (q/defsketch commit-graph
    :title "Commit graph"
    :size [width height]
                                        ; setup function called only once, during sketch initialization.
    :setup (partial setup graph output-file)
    :update (fn [state] (update-fn state))
    :key-typed key-handler
                                        ; update-state is called on each iteration before draw-state.
                                        ;  :update update-state
    :draw draw-graph
    :features [:keep-on-top]
                                        ; This sketch uses functional-mode middleware.
                                        ; Check quil wiki for more info about middlewares and particularly
                                        ; fun-mode.
    :middleware [m/fun-mode]))



(comment

  (def graph {1 []
              2 [1]
              3 [2]
              4 [2]
              5 [4]
              6 [5]
              7 [5]
              8 [6 2]
              9 [8 7]
              10 [5 3]
              11 [8]
              12 [11]
              13 [11]
              14 [13 10]
              15 [13]
              16 [14 15]
              17 [4]
              18 [17]
              19 [18]
              20 [19]
              21 [20]
              22 [21]
              23 [22]
              24 [23]
              25 [23]
              26 [24]
              27 [9 26]
              28 [25]})


  (sketch graph)























  (defn- hex-to-rgb
    "Thanks to https://github.com/jackrusher/quil-sketches/blob/master/src/quil_sketches/util.clj"
    [hex]
    (mapv (comp #(Integer/parseInt % 16) (partial apply str))
          (partition 2 (.replace hex "#" ""))))

  (def color-palette
    "Simple color palette"
    (mapv hex-to-rgb
          ["#001f3f"
           "#3d9970"
           "#ff851b"
           "#b10dc9"
           "#007dD9"
           "#7fdbff"
           "#39cccc"
           "#111111"
           "#2ecc40"
           "#01ff70"
           "#ffdc00"
           "#aaaaaa"
           "#ff4136"
           "#85144b"
           "#f012be"
           "#dddddd"]))


  (defn- create-setup-fn
    "Create the setup function based on give repo-positions, output-file, width and height"
    [repo-positions output-file width height]
    (fn []
      (let [{:keys [x-positions y-positions nodes links branches]} repo-positions
            circle-size (/ (max width height) 64)]
        (q/background 220)
        (q/text-size (/ height 32))
        (q/text-align :center)
        {:nodes (mapv
                 (fn [[id b]]
                   [(* width (get x-positions id))
                    (* height (get y-positions id))
                    b
                    (str id)])
                 nodes)
         :links (mapv
                 (fn [[start end b]]
                   [(* width (get x-positions start)) (* height (get y-positions start))
                    (* width (get x-positions end)) (* height (get y-positions end))
                    b])
                 links)
         :colors (zipmap (map first branches) (take (count branches) color-palette))
         :circle-size circle-size
         :line-width (/ circle-size 4)
         :width width
         :height height
         :output-file output-file})))


  (defn- draw
    "Graph draw routine"
    [{:keys [nodes links colors circle-size line-width width height] :as state}]
    (q/smooth)
    (q/background 222)
    (dorun
     (for [[x1 y1 x2 y2 b] links]
       (let [[r g b] (get colors b)]
         (q/stroke r g b)
         (q/no-fill)
         (q/stroke-weight line-width)
         (q/line x1 y1 x2 y2)
         ;; bezier experiments
         #_(if (= y1 y2)
             (q/line x1 y1 x2 y2)
             (let [cx1 (/ (+ (* x1 1.1) x2) 2)
                   cy1 (* 1.3 y1)
                   cx2 (/ (+ (* 0.9 x1) x2) 2)
                   cy2 (* 0.9 y2)]
               (q/bezier x1 y1 cx1 cy1 cx2 cy2 x2 y2))))))
    (dorun
     (for [[x y branch id] nodes]
       (let [[r g b] (get colors branch)
             radius (/ circle-size 2)]
         (q/no-stroke)
         (if (<=
              (+ (Math/pow (- (q/mouse-x) x) 2)
                 (Math/pow (- (q/mouse-y) y) 2))
              (Math/pow radius 2))
           (do
             (q/fill 200 0 0)
             (q/text id x (- y circle-size)))
           (q/fill r g b))
         (q/ellipse x y circle-size circle-size))))))
