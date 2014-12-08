(ns no2day.core
  (:use [compojure.core :only [defroutes GET]]
         ring.middleware.cors)
  (:require [org.httpkit.server :as http-kit]
            [ring.middleware.reload :as reload]
            [ring.util.response :refer [redirect]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [clojure.tools.logging :as log]
            [clj-wamp.server :as wamp]))

;(use 'alembic.still)
;(load-project)

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(def clients (atom {}))

(defn ws
  [req]
  (http-kit/with-channel req con
    (swap! clients assoc con {:name "guest"})
    (http-kit/on-receive con (fn [data]
                               (println "Data received: " )
                               (doseq [client @clients]
                                 (http-kit/send! (key client) (str data) false))))
    (http-kit/on-close con (fn [status]
                             (doseq [client @clients]
                               (http-kit/send! (key client) (str "Server closed:" con) false))
                             (swap! clients dissoc con)
                             (println con " disconnected. status: " status)))))

(defn change-name [con name]
 "Change user name"
 (swap! clients assoc con {:name name}))

(defn remove-user []
  (println "Usser Disconnected"))

(defroutes routes
  (GET "/ws" [] ws)
  (GET "/" [] (redirect "/index.html"))
  (route/resources "/"))

(def app (-> (handler/site routes)
            reload/wrap-reload))

(defn -main [& args]
  (stop-server)
 (reset! server (http-kit/run-server app {:port 8090 :join? false}))
)

;(doseq [client @clients]
;    (http-kit/send! (key client) "ripusca" false))

(-main)
