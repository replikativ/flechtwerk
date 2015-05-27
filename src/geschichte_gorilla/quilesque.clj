(ns geschichte-gorilla.quilesque
  (:require [quil.core :as q]
            [geschichte-gorilla.graph :as graph]
            [quil.middleware :as m]))

(defn hex-to-rgb
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

(defn draw
  "Graph draw routine"
  [{:keys [nodes links colors circle-size line-width] :as state}]
  (q/smooth)
  (dorun
   (for [[x1 y1 x2 y2 b] links]
     (let [[r g b] (get colors b)]
       (q/stroke r g b)
       (q/no-fill)
       (q/stroke-weight line-width)
       (q/line x1 y1 x2 y2))))
  (dorun
   (for [[x y b] nodes]
     (let [[r g b] (get colors b)]
       (q/fill r g b)
       (q/no-stroke)
       (q/ellipse x y circle-size circle-size)))))


(defn mouse-clicked
  "Store current frame to output file if middle mouse button is clicked.
  Specific output name as sketch parameter"
  [{:keys [output-file] :as state} {:keys [x y button]}]
  (when (= button :center)
    (q/save-frame output-file)
    (println "Saving current frame to:" output-file))
  state)


(defn sketch
  "Render commit graph by using given positions.
  Optionally a width, height, update function and output-file can be given."
  [repo-positions & {:keys [width height update-fn output-file]
                     :or {width 700
                          height 700
                          update-fn identity}}]
  (q/defsketch commit-graph
    :title "Commit graph"
    :setup (fn []
             (let [{:keys [x-positions y-positions nodes links branches]} repo-positions
                   circle-size (/ (min width height) 64)]
               (q/background 220)
               {:nodes (mapv
                        (fn [[id b]]
                          [(* width (get x-positions id))
                           (* height (get y-positions id)) b])
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
                :output-file output-file}))
    :update (fn [state] (update-fn state))
    :draw draw
    :size [width height]
    :mouse-clicked (if output-file mouse-clicked nil)
    :middleware [m/fun-mode]))


(comment
  
  (sketch (graph/compute-positions graph/test-repo) :output-file "test.png")

  
  )
