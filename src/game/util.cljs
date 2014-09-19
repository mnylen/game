(ns game.util)

(defn trace [x]
  (println x)
  x)

(defn create-elem [tag]
  (.createElement js/document tag))

(defn elems-by-tag-name [tag-name]
  (array-seq (.getElementsByTagName js/document (name tag-name))))
