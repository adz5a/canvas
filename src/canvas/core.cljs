(ns canvas.core
    (:require [reagent.core :as reagent :refer [atom]]
              [canvas.life :as life]))

(enable-console-print!)

(def height 500)
(def width 500)

(defonce app-state (atom {:text "Hello world!"
                          :!ref nil}))

(def canvas (reagent/create-class {:component-did-mount (fn []
                                                          (swap! app-state assoc :ctx (.getContext (:!ref @app-state)
                                                                                                      "2d")))
                             :reagent-render (fn []
                                               [:canvas 
                                                {:ref (partial swap! app-state assoc :!ref)
                                                 :width width
                                                 :height height
                                                 :style {:border "black solid 1px"
                                                         }}])}))

(def origin [0 0])

(defn set-transform! [ctx X Y D]
  (let [[a b c d e f] (concat X Y D)] 
    (.setTransform ctx a b c d e f))
  ctx)

(defn line-to! 
  ([ctx [x y]]
   (line-to! ctx origin [x y]))
  ([ctx [x1 y1] [x2 y2]]
   (doto ctx
     (.beginPath)
     (.moveTo x1 y1)
     (.lineTo x2 y2)
     (.stroke))
   ctx))

(defn stroke! [ctx]
  (.stroke ctx)
  ctx)

(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:h3 "Edit this and watch it change!"]
   [canvas]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

; repl utils
(defn ctx! []
  (:ctx @app-state))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
