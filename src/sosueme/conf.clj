(ns sosueme.conf
  "Simple configuration utility. Knows how to read in configuration from
   files and the classpath. Provides access to said configuration.

   Configuration data is expected to be stored as a clojure hash-map.
   E.g., if you have a file called myconf.clj with this content:

     {:host \"my.server.com\"
      :port 8081}

   Then your app could do this:
   ...
   (conf/load-file! \"myconf.clj\")
   (let [host (conf/get :host)] ... )"

  (:use [sosueme.io :as sio])
  (:use [fs.core :only (exists?)])
  (:use [clojure.tools.logging :only (info debug error)]))
  
(defn load-when 
  "If path exists in the present working directory or on the classpath,
   returns the data structure represented in the file at path. Otherwise
   returns nil."
  [path]
  (if (exists? path)
    (do
      (info "conf.load-when: loading conf from file at" path)
      (read-string (slurp path)))
    (if-let [data-str (sio/slurp-cp path)]
      (do
        (info "conf.load-when: loading conf from classpath at" path)
        (read-string data-str))
      (info "conf.load-when: no file or resource at" path "; skipping"))))

(def ^:dynamic *conf* nil)

(defn load-file!
  "Merges the configuration in path into current *conf*. In the case of
   identical keys, the configuration in path will take precedence."
  [path]
  (def ^:dynamic *conf* (merge *conf* (load-when path))))

(defn get
  "Returns the value at key k in the configuration that has been loaded."
  [k]
  (*conf* k))

(defn all
  "Returns the full configuration hashmap that has been loaded."
  [] *conf*)

(defn show 
  "Convenience fn to print conf to stdout"
  []
  (info "conf.show: conf:" *conf*))