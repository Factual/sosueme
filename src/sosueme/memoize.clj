(ns sosueme.memoize
  "Memoization utilities. Created by Myron @ factual.com"
  (:require  [clojure.core.cache :as cache]
             [clojure.core.memoize :as mem])
  (:import [clojure.core.memoize PluggableMemoization]))

(defn- valid? [cache item validator-fn]
  (and (contains? cache item) (validator-fn (get cache item))))

;; TODO: Myron's original approach:
#_(defn- get-valid [cache item not-found validator-fn]
  (if (valid? cache item validator-fn)
    (get cache item)
    not-found))

(cache/defcache ValidatingCache [cache validator-fn]
  cache/CacheProtocol
  (has? [_ item]
    (valid? cache item validator-fn))
  (hit [this item] this)
  (miss [_ item result]
    (ValidatingCache. (assoc cache item result) validator-fn))
  (evict [_ key]
    (ValidatingCache. (dissoc cache key) validator-fn))
  (lookup [_ item]

          ;;TODO: This change fixes NPE on always-invalid, but Myron not happy:
          ;;"Not too happy with this solution since it kind of depends on the
          ;; implementation, but it works for now."
          ;;(get-valid cache item nil validator-fn)
          (get cache item))
  (seed [_ base]
    (ValidatingCache. base validator-fn))
  Object
  (toString [_] (str cache)))

(defn- dereferenced-args-fn-builder [f]
  (fn [& args]
    (apply f (map deref args))))

(defn memo
  "Like core's memoize except it allows you to pass in a validating function
   which will kick the cached item out if (validator cached-item) returns false."
  [f validator]
  (mem/build-memoizer
    #(PluggableMemoization. %1 (ValidatingCache. {} %2))
    f
    (dereferenced-args-fn-builder validator)))
