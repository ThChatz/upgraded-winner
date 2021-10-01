(ns upgraded-winner.routes.user.notifications
  (:require [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [ db ]]))

(hugsql/def-sqlvec-fns "queries/notifications.sql")
(hugsql/def-db-fns "queries/notifications.sql")

(defn get-handler [req]
  (get-notifications db {:usr (-> req :session :identity)}))


(def route
  ["/notifications"
   {:name ::notifications
    :get 
    {:parameters {:path {:page pos-int?}}
     :handler get-handler}}])

(insert-notification db {:usr 1 :type :comment :pic 1 :comment_id 1})
