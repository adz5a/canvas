(ns canvas.core
  (:require [canvas.operations 
             :as op
             :refer [add
                     diff
                     rotate]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {}))

(defonce canvas (.getElementById js/document "canvas"))
(defonce output (.getElementById js/document "output"))

(defonce ctx (.getContext canvas "2d"))

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

(defn set-output! [text]
  (set! (.-textContent output) text))

(defn click-handler [e]
  (let [x-e (.-clientX e)
        y-e (.-clientY e)
        [off-x off-y] (:offset @app-state)
        [x y] [(- x-e off-x)
               (- y-e off-y)]]
    (set-output! (str x "-" y))
    (doto ctx
      (op/begin-path)
      (op/move-to [250 250])
      (op/line-to [x y])
      (op/stroke))))



(defonce on-click (fn [event]
                    (click-handler event)))

(.addEventListener js/window "resize" on-resize)
(.addEventListener canvas "click" on-click)

(defn clear! [ctx]
  (let [w (aget ctx "canvas" "width")
        h (aget ctx "canvas" "height")]
    (.clearRect ctx 0 0 w h)))



(def PI (.-PI js/Math))


(def PI|3 (/ PI 3))


; (draw-line! (rotate l (/ PI 3)) ctx)



(def start [50 50])
(def dir [100 0])

(doto ctx
  (op/begin-path)
  (op/move-to start)
  (op/line-to (add start dir))
  (op/stroke "red")
  
  (op/begin-path)
  (op/move-to start)
  (op/line-to (add start
                       (rotate dir
                                      (/ PI 2))))
  (op/stroke "blue")

  (op/begin-path)
  (op/move-to start)
  (op/line-to (add start
                       (rotate dir
                                      (/ PI 4))))
  (op/stroke "black")

  (op/begin-path)
  (op/move-to start)
  (op/line-to (add start
                       (rotate dir
                                      (/ PI 3))))
  (op/stroke "green")
  
  )

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
