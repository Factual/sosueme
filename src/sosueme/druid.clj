(ns druid
  (require [sosueme.hawaii :as hi]
           [sosueme.net :as net]
           [clj-time.core :as t]
           [clj-time.format :as tf]))

(def TIME-FORMAT (tf/formatter "yyMMdd-HHmmss"))
(def MILL-FORMAT (tf/formatter "SSS"))

;;TODO: research the worst case of no good local hostname
(defonce ME
  (->
   (net/local-hostname)
   (.substring 0 10)
   (.toLowerCase)))

(defn druid []
  (let [now (t/now)]
    (str
     ME "-"
     (tf/unparse TIME-FORMAT now) "-"
     (hi/newname) "-"
     (tf/unparse MILL-FORMAT now))))
