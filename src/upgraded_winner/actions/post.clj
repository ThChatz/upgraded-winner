(ns upgraded-winner.actions.post
  (:require [hugsql.core :as hugsql]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [upgraded-winner.db :refer [ db ]]))


(hugsql/def-sqlvec-fns "queries/post.sql")

(hugsql/def-db-fns "queries/post.sql")

(defn create-post [req]
  (let [{{id :user-id} :session {{text :text} :body} :parameters} req]
    (insert-new-post db {:usr id :media 0 :content text})))

(def make-post-route
  ["/new-post"
   {:name ::new-post
    :post
    {:parameters {:body {:text (complement nil?)}}
     :handler create-post}}])
