(ns upgraded-winner.routes.post
  (:require [hugsql.core :as hugsql]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [upgraded-winner.db :refer [ db ]]))


(hugsql/def-db-fns "queries/post.sql")

(defn post-handler [req]
  (let [{{usr :identity} :session {{text :text} :body} :parameters} req
        resp (insert-post db {:usr usr :media 0 :content text})]
    (if (= resp 1)
      {:status 200
       :body resp})))

(defn put-handler [req]
  (let [{{usr :identity} :session {text :text post :post-id} :parameters} req
        resp (edit-post db {:text text :id post :usr usr})]
    {:status 200
     :body resp}))

(defn get-handler [req]
  (let [post (first (get-post db {:id (-> req :parameters :path :post-id)}))]
    (if (nil? post)
      {:status 404 :body {:error "Post not found"}}
      {:status 200 :body post})))

(def route
  ["/post*"
   {:name ::post
    :post
    {:parameters {:body {:text (complement nil?)}}
     :handler post-handler}
    :put
    {:parameters {:body {:text (complement nil?)
                         :post-id int?}}
     :handler put-handler}}
   ["/:post-id"
    {:name ::post-get
     :get
     {:parameters {:path {:post-id int?}}
     :handler get-handler}}]])

(post-handler {:parameters {:body {:text "foo"}}})
(get-handler {:parameters {:path {:post-id 1}}})

(get-post db {:id 1})
