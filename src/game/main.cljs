(ns game.main)

(defn ^:export main []
  (let [socket (io/connect)]
    (doseq [event ["left:move" "right:move"]]
      (.on socket event (fn [data]
                          (.log js/console event (pr-str data)))))))
