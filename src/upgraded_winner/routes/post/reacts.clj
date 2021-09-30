(ns upgraded-winner.routes.post.reacts
  (:require [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [db]]
            [spec-tools.core :as st]
            [upgraded-winner.specs.post :refer [post-id-spec]]))

(hugsql/def-db-fns "queries/post.reacts.sql")
(hugsql/def-sqlvec-fns "queries/post.reacts.sql")

(def react?
  (st/spec
   {:name ::react
    :type :keyword
    :spec #{:like :dislike :love}
    :reason "reaction must be one of 'like', 'dislike', 'love'"}))


(defn post-handler [{{{post :post-id} :path
                      {reaction :reaction} :body} :parameters
                     {usr :identity} :session}]
  {:status 200
   :body (add-react db {:usr usr :post post :reaction reaction})})

(defn delete-handler [{{{post :post-id} :path} :parameters
                     {usr :identity} :session}]
  (delete-react db {:post post :usr usr}))

(def route
  ["/post/:post-id/react"
   {:name ::react
   :parameters {:path {:post-id post-id-spec}}
   :post
   {:parameters {:body {:reaction react?}}
    :handler post-handler}}])
