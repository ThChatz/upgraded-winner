(ns upgraded-winner.routes.post.comments
  (:require [hugsql.core :as hugsql]
            [upgraded-winner.specs.post :refer [post-id-spec]]
            [upgraded-winner.db :refer [db]]))

(hugsql/def-db-fns "queries/post.comments.sql")

(defn get-handler [req]
  (let [{{usr :identity} :session
         {{post :post-id page :page} :path} :parameters} req
        resp (get-comments-of-post db {:post post :page-size 20 :page-num page})]
    (if (zero? (count resp))
      {:status 404
       :body {:error "Page number out of bounds"}}
      {:status 200
     :body resp})))


(def route
  ["/post/:post-id/comments/:page"
   {:name ::comments
    :get {:parameters {:path {:post-id post-id-spec
                              :page pos-int?}}
            :handler get-handler}}])
