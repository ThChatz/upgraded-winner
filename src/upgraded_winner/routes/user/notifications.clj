(ns upgraded-winner.routes.user.notifications
  (:require [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [ db ]]))

(hugsql/def-sqlvec-fns "queries/notifications.sql")
(hugsql/def-db-fns "queries/notifications.sql")

(defn get-handler [req]
  {:status 200
  :body (get-notifications db {:usr (-> req :session :identity)})})


(def route
  ["/notifications"
   {:name ::notifications
    :get 
    {:handler get-handler}}])