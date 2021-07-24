(ns upgraded-winner.common-specs
  (:require [clojure.spec.alpha :as s]))

(s/def ::not-nil #(not (nil? %)))
