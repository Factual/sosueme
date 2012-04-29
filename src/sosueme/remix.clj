(ns sosueme.remix
  (:require [clojure.string :as s]))

;; just for fun -- read in a file of words, break them up by eg syllables,
;; and remix the syllables into new words of similar length

;; given a sample of words, create a model which will generate words like them

(def wordsets [:hawaiian-places :boys-names :girls-names])

(def word-files
  (let [full-path #(str (first (s/split *file* #"/src/" 2)) "/data/" (name %) ".txt")]
    (into {} (for [ws wordsets] [ws (full-path ws)]))))

(defn read-lines [uri]
  (-> uri slurp s/split-lines))

(defn cleanup-lines [lines]
  (let [comment? #(= (first %) \;)]
    (remove comment?
            (map s/trim lines))))

;; FIXME make this work for sentences too -- need to not
(defn read-words
  ([lines]
      (let [first-word #(first (s/split % #"[|,\s]" 2))]
        (map (comp s/lower-case first-word)
             lines))))

(defn words [key]
  (-> key word-files read-lines cleanup-lines read-words))

(defn split-by-syllables [word]
  (s/split word #"(?<=[aeiou])(?=[bcdfghjklmnpqrstvwxyz])"))

(defn remix-aux
  [pieces-seq lengths]
  ;; syllable-position-blind sampling
  #_ (let [all-pieces (apply concat pieces-seq)]
       (repeatedly (rand-nth lengths) #(rand-nth all-pieces)))
  ;; syllable-position-aware sampling
  (for [k (range 0 (rand-nth lengths))] (nth (rand-nth pieces-seq) k nil)))

(defn remix
  [items splitter]
  (let [pieces-seq (map splitter items)
        lengths (map count pieces-seq)]
    (s/capitalize (apply str
                         (remix-aux pieces-seq lengths)))))

(defn print-n [n dataset-name]
  (dorun (map println (repeatedly n #(remix (words dataset-name) split-by-syllables)))))

(comment
  (print-n 20 :hawaiian-places)
  (print-n 20 :boys-names)
  (print-n 20 :girls-names))