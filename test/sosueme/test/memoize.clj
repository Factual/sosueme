(ns sosueme.test.memoize
  (:use
   [clojure.test]
   [sosueme.memoize]))

(def ALWAYS-VALID (constantly true))

(def NEVER-VALID (constantly false))

(def call-count (atom 0))

(def INC-FN
  (fn [x] (swap! call-count inc)
          (+ x 8)))


(deftest test-memo-basecase
  "Verify the memozied function works"
  (let [memod (memo
               (fn [x] (* x 2))
               ALWAYS-VALID)]
    (is (= 4 (memod 2)))
    (is (= 8 (memod 4)))))

(deftest test-memo-always-valid
  (reset! call-count 0)
  (let [memod (memo
               INC-FN
               ALWAYS-VALID)]
    (memod 3)
    (memod 3)
    ;; INC-FN was memo'd with an always-true validator and called twice.
    ;; Memoization should have prevented the 2nd call and used cache.
    (is (= 1 @call-count))))

(deftest test-memo-never-valid
  (reset! call-count 0)
  (let [memod (memo
               INC-FN
               NEVER-VALID)]
    (memod 3)
    (memod 3)
    ;; INC-FN was memo'd with a never-true validator and called twice.
    ;; Memoization should never have worked, therefore 2 real calls.
    (is (= 2 @call-count))))
