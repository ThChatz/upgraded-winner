(ns upgraded-winner.routes.conversations
  (:require [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [ db ]]))

(hugsql/def-sqlvec-fns "queries/messages.sql")
(hugsql/def-db-fns "queries/messages.sql")

(defn get-conv-handler [req]
  (get-usr-convs db {:usr (-> req :session :identity)}))

(defn get-messages-handler [req]
  (get-conv-messages db {:usr (-> req :session :identity) 
                        :conversation (-> req :parameters :path :conv-id)
                        :page-num (-> req :parameters :path :page)}))

(defn post-message [req]
  (insert-message db {}))
(def route
  ["/conversations"
   {:name ::conversations
    :get
    {:parameters {:path {:page pos-int?}}
     :handler get-conv-handler}}]
  ["/conversations/:conv-id/:page"
   {:name ::conversation-messages
    :get
    {:parameters {:path {:page pos-int? :conv-id pos-int?}}
     :handler get-messages-handler}}])