(ns game.main
  (:require [game.util :refer [log trace ]]
            [game.draw :as renderer]
            [game.constants :refer [ticks-per-second skip-ticks max-frames-skipped max-speed]]))

(enable-console-print!)

(defn- millis-since-epoch []
  (.getTime (js/Date.)))

(def start-time (millis-since-epoch))

(defn- tick-count []
  (- (millis-since-epoch) start-time))

(defn- init-controls []
  (let [socket (io/connect)]
    (doseq [event ["left:move" "right:move"]]
      (.on socket event (fn [data]
                          (.log js/console event (pr-str data)))))))

(def initial-world {:objects [{:type       :dragon 
                              :state      :flying
                              :position   [512 384]
                              :dimensions [30 30]
                              :speed      [1 0]}]})

(defn- update-world [delta world]
  (let [update-position (fn [delta object]
                          (let [[speed-x speed-y] (:speed object)
                                [pos-x   pos-y]   (:position object)]

                            (assoc object :position [(+ pos-x (* delta speed-x max-speed))
                                                     (+ pos-y (* delta speed-y max-speed))])))]

  (assoc world :objects (map (partial update-position delta) (:objects world)))))

(defn- update-loop [next-tick world frames-skipped]
  (if (and (> (tick-count) next-tick) (< frames-skipped max-frames-skipped))
    (recur (+ next-tick skip-ticks) (update-world (/ skip-ticks 1000) world) (inc frames-skipped))
    [next-tick world]))

(defn- game-loop [canvas next-tick world]
  (let [[next-tick updated-world] (update-loop next-tick world 0)
        interpolation (/ (- (+ (tick-count) skip-ticks) next-tick) skip-ticks)]
    (renderer/draw canvas (:objects world) interpolation) 
    (.setTimeout js/window #(game-loop canvas next-tick updated-world) 1)))

(defn ^:export main []
  (init-controls)
  (game-loop (renderer/init) (tick-count) initial-world))
