(defn appN
  ([n f] (fn [p1 p2] (cond
                       (= n 1) (mapv (fn [r1 r2] (f r1 r2)) p1 p2)
                       (> n 1) (mapv (appN (dec n) f) p1 p2))))
  ([f] (fn [p1 p2] (mapv f p1 p2))))

(defn app [f] (fn [p1 p2] (mapv f p1 p2)))

(def v+ (appN +))
(def v- (appN -))
(def v* (appN *))
(def vd (appN /))

(defn v*s [v, s]
  (mapv #(* % s) v))

(defn scalar [a, b]
  (reduce + (mapv * a b)))

(defn vect [v1, v2]
  (vector ( - (* (nth v1 1) (nth v2 2)) (* (nth v1 2) (nth v2 1)))
          ( - (* (nth v1 2) (nth v2 0)) (* (nth v1 0) (nth v2 2)))
          ( - (* (nth v1 0) (nth v2 1)) (* (nth v1 1) (nth v2 0)))))


(def m+ (appN v+))
(def m- (appN v-))
(def m* (appN v* ))
(def md (appN vd))

(defn app1 [f]
  (fn [pol pol-] (mapv (fn [r] (f r pol-)) pol)))

(def m*s (app1 v*s))
(def m*v (app1 scalar))

(defn transpose [matrix]
  (apply mapv vector matrix))

(defn m*m [m1 m2]
  (transpose (mapv (fn [r] (m*v m1 r)) (transpose m2))))

(def c4+ (appN (appN m+)))
(def c4- (appN (appN m-)))
(def c4* (appN (appN m*)))
(def c4d (appN (appN md)))

;(def c4* (appN 2 m*))
;(def c4d (appN 2 md))


