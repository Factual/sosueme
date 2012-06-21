(ns sosueme.druid
  (require [sosueme.hawaii :as hi]
           [sosueme.net :as net])
  (use [clj-time.core :only [default-time-zone]]
       [clj-time.format :only [formatter unparse]]
       [clj-time.local :only [local-now]]))

(def TIME-FORMAT (formatter "yyMMdd-HHmmss" (default-time-zone)))
(def MILL-FORMAT (formatter "SSS" (default-time-zone)))

;;TODO: research the worst case of no good local hostname
(defonce ME
  (->
   (net/local-hostname)
   (.substring 0 10)
   (.toLowerCase)))

(defn druid
  "Returns a Darned Relevant Unique Identifier (DRUID). The DRUID will
   start with the first 10 characters of the localhost name (or prefix if
   supplied), followed by the date and time in yyMMdd-HHmmss format,
   followed by a randomly generated Hawaiian-sounding name, followed by
   the milliseconds of the current time.

   An example DRUID:
     aaron-crow-120621-025001-lumawi-039"
  ([prefix]
   (let [now (local-now)]
     (str
      prefix "-"
      (unparse TIME-FORMAT now) "-"
      (hi/newname) "-"
      (unparse MILL-FORMAT now))))
  ([]
     (druid ME)))
