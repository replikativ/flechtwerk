(ns geschichte-gorilla.quilesque
  (:require [quil.core :as q]
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
  [{:keys [nodes links colors]}]
  (q/smooth)
  (dorun
   (for [[x1 y1 x2 y2 b] links]
     (let [[r g b] (get colors b)]
       (q/stroke r g b)
       (q/stroke-weight 2)
       (q/line x1 y1 x2 y2))))
  (dorun
   (for [[x y b] nodes]
     (let [[r g b] (get colors b)]
       (q/fill r g b)
       (q/no-stroke)
       (q/ellipse x y 10 10)))))


(defn sketch [repo-positions & {:keys [width height]
                                :or {width WIDTH
                                     height HEIGHT}}]
  (q/defsketch commit-graph
    :title "Commit graph"
    :setup (fn []
             (let [{:keys [x-positions y-positions nodes links branches]} repo-positions]
               
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
                :colors (zipmap (map first branches) (take (count branches) color-palette))}))
    :update (fn [state] state)
    :draw draw
    :size [width height]
    :middleware [m/fun-mode]))
