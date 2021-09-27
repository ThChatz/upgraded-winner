(ns upgraded-winner.routes.post.comment
  (:require [hugsql.core :as hugsql]
            [clojure.string :as str]
            [upgraded-winner.specs.post :refer [post-id-spec]]
            [upgraded-winner.db :refer [ db ]]))

(hugsql/def-db-fns "queries/post.comment.sql")

(defn post-handler
  "Post a comment to the post"
  [req]
  (let [{{usr :identity} :session
         {{text :text} :body
         {post :post-id} :path} :parameters} req
        resp (insert-comment db {:usr usr :post post :text text})]
    (if (= resp 1)
      {:status 200
       :body resp})))

(defn put-handler [req]
  (let [{{usr :identity} :session
         {{text :text} :body
          {comment :comment-id
           post :post-id} :path} :parameters} req
        resp (edit-comment db {:text text :id comment :post post :usr usr})]
    {:status 200
     :body resp}))

(defn delete-handler [req]
  (let [{{usr :identity} :session
         {{comment :comment-id
           post :post-id} :path} :parameters} req
        resp (delete-comment db {:id comment :post post :usr usr})]
    {:status 200
     :body resp}))


(def route
  ["/post/:post-id"
   ["/comment"
    {:name ::comment
     :post {:parameters {:path {:post-id post-id-spec}
                         :body {:text (complement nil?)}}
            :handler post-handler}}]
   ["/comment/:comment-id"
    {:name ::comment-id
     :put
     {:parameters {:path {:post-id post-id-spec :comment-id int?}
                   :body {:text (complement nil?)}}
      :handler put-handler}
     :delete
     {:parameters {:path {:post-id post-id-spec :comment-id int?}}
      :handler delete-handler}}]])
