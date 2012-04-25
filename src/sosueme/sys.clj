(ns sosueme.sys
  (:import [java.lang.management ManagementFactory]))

(defn pid
  "Returns the current process's PID, as a String"
  []
  (or
   (System/getProperty "pid")
   (first
     (->
      (.getName (ManagementFactory/getRuntimeMXBean))
      (.split "@")))))
