(ns game.draw
  (:require [game.util :refer [trace create-elem elems-by-tag-name]]
            [game.canvas :as canvas]
            [game.assets :as assets]))

(def width 1024)
(def height 768)

(defn init []
  (let [canvas-elem (create-elem "canvas")
        body-elem   (first (elems-by-tag-name "body"))]

    (aset canvas-elem "width" width)
    (aset canvas-elem "height" height)
    (.appendChild body-elem canvas-elem)
    
    (canvas/context-2d canvas-elem)))

(defn- draw-dragon [ctx pos-x pos-y animation anim-frame]
  (let [[sprite-x sprite-y sprite-width sprite-height] (assets/dragon-frame animation anim-frame)]
    (canvas/draw-image ctx assets/dragon sprite-x sprite-y sprite-width sprite-height pos-x pos-y sprite-width sprite-height)))

(defn draw [ctx objects interpolation]
  (canvas/clear-rect ctx 0 0 width height)
  (canvas/draw-image ctx assets/background 0 0 width height) 

  (doseq [object objects]
    (let [[width height]    (:dimensions object)
          [pos-x pos-y]     (:position object)
          [speed-x speed-y] (:speed object)
          [ipos-x ipos-y]   [(+ pos-x (* speed-x interpolation)) (+ pos-y (* speed-y interpolation))]]

      (condp = (:type object)
        :dragon (draw-dragon ctx ipos-x ipos-y (:animation object) (:anim-frame object))))))
