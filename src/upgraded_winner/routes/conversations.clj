(ns upgraded-winner.routes.conversations
  (:require [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [ db ]]
            [spec-tools.core :as st]
            [spec-tools.data-spec :as ds]))

(hugsql/def-sqlvec-fns "queries/messages.sql")
(hugsql/def-db-fns "queries/messages.sql")

(defn get-conv-handler [req]
  {:status 200
   :body (get-usr-convs db {:usr (-> req :session :identity)})})

(defn get-messages-handler [req]
  {:status 200
   :body (map #(update % :time (fn [x] (.getTime x)))
              (get-conv-messages db
                                 (merge
                                  {:usr
                                   (-> req :session :identity) 
                                   :conversation
                                   (-> req
                                       :parameters
                                       :path
                                       :conv-id)}
                                  (-> req :parameters :query))))})

(defn post-message [{{{msg :message} :body
                      {cnv :conv-id} :path} :parameters
                     {usr :identity} :session}]
  (do (insert-message db {:usr usr
                      :message msg
                          :conversation cnv})
      {:status 200}))

(def route
  [["/conversations"
    {:name ::conversations
     :get
     {:handler get-conv-handler}}]
   ["/conversations/:conv-id"
    {:name ::conversation-messages
     :parameters {:path {:conv-id pos-int?}}
     :get
     {:parameters {:query {(ds/opt :before) pos-int?
                           (ds/opt :after) pos-int?
                           (ds/opt :limit) pos-int?}}
      :handler get-messages-handler}
     :post
     {:parameters {:body {:message string?}}
      :handler post-message}}]])

;; (get-conv-handler {:session {:identity 1}})
