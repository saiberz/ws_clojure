(defproject wsclj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [http-kit "2.1.19"]
                 [ring "1.3.2"]
                 [compojure "1.3.0"]
                 [stylefruits/gniazdo "0.3.0"] ; client
                 [ring-cors "0.1.4"]]
  :main ^:skip-aot wsclj.core
  :profiles {:uberjar {:aot :all}})
