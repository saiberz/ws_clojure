(ns cljs.demo
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)
(def app-data
  (atom
   {:users
    [{:name "Ben" :country "Bangladesh" :email "benb@mit.edu"}
     {:name "Alyssa" :country "Bulgary" :email "aphacker@mit.edu"}
     {:name "Eva" :country "Germany" :email "eval@mit.edu"}
     {:name "Louis" :country "Brazil" :email "prolog@mit.edu"}
     {:name "Cy" :country  "China" :email "bugs@mit.edu"}
     {:name "Lem" :country "Switzerland" :email "morebugs@mit.edu"}]}))

(defn users-view [user xx]
  (reify
    om/IRender
    (render [this]
      (dom/tr nil
              (dom/td nil (:name user))
              (dom/td nil (:country user))
              (dom/td nil (:email user))))))

(defn appfunction [data xx]
  (reify
    om/IRender
    (render [this]
      (dom/div #js {:className "div1"}
               (dom/table #js {:className "table"}
                          (apply dom/tbody nil
                                 (om/build-all users-view (:users data))))))))

(om/root appfunction
         app-data
         {:target (. js/document (getElementById "demo"))})

(defn add-message [x]
  (.log js/console (.-data x)))

(def socket
  (js/WebSocket. "ws://localhost:8080/ws"))

(set! (.-onopen socket)
      (fn [x]
        (.send socket "new")))

(set! (.-onmessage socket) add-message)

(defn send-to-server[]
  (let
      [m (.trim js/$ (.val (js/$ "#message")))]
    (.send socket m)))

(.click (js/$ "#send") send-to-server)
