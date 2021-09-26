(ns upgraded-winner.routes.feed
  (:require [hugsql.core :as hugsql]
            [clojure.string :as str]
            [upgraded-winner.db :refer [ db ]]
            [upgraded-winner.routes.post.comment :as comment]
            [upgraded-winner.routes.post.comments :as comments]
            [upgraded-winner.routes.post :refer [format-post]]))

(hugsql/def-db-fns "queries/feed.sql")

(defn get-handler [{{usr :identity} :session}]
  {:status 200
   :body {:post_ids (get-feed db {:usr usr})
          :usr usr}})

(def route
  ["/feed/0"
   {:name ::feed
    :get
    {:handler get-handler}}])

