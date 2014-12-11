(defproject wsclj "0.1.0-SNAPSHOT"
  :description "bla bla"
  :url "http://saiberz.me"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2411"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [om "0.7.3"]
                 [http-kit "2.1.19"]
                 [ring "1.3.2"]
                 [compojure "1.3.0"]
                 [stylefruits/gniazdo "0.3.0"] ; client
                 [ring-cors "0.1.4"]]
  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :cljsbuild {
              :builds [{
                        :id "test"
                        :source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/js/demo.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}

  :main ^:skip-aot wsclj.core
  :profiles {:uberjar {:aot :all}})
