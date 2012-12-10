(ns sosueme.fn
  "Function utilities."
  (:use [clojure.string :only [join]]
        [clojure.set :only [difference]]))

(def defnk-default ::defnk-default)

(defmacro defnk
  "The macro that jackknife.def/defnk always should have been. The only real difference is that we
  generate a proper {:keys :or} map instead of doing varargs processing. This improves the
  documentation for people who are calling your function.

  A few notes:

  1. All keyword args must be at the end; you can't intersperse positional and keyword parameters.
  2. Any keyword argument that has no corresponding formal causes an error. (This is unlike both
     jackknife.def/defnk and Clojure's core defn.)
  3. Keyword arguments can contain an optional docstring after the default value. If you do this,
     that docstring is appended to the function's docstring in a standard way. Positional arguments
     can be documented similarly, but the docstring follows the name since there is no value.
  4. You can use the default value for a keyword argument by passing the value defnk-default. This
     allows you to chain defnk invocations and write the default values only once.

  For example:

  > (defnk foo
      \"Does something cool\"
      [xs \"the collection of things to process\"
       ys margin
       :maximum 5 \"the maximum number of entries to process\"
       :minimum 4
       :variance 0.01 \"if specified, changes the variance\"]
      (do-something-cool))
  > (doc foo)
  \"...
  Required arguments:
  xs  the collection of things to process
  Optional arguments:
  :maximum 5  the maximum number of entries to process
  :minimum 4
  :variance 0.01  if specified, changes the variance\"
  > (:arglists (meta #'foo))
  ([xs ys margin :maximum 5 :minimum 4 :variance 0.01])"

  [fn-name & docstring-args-and-body]
  (let [[docstring formals & body] (if-not (string? (first docstring-args-and-body))
                                     (cons "" docstring-args-and-body)
                                     docstring-args-and-body)
        [positionals keywords]     (split-with (complement keyword?) formals)
        positional-args            (vec (filter symbol? positionals))
        positional-docs            (into {} (map vec (filter #(and (symbol? (first %))
                                                                   (string? (second %)))
                                                             (partition 2 1 positionals))))

        map-keywords               (fn map-keywords [f [k default doc? & rest]]
                                     (when (keyword? k)
                                       (if (string? doc?)
                                         (cons (f k default doc?) (map-keywords f rest))
                                         (cons (f k default) (map-keywords f (cons doc? rest))))))

        recognized-keywords        (set (map-keywords (fn [k & rest] k) keywords))
        default-map                (into {} (map-keywords (fn [k v & [doc]] [k v]) keywords))

        keyword-formals            (map-keywords (fn [k & rest] (symbol (name k))) keywords)
        keyword-docs               (into {} (map-keywords (fn [k v & [doc]] (when doc [k doc]))
                                                          keywords))
        keyword-defaults           (apply concat (map-keywords (fn [k v & rest] [k v]) keywords))
        scoped-defaults            (into {} (map-keywords (fn [k v & [doc]] [(symbol (name k))
                                                                             defnk-default])
                                                          keywords))
        default-map-gensym         (gensym "default-map")
        default-assignments        (mapcat #(vector % `(if (= ~% ~defnk-default)
                                                         ~(get default-map (keyword %))
                                                         ~%))
                                           keyword-formals)

        positional-width           (reduce max 0 (map (comp count str) positional-args))
        keyword-width              (reduce max 0 (map-keywords (fn [k v & rest]
                                                                 (count (str k " " v)))
                                                               keywords))

        positional-docstring       (when-not (empty? positional-docs)
                                     (str "Required arguments:\n"
                                          (->> positional-args
                                               (map #(format (str "%-" positional-width "s  %s")
                                                             % (str (positional-docs %))))
                                               (join "\n"))))

        keyword-docstring          (when-not (empty? keyword-docs)
                                     (str "Optional arguments:\n"
                                          (->> keywords
                                               (map-keywords
                                                (fn [k v & [doc]]
                                                  (format (str "%-" keyword-width "s  %s")
                                                          (str k " " v) (str doc))))
                                               (filter string?)
                                               (join "\n"))))

        final-docstring            (join "\n" (filter string? [docstring
                                                               positional-docstring
                                                               keyword-docstring]))]

    `(do (defn ~fn-name ~final-docstring [~@positional-args &
                                          {:keys ~keyword-formals :or ~scoped-defaults :as a#}]
           (when-let [strays# (seq (difference (set (keys a#)) ~recognized-keywords))]
             (throw (Exception. (str "invalid keyword args for " '~fn-name ": " strays#))))
           (let [~default-map-gensym ~default-map
                 ~@default-assignments]
             ~@body))
         (alter-meta! (var ~fn-name) assoc :arglists '(~(vec (concat positional-args
                                                                     (seq keyword-defaults)))))
         (var ~fn-name))))
