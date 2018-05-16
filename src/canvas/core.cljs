(ns canvas.core
  (:require ))

(enable-console-print!)

(println "This text is printed from src/canvas/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {}))

(defonce canvas (.createElement js/document "canvas"))

(defonce ctx (.getContext canvas "2d"))

(defonce insert-canvas (.appendChild (.-body js/document) canvas))

(set! (-> canvas
          (.-style)
          (.-border))
      "solid 1px red")

(set! (-> canvas
          (.-width))
      "500")

(set! (-> canvas
          (.-height))
      "500")

(def offsets (.getBoundingClientRect canvas))

(swap! app-state assoc 
       :offset [(.-x offsets)
                (.-y offsets)])

(defonce on-resize (fn []
                     (let [offsets (.getBoundingClientRect canvas)]
                       (swap! app-state assoc 
                              :offset [(.-x offsets)
                                       (.-y offsets)]))))

(defn click-handler [e]
  (let [x-e (.-clientX e)
        y-e (.-clientY e)
        [off-x off-y] (:offset @app-state)
        [x y] [(- x-e off-x)
               (- y-e off-y)]]
    (println x y)))

(defn rotate [[x y] a]
  (let [c (.cos js/Math a)
        s (.sin js/Math a)]
    [(+ (* c x) (* s y))
     (- (* s x) (* c y))]))

(defonce on-click (fn [event]
                    (click-handler event)))

(.addEventListener js/window "resize" on-resize)
(.addEventListener canvas "click" on-click)

(defn clear! [ctx]
  (let [w (aget ctx "canvas" "width")
        h (aget ctx "canvas" "height")]
    (.clearRect ctx 0 0 w h)))


(defn draw-line! 
  [line ctx]
  (let [[x y] line]
    (doto ctx
      (.beginPath)
      (.moveTo 0 0)
      (.lineTo x y)
      (.stroke))))

(def PI (.-PI js/Math))

(clear! ctx)
(def l [100 0])
(println (rotate l (/ PI 3)))
(draw-line! (rotate l (/ PI 3)) ctx)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)

  (println @app-state)
  )
