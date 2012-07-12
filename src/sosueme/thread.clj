(ns sosueme.thread)

(defmacro with-thread-name
  "Changes the current Thread name to name, runs body, then reverts the
   current Thread name back to what it was originally."
  [name & body]
  `(let [thread# (Thread/currentThread)
        old-name# (.getName thread#)]
    (.setName thread# ~name)
    (let [ret# (do ~@body)]
       (.setName thread# old-name#)
       ret#)))