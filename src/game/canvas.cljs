(ns game.canvas)

(defn context-2d [dom]
  (.getContext dom "2d"))

(defn fill-style [ctx style]
  (aset ctx "fillStyle" style))

(defn fill-rect [ctx x y width height]
  (.fillRect ctx x y width height))

(defn clear-rect [ctx x y width height]
  (.clearRect ctx x y width height))

(defn draw-image
  ([ctx img x y width height]
   (.drawImage ctx img x y width height))
  ([ctx img sx sy swidth sheight x y width height]
   (.drawImage ctx img sx sy swidth sheight x y width height)))
