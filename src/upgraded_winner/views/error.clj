(ns upgraded-winner.views.error
  (:require [upgraded-winner.views.layout :as layout]
            [hiccup.core :as core]))

(defn user-input [input]
  [:b [:i "'" (core/h input) "'"]])

(defn coercion-problem-format [problem]
  [:li
   "Invalid value for "
   (user-input (problem :path))
   ". Value "
   (user-input (problem :val))
   " could not satisfy spec "
   (user-input (problem :pred))
   "."])


(defn coercion [problems]
  (core/html
   (layout/common [:ul (map coercion-problem-format problems)])))

