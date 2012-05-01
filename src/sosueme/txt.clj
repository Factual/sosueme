(ns sosueme.txt
  (:import [java.io StringWriter])
  (:require [clojure.pprint :as pprint]))

(defn pretty
  "Uses Clojure's pprint to return a prettily formatted String representation of o,
   including line breaks for readability."
  [o]
  (let [w (StringWriter.)] (pprint/pprint o w)(.toString w)))
  




