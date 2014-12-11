(ns cljs.demo
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(.log js/console "hi cljs")

(def app-state (atom {:list ["Lion" "Zebra" "Buffalo" "Antelope" "Other"]}))

(defn stripe [text bgc]
  (let [st #js {:backgroundColor bgc}]
    (dom/li #js {:style st} text)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; (om/root                                                      ;;
;;  (fn [app owner]                                               ;;
;;    (om/component                                              ;;
;;     (apply dom/ul #js {:className "animals"}                  ;;
;;            (map stripe (:list app) (cycle ["#ff0" "fff"]))))) ;;
;;  app-state                                                    ;;
;;  {:target (. js/document (getElementById "demo"))})           ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def app-test (atom {:users ["user1"]}))

(def app-state
  (atom
   {:users
    [{:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
     {:first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"}
     {:first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
     {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
     {:first "Cy" :middle-initial "D" :last "Effect" :email "bugs@mit.edu"}
     {:first "Lem" :middle-initial "E" :last "Tweakit" :email "morebugs@mit.edu"}]}))

(defn middle-name [{:keys [middle middle-initial]}]
  (cond
    middle (str " " middle)
    middle-initial (str " " middle-initial ".")))

(defn display-name [{:keys [first last] :as contact}]
  (str last ", " first (middle-name contact)))

(defn display-user [{:keys [first]}]
  (str "->" first))




                                        ;om/build-all [fn [sequence "data]]

(defn contact-view [contact owner]
  (reify
    om/IRender
    (render [this]
      (dom/li nil (display-name contact)))))

(defn users-view [user xx]
  (reify
    om/IRender
    (render [this]
      (dom/tr nil
              (dom/td nil (:first user))
              (dom/td nil (:last user))
              (dom/td nil (:email user))))))

(defn appfunction [data xx]
  (reify
    om/IRender
    (render [this]
      (dom/div #js {:className "div1"}
               (dom/table #js {:className "table"}
                          (apply dom/tbody nil
                                 (om/build-all users-view (:users data))))))))

(om/root appfunction app-state
         {:target (. js/document (getElementById "demo"))})

(defn add-message [x]
  (.log js/console (.-data x)))

(def socket
  (js/WebSocket. "ws://localhost:8080/ws"))

(set! (.-onmessage socket) add-message)
                                        ;(set! (.-onmessage socket)
                                        ;      (fn[x] (.log js/console (.-data x))))

(defn send-to-server[]
  (let
      [m (.trim js/$ (.val (js/$ "#message")))]
    (.send socket m)))

(.click (js/$ "#send") send-to-server)
