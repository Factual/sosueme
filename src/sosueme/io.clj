(ns sosueme.io
  (require [clojure.java.io :as jio]
       [fs.core :as fs]))

(defn slurp-cp
  "Slurps the contents of f from the classpath. f can be a file path as a String,
   or an actual File. It must exist on the classpath, otherwise nil will be returned."
  [f]
  (let [loader (java.net.URLClassLoader. (into-array [(jio/as-url (fs/file f))]))]
    (when-let [res (jio/resource f loader)]
      (slurp res))))