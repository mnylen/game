(ns game.assets)

(defn- load-image [src]
  (let [img (js/Image.)] 
    (aset img "loaded" false)
    (aset img "onload" #(aset img "loaded" true)) 
    (aset img "src" src)
    img))

(def background (load-image "/resources/images/space-background.jpg"))

(def dragon (load-image "/resources/images/dragon.gif"))

(defn dragon-frame [animation frame]
  (println animation frame)
  (let [row (condp = animation
              :flying 0
              :flying-down 1
              :flying-up 7)]

    [(* frame 75) (* row 70) 75 70]))

(def all-assets [background dragon])

(defn load-assets [callback]
  (let [loaded?     #(aget % "loaded")
        all-loaded? (every? loaded? all-assets)]

    (if all-loaded?
      (callback)
      (.setTimeout js/window #(load-assets callback) 100))))
