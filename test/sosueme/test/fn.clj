(ns sosueme.test.fn
  (:use [sosueme.fn] :reload)
  (:use [clojure.test]))

;; Tests for defnk
(defnk foo [] 5)
(defnk foo-doc "a docstring" [] 5)
(defnk foo-arg [x] (inc x))
(defnk foo-doc-arg "a docstring" [x] (inc x))
(defnk foo-arg-doc [x "the number"] (inc x))
(defnk foo-doc-arg-doc "a docstring" [x "the number"] (inc x))

(defnk foo-key [:x 10] (inc x))
(defnk foo-doc-key "a docstring" [:x 10] (inc x))
(defnk foo-key-doc [:x 10 "the number"] (inc x))
(defnk foo-doc-key-doc "a docstring" [:x 10 "the number"] (inc x))

(defnk foo-arg-key [x :y 5] (+ x y))
(defnk foo-doc-arg-key "a docstring" [x :y 5] (+ x y))
(defnk foo-arg-doc-key [x "x doc" :y 5] (+ x y))
(defnk foo-arg-key-doc [x :y 5 "y doc"] (+ x y))
(defnk foo-arg-doc-key-doc [x "x doc" :y 5 "y doc"] (+ x y))
(defnk foo-doc-arg-doc-key-doc "a docstring" [x "x doc" :y 5 "y doc"] (+ x y))

(defn should-increment [f] (are [x y] (= (f x) y)
                                10 11
                                5  6))

(defn should-increment-keyword [f] (should-increment #(f :x %)))

(defn should-be-documented [v]         (is (re-find #"a docstring" (:doc (meta v)))))
(defn should-have-arglist  [v arglist] (is (= arglist (first (:arglist (meta v))))))

(defmacro each [f & xs] `(doseq [x# ~(vec xs)] (~f x#)))

(deftest test-defnk-functionality
  (are [x y] (= x y)
       5 (foo)
       5 (foo-doc))

  (each should-be-documented #'foo-doc #'foo-doc-arg #'foo-doc-arg-doc)
  (each should-be-documented #'foo-doc-key     #'foo-doc-key-doc
                             #'foo-doc-arg-key #'foo-doc-arg-doc-key-doc)

  (each should-increment         foo-arg foo-doc-arg foo-arg-doc foo-doc-arg-doc)
  (each should-increment-keyword foo-key foo-doc-key foo-key-doc foo-doc-key-doc))

(defnk wootwoot [:x 10] x)
(defnk wootwoot-proxy [:x defnk-default] (wootwoot :x x))

(defnk* wootwoot2 [:x 10] x)
(defnk* wootwoot2-proxy [:x nil] (wootwoot2 :x x))

(deftest test-defnk-defaults
  (is (= (wootwoot :x 5) 5))
  (is (= (wootwoot) 10))
  (is (= (wootwoot-proxy :x 5) 5))
  (is (= (wootwoot-proxy) 10)))

(deftest test-defnk*-defaults
  (is (= (wootwoot2 :x 5) 5))
  (is (= (wootwoot2) 10))
  (is (= (wootwoot2-proxy :x 5) 5))
  (is (= (wootwoot2-proxy) 10)))