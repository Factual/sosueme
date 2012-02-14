(ns sosueme.test.io
  (:use [sosueme.io])
  (:use [clojure.test]))

(deftest test-slurp-cp
  (is (= (slurp-cp "somefile.txt") "This is somefile!\n")))
