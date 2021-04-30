(ns upgraded-winner.routes.home
  (:require [compojure.core :refer :all]
            [upgraded-winner.views.layout :as layout]))

(defn home [who]
  (layout/common [:h1 (format "Hello %s!" who)]))

(defroutes home-routes
  (GET "/home/:who" [who] (home who)))
