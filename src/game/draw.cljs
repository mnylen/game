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

(defn draw [ctx objects]
  (doseq [{{:keys [width height]} :dimensions
           {:keys [x y]}          :position} objects]

    (canvas/fill-rect ctx x y width height)))
