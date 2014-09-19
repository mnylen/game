(defproject game "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2341"]]
  :plugins [[lein-cljsbuild "1.0.3"]] 
  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:output-to "public/resources/javascript/main.js"
                                   :externs ["lib/socket.io-externs.js"]
                                   :optimizations :whitespace
                                   :pretty-print true}}]})
