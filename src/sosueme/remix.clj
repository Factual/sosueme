(ns sosueme.remix
  (:require [clojure.string :as s]))

;; just for fun -- read in a file of words, break them up by eg syllables,
;; and remix the syllables into new words of similar length

;; given a sample of words, create a model which will generate words like them

(def wordsets [:hawaiian-places :boys-names :girls-names])
(def word-files
  (let [full-path #(str (java.io.File. (.getParent (java.io.File. *file*))
                                       (str (java.io.File. "data" (name %)))) ".txt")]
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

(defn remix
  [words splitter]
  (let [word-pieces (map splitter words)
        lengths (map count word-pieces)
        all-pieces (apply concat word-pieces)]
    (s/capitalize (apply str
                         (repeatedly (rand-nth lengths) #(rand-nth all-pieces))))))

;; (repeatedly 5 #(remix (words :hawaiian-places) split-by-syllables))