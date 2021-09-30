(ns upgraded-winner.routes.post.comments
  (:require [hugsql.core :as hugsql]
            [upgraded-winner.specs.post :refer [post-id-spec]]
            [upgraded-winner.db :refer [db]]))

(hugsql/def-db-fns "queries/post.comments.sql")

(defn get-handler [req]
  (let [{{usr :identity} :session
         {{post :post-id} :path} :parameters} req
        resp (get-comments-of-post db {:post post})]
    {:status 200
     :body resp}))

(def route
  ["/post/:post-id/comments"
   {:name ::comments
    :get {:parameters {:path {:post-id post-id-spec}}
            :handler get-handler}}])
