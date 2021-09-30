(ns upgraded-winner.routes.conversations
  (:require [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [ db ]]
            [spec-tools.core :as st]
            [spec-tools.data-spec :as ds]))

(hugsql/def-sqlvec-fns "queries/messages.sql")
(hugsql/def-db-fns "queries/messages.sql")

(defn get-conv-handler [req]
  (get-usr-convs db {:usr (-> req :session :identity)}))

(defn get-messages-handler [req]
  (get-conv-messages db (merge
                         {:usr (-> req :session :identity) 
                          :conversation (-> req
                                            :parameters
                                            :path
                                            :conv-id)}
                         (-> req :parameters :query))))

(defn post-message [{{{msg :message} :body
                      {cnv :conv-id} :path} :parameters
                     {usr :identity} :session}]
  (insert-message db {:usr usr
                      :message msg
                      :conversation cnv}))

(def route
  [["/conversations"
    {:name ::conversations
     :get
     {:handler get-conv-handler}}]
   ["/conversations/:conv-id/"
    {:name ::conversation-messages
     :get
     {:parameters {:path {:conv-id pos-int?}
                   :query {(ds/opt :before) pos-int?
                           (ds/opt :after) pos-int?
                           (ds/opt :limit) pos-int?}}
      :handler get-messages-handler}}]])

;; (get-conv-handler {:session {:identity 1}})
