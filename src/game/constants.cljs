(ns game.constants)

(def ticks-per-second 30)                  ; target fps
(def skip-ticks (/ 1000 ticks-per-second)) ; not sure what means?
(def max-frames-skipped 10)                ; how many frames to skip before forcing display?
(def max-speed 128)

