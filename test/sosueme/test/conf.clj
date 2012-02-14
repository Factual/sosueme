(ns sosueme.test.conf
  (:require [sosueme.conf :as conf])
  (:use [clojure.test]))

(deftest test-basic-usage
  (conf/load-file! "someconf.clj")
  (is (= {:key1 "val1" :key2 "val2" :key3 "val3"} (conf/all)))
  (is (= "val2" (conf/get :key2))))
