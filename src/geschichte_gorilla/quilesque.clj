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


(defn create-setup-fn
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


(defn draw
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
       (q/ellipse x y circle-size circle-size)))))


(defn key-handler
  "Handles key events"
  [{:keys [output-file] :as state} {:keys [raw-key]}]
  (case raw-key 
    \q (quil.applet/applet-close commit-graph)
    \p (when output-file
         (q/save-frame output-file)
         (println "Current frame saved to:" output-file)))
  state)


(defn sketch
  "Render commit graph by using given positions.
  Optionally a width, height, update function and output-file can be given.
  Quit with 'q',
  mouse-over shows commit id,
  'p' prints current frame to given output file"
  [repo-positions & {:keys [width height update-fn output-file]
                     :or {width 768
                          height (float (/ 1024 1.618))
                          update-fn identity}}]
  (q/defsketch commit-graph
    :title "Commit graph"
    :setup (create-setup-fn repo-positions output-file width height)
    :update (fn [state] (update-fn state))
    :draw draw
    :size [width height]
    :key-typed key-handler
    :middleware [m/fun-mode]))


(comment

  (-> graph/test-repo graph/compute-positions (sketch :output-file "test.png") )

  )
