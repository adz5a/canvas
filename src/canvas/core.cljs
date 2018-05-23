(ns canvas.core
  (:require [canvas.shape :refer [line
                                  rotate
                                  draw!]]))

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

(defn click-handler [e]
  (let [x-e (.-clientX e)
        y-e (.-clientY e)
        [off-x off-y] (:offset @app-state)
        [x y] [(- x-e off-x)
               (- y-e off-y)]]
    (println x y)))



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

(defn vector-diff [v1 v2]
  (apply vector (map - v2 v1)))

(defn vector-add [v1  v2]
  (apply vector (map + v1 v2)))

(defn vector-rotate [[x y] a]
  (let [c (.cos js/Math a)
        s (.sin js/Math a)]
    [(+ (* c x) (* s y))
     (- (* s x) (* c y))])) 

; (draw-line! (rotate l (/ PI 3)) ctx)

(defn begin-path [ctx]
  (.beginPath ctx)
  ctx)

(defn move-to [ctx [x y]]
  (.moveTo ctx x y)
  ctx)

(defn line-to [ctx [x y]]
  (.lineTo ctx x y)
  ctx)

(defn stroke
  ([ctx]
   (.stroke ctx)
   ctx)
  ([ctx style]
   (let [prev-style (.-strokeStyle ctx)]
     (set! (.-strokeStyle ctx) style)
     (.stroke ctx)
     (set! (.-strokeStyle ctx) prev-style)
     ctx)))

(def start [50 50])
(def dir [100 0])

(doto ctx
  (begin-path)
  (move-to start)
  (line-to (vector-add start dir))
  (stroke "red")
  
  (begin-path)
  (move-to start)
  (line-to (vector-add start
                       (vector-rotate dir
                                      (/ PI 2))))
  (stroke "blue")

  (begin-path)
  (move-to start)
  (line-to (vector-add start
                       (vector-rotate dir
                                      (/ PI 4))))
  (stroke "black")

  (begin-path)
  (move-to start)
  (line-to (vector-add start
                       (vector-rotate dir
                                      (/ PI 3))))
  (stroke "green")
  
  )

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
