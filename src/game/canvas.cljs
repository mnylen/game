(ns game.canvas)

(defn context-2d [dom]
  (.getContext dom "2d"))

(defn fill-style [ctx style]
  (aset ctx "fillStyle" style))

(defn fill-rect [ctx x y width height]
  (.fillRect ctx x y width height))

(defn clear-rect [ctx x y width height]
  (.clearRect ctx x y width height))
