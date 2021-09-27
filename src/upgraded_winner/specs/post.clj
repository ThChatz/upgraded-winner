(ns upgraded-winner.specs.post
  (:require [spec-tools.core :as st]))

(def post-id-spec
  (st/spec
   {:name ::post-id
    :spec pos-int?
    :reason "post-id must be a positive integer"}))
