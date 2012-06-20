(ns sosueme.hawaii)

(def C "hwknlpm")
(def V [\a \e \i \o \u])

(defn newname
  "Returns a randomly generated String that should be roughly human pronounceable.
   Follows a convention meant to simulate Hawaiian sounding names.

   Example names:
     monime
     nuhine
     kamino"
  []
  (apply str (repeatedly 3 #(str (rand-nth C) (rand-nth V)))))
