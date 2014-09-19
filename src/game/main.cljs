(ns game.main
  (:require [game.util :refer [log trace ]]
            [game.draw :as renderer]))

(enable-console-print!)

(defn- init-controls []
  (let [socket (io/connect)]
    (doseq [event ["left:move" "right:move"]]
      (.on socket event (fn [data]
                          (.log js/console event (pr-str data)))))))


(def initial-state {:objects [{:type       :dragon 
                               :state      :flying
                               :position   {:x 512 :y 384}
                               :dimensions {:width 30 :height 30}
                               :speed      {:x 0 :y 0}}]})
(defn ^:export main []
  (init-controls)

  (let [canvas (renderer/init)]
    (renderer/draw canvas (:objects initial-state))))
