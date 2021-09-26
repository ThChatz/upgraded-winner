(ns upgraded-winner.routes.post.reacts
  (:require [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [db]]
            [upgraded-winner.routes.post :refer [post-id-spec]]))

(hugsql/def-db-fns "queries/post.reacts.sql")


(defn post-handler [{{{post :post-id} :path
                      {reaction :reaction} :body} :parameters
                     {usr :identity} :session}]
  (add-react db {:usr usr :post post :reaction reaction}))

(defn delete-handler [{{{post :post-id} :path} :parameters
                     {usr :identity} :session}]
  (delete-react db {:post post :usr usr}))

(defn get-handler [{{{post-id :post-id} :path} :parameters}]
  (get-))