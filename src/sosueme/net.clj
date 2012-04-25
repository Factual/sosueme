(ns sosueme.net
  (:import [java.net InetAddress]))

(defn local-hostname []
  (.getHostName (InetAddress/getLocalHost)))