(ns game.assets)

(defn- load-image [src]
  (let [img (js/Image.)] 
    (aset img "loaded" false)
    (aset img "onload" #(aset img "loaded" true)) 
    (aset img "src" src)
    img))

(def background (load-image "/resources/images/space-background.jpg"))

(def all-assets [background])

(defn load-assets [callback]
  (let [loaded?     #(aget % "loaded")
        all-loaded? (every? loaded? all-assets)]

    (if all-loaded?
      (callback)
      (.setTimeout js/window #(load-assets callback) 100))))
