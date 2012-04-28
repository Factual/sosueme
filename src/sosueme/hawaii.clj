(ns sosueme.hawaii)

(def C "hwknlpmn")
(def V [\a \e \i \o \u "ai" "ei"])

(defn newname
  "Returns a randomly generated String that should be roughly human pronounceable.
   Follows a convention meant to simulate Hawaiian sounding names.

   Example names:
     nainini
     paiwaipei
     nikiwai
     nawilu
     winenu
     pinaku
     leinai
     honaimai
     heimaiho
     munupei
     wainei
     peikani"
  []
  (apply str (repeatedly (rand-nth [2 3]) #(str (rand-nth C) (rand-nth V)))))
