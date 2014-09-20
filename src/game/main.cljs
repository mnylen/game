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

(def user-input (atom cljs.core.PersistentQueue/EMPTY))

(defn- init-controls []
  (let [socket (io/connect)]
    (doseq [event ["left:move" "right:move"]]
      (.on socket event (fn [data]
                          (swap! user-input conj [event (js->clj data :keywordize-keys true)]))))))

(def initial-world {:objects [{:type      :dragon 
                              :state      :flying
                              :position   [512 384]
                              :dimensions [30 30]
                              :speed      [0 0]}]})


(defn- dequeue! [queue]
  (loop []
    (let [q     @queue
          value (peek q)
          nq    (pop q)]
      (if (compare-and-set! queue q nq)
        value
        (recur)))))

(defn- normalize-speed [ctrl-speed]
  (/ (- ctrl-speed max-speed) max-speed))

(defn- move-player [world event-data]
  (let [[ctrl-speed-x ctrl-speed-y] [(:x event-data) (:y event-data)]
        player                      (first (:objects world))
        updated-player              (assoc player :speed [(normalize-speed ctrl-speed-x) (normalize-speed ctrl-speed-y)])
        updated-objects             (assoc (vec (:objects world)) 0 updated-player)]

    (assoc world :objects updated-objects)))

(defn- apply-controls [previous-world]
  (loop [world previous-world 
         [event data] (dequeue! user-input)]

    (condp = event
      "left:move" (recur (move-player world data) (dequeue! user-input))
      nil         world
      (recur world (dequeue! user-input))))) ; ignore event, not supported


(defn- update-world [delta world]
  (let [new-world       (apply-controls world)
        update-position (fn [delta object]
                          (let [[speed-x speed-y] (:speed object)
                                [pos-x   pos-y]   (:position object)]

                            (assoc object :position [(+ pos-x (* delta speed-x max-speed 2))
                                                     (+ pos-y (* delta speed-y max-speed 2))])))]

  (assoc new-world :objects (map (partial update-position delta) (:objects new-world)))))

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
