(ns upgraded-winner.middleware.network
  (:require [upgraded-winner.specs.user :refer [user-id-spec]]
            [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [db]]))

(hugsql/def-db-fns "queries/network.sql")

(defn connected? [req]
  (let [usr (-> req :session :identity)
        friend (-> req :parameters :path :user-id)]
    (do
      (if (nil? usr)
        false
        (not (nil?
              (friend? db
                       {:usr usr
                        :friend friend})))))))

(defn wrap-connected? [handler]
  (fn [req]
    (handler (assoc req :connected? (connected? req)))))

(defn wrap-me? [handler]
  (fn [req]
    (handler (assoc req
                    :me? (=
                          (-> req :session :identity)
                          (-> req :parameters :path :user-id))))))
