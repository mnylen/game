(ns game.draw
  (:require [game.util :refer [trace create-elem elems-by-tag-name]]
            [game.canvas :as canvas])) 

(defn init []
  (let [canvas-elem (create-elem "canvas")
        body-elem   (first (elems-by-tag-name "body"))]

    (aset canvas-elem "width" 1024)
    (aset canvas-elem "height" 768)
    (.appendChild body-elem canvas-elem)
    
    (canvas/context-2d canvas-elem)))

(defn draw [ctx objects interpolation]
  (canvas/clear-rect ctx 0 0 1024 768)

  (doseq [object objects]
    (let [[width height]    (:dimensions object)
          [pos-x pos-y]     (:position object)
          [speed-x speed-y] (:speed object)]

      (canvas/fill-rect ctx (+ pos-x (* speed-x interpolation)) (+ pos-y (* speed-y interpolation)) width height))))
