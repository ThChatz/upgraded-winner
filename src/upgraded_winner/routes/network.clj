(ns upgraded-winner.routes.network
  (:require [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [db]]
            [spec-tools.core :as st]))


(hugsql/def-sqlvec-fns "queries/network.sql")
(hugsql/def-db-fns "queries/network.sql")


(def id-spec
  (st/spec
   {:name ::id
    :spec pos-int?
    :reason "User ids must be positive integers"}))


(defn post-handler [req]
  (add-friend db 
                  (assoc (-> req :parameters :body)
                         :usr (-> req :session :identity))))

(defn delete-handler [req]
  (remove-friend-req db
                     (assoc (-> req :parameters :body)
                         :usr (-> req :session :identity))))

(def route
  ["/network"
   {:name ::network
    :post
    {:parameters {:body {:friend id-spec}}}
    :delete
    {:parameters {:body {:friend id-spec}}}}])
