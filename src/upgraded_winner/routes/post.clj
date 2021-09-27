(ns upgraded-winner.routes.post
  (:require [hugsql.core :as hugsql]
            [spec-tools.core :as st]
            [clojure.string :as str]
            [upgraded-winner.db :refer [ db ]]
            [upgraded-winner.routes.post.comment :as comment]
            [upgraded-winner.routes.post.comments :as comments]))

(def post-id-spec
  (st/spec
   {:name ::post-id
    :spec pos-int?
    :reason "post-id must be a positive integer"}))


(hugsql/def-db-fns "queries/post.sql")

(defn format-post [post]
  (update post :created_at #(.getTime %)))

(defn post-handler [req]
  (let [{{usr :identity} :session {{text :text} :body} :parameters} req
        resp (insert-post db {:usr usr :media 0 :content text})]
    (if (= resp 1)
      {:status 200
       :body (-> req :session)})))

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
      {:status 200 :body (format-post post)})))

(defn delete-handler [req]
  (let [{{usr :identity} :session
         {{post :post-id} :path} :parameters} req
        resp (delete-post db {:id post :usr usr})]
    {:status 200
     :body resp}))


(def route
  [""
   ["/post"
   {:name ::post
    :post
    {:parameters {:body {:text (complement nil?)}}
     :handler post-handler}}]
   ["/post/:post-id"
    {:name ::post-id
     :get
     {:parameters {:path {:post-id post-id-spec}}
      :handler get-handler}
     :put
     {:parameters {:body {:text (complement nil?)}
                   :path {:post-id post-id-spec}}
      :handler put-handler}
     :delete
     {:parameters {:path {:post-id post-id-spec}}
      :handler delete-handler}}]
   comment/route
   comments/route])


;; (insert-post db {:usr 1 :media 0 :content "test"})
;; (java.sql.Timestamp. 1650000000000)
;; (.getTime (:created_at (first (get-post db {:id 1}))))
;; (get-post db {:id 1})
