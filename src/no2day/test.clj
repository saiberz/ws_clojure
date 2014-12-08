(ns no2day.core
  (:use [compojure.core :only (defroutes GET)]
        ring.util.response
        ring.middleware.cors
        org.httpkit.server)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [cheshire.core :refer :all]
            [ring.middleware.reload :as reload]))

(use 'alembic.still)
                                        ;(load-project)

(def users (atom {}))


(defn handler [req]
  (with-channel req channel
    (swap! users assoc channel true)
    (on-close channel (fn [status]
                        (swap! users assoc channel false)
                        (println "channel closed")))
    (on-receive channel (fn [data]       ; data received from client
                          (println "data received!")))))

(defonce server (atom nil))
(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(def clients (atom {}))

(defn ws
  [req]
  (with-channel req con
    (swap! clients assoc con true)
    (println con " connected")
    (on-receive con (fn [data]
                      (doseq [client @clients]
                        (send! (key client) "sup dudes im writing" false))
                      (println "data received!")))
    (on-close con (fn [status]
                    (swap! clients dissoc con)
                    (println con "disconnected. status: " status)))))

(doseq [client @clients]
            (send! (key client) (generate-string
                                 {:happiness (rand 10)}) false))

(defroutes rutas
  (GET "/ws" [] ws)
  (GET "/chat" [] "ws"))

(stop-server)

(reset! server ( run-server (-> (handler/site rutas)
                               reload/wrap-reload
                               (wrap-cors
                                :access-control-allow-origin #".+")) {:port 7003 :join? false}))
