(ns canvas.life)

; rules
; Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
; Any live cell with two or three live neighbors lives on to the next generation.
; Any live cell with fewer than two live neighbors dies, as if caused by underpopulation.
; Any live cell with more than three live neighbors dies, as if by overpopulation.

(defonce size (range 100))
(defonce coords (for [x size
                  y size]
              [x y]))

; initial proba for a cell to be alive
(defonce p 0.3)

(defonce state (reduce (fn [state coord]
                         (assoc state coord (if (< p (rand))
                                              1
                                              0)))
                       {}
                       coords))

(defonce offsets (disj
               (set (for [x [0 1 -1]
                          y [0 1 -1]]
                      [x y]))
               [0 0]))

(defn add [c1 c2]
  (apply vector (map + c1 c2)))

(defn get-neighbors [coord]
  (map (partial add coord) offsets))

(defn get-life-count [coord]
  (let [neighbors (get-neighbors coord)]
    (reduce #(let [s (get state %2)]
               (+ %1 s))
            0
            neighbors)))

(defn next-cell-state [coord]
  (let [current (get state coord)
        life-count (get-life-count (get-neighbors coord))]
    (cond
      (= life-count 3) 1
      
      (and
        (= 1 current)
        (#{2 3} life-count)) 1
      
      (and
        (= 1 current)
        (< life-count 2)) 0
      
      (> 3 life-count) 0)))

(doall (for [[coords state] state]
         (println coords state)))
