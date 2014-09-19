(ns game.main
  (:require [game.util :refer [log trace ]]
            [game.draw :as renderer]))

(enable-console-print!)

(def max-speed 128)     ; max speed in "units"
(def pixels-per-unit 1) ; how many pixels there are per "unit"
(def target-fps 30)    ; target fps

(defn- init-controls []
  (let [socket (io/connect)]
    (doseq [event ["left:move" "right:move"]]
      (.on socket event (fn [data]
                          (.log js/console event (pr-str data)))))))


(def initial-state {:objects [{:type       :dragon 
                               :state      :flying
                               :position   [512 384]
                               :dimensions [30 30]
                               :speed      [max-speed 0]}]})

(defn- move-object [ms-delta {:keys [position speed] :as object}]
  (let [[speed-x speed-y] speed
        [pos-x   pos-y]   position]
    (assoc object :position [(+ pos-x (/ (* (/ ms-delta 1000) speed-x) pixels-per-unit))
                             (+ pos-y (/ (* (/ ms-delta 1000) speed-y) pixels-per-unit))])))

(defn- update [ms-delta objects]
  (->> objects
       (map (partial move-object ms-delta))))

(defn- tick [ms-delta {:keys [canvas objects] :as state}]
  (let [new-state (assoc state :objects (update ms-delta objects))]
    (renderer/draw canvas (:objects new-state))
    new-state))

(defn- game-loop [state]
  (.setTimeout js/window (fn []
                           (game-loop (tick (/ 1000 target-fps) state))) (/ 1000 target-fps)))

(defn ^:export main []
  (init-controls)

  (let [canvas (renderer/init)]
    (game-loop (assoc initial-state :canvas canvas))))
