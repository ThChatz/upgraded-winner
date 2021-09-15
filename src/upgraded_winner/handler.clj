(ns upgraded-winner.handler
  (:require [reitit.ring :as ring]
            [reitit.coercion.spec :as spec]
            [ring.middleware.session :refer [wrap-session]]
            [upgraded-winner.routes.core :refer [route-tree]]
            [upgraded-winner.middleware :as mw]))

(defn init []
  (println "upgraded-winner is starting"))
(defn destroy []
  (println "upgraded-winner is shutting down"))

(def app
  (ring/ring-handler
   (ring/router
    route-tree
    {:data
     {:muuntaja muuntaja-instance
      :coercion spec/coercion
      :middleware mw/top-level-middleware}})
   (ring/create-resource-handler {:path "/"})))
