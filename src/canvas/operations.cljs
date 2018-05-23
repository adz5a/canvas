(ns canvas.operations)

(defn diff [v1 v2]
  (apply vector (map - v2 v1)))

(defn add [v1  v2]
  (apply vector (map + v1 v2)))

(defn rotate [[x y] a]
  (let [c (.cos js/Math a)
        s (.sin js/Math a)]
    [(+ (* c x) (* s y))
     (- (* s x) (* c y))])) 

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
