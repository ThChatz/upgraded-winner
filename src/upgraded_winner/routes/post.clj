(ns upgraded-winner.routes.post
  (:require [hugsql.core :as hugsql]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [upgraded-winner.db :refer [ db ]]))

(s/def ::post-id int?)
(s/def ::post-id int?)


(hugsql/def-db-fns "queries/post.sql")

(defn post-handler [req]
  (let [{{usr :identity} :session {{text :text} :body} :parameters} req
        resp (insert-post db {:usr usr :media 0 :content text})]
    (if (= resp 1)
      {:status 200
       :body resp})))

(defn put-handler [req]
  (let [{{usr :identity} :session
         {{text :text} :body
          {post :post-id} :path} :parameters} req
        resp (edit-post db {:text text :id post :usr usr})]
    {:status 200
     :body resp}))

(defn get-handler [req]
  (let [post (first (get-post db {:id (-> req :parameters :path :post-id)}))]
    (if (nil? post)
      {:status 404 :body {:error "Post not found"}}
      {:status 200 :body post})))

(defn delete-handler [req]
  (let [{{usr :identity} :session
         {{post :post-id} :path} :parameters} req
        resp (delete-post db {:id post :usr usr})]
    {:status 200
     :body resp}))


(def route
  ["/post"
   {:name ::post
    :post
    {:parameters {:body {:text (complement nil?)}}
     :handler post-handler}}])

(def route-id
  ["/post/:post-id"
    {:name ::post-id
     :get
     {:parameters {:path {:post-id int?}}
      :handler get-handler}
     :put
     {:parameters {:body {:text (complement nil?)}
                   :path {:post-id int?}}
      :handler put-handler}
     :delete
     {:parameters {:path {:post-id int?}}
      :handler delete-handler}}])
