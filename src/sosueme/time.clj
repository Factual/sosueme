(ns sosueme.time)

(defn now []
 "Returns the unix time for now."
 (System/currentTimeMillis))

(defn minutes-ago
  "Returns the unix time for m minutes ago."
  [m]
  (- (now) (* m 60 1000)))
