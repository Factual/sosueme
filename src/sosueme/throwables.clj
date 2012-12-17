(ns sosueme.throwables)

(defn stack-trace-str
  "Returns, as a String, the formatted stack trace from throwable."
  [throwable]
  (let [swrt (java.io.StringWriter.)]
    (.printStackTrace throwable (java.io.PrintWriter. swrt))
    (str swrt)))