(ns upgraded-winner.routes.connections
  (:require [hugsql.core :as hugsql]
            [upgraded-winner.middleware.network :as net-mw]
            [upgraded-winner.db :refer [db]]
            [upgraded-winner.specs.user :refer [user-id-spec]]))

(hugsql/def-db-fns "queries/network.sql")
(hugsql/def-db-fns "queries/messages.sql")

(defn add-friend- [params]
  (let [usr (:usr params)
        friend (:friend params)
        inv-params {:usr friend :friend usr}]
    (cond
      (friend? db params) "already connected"
      (friend-req? db inv-params)
      (do (remove-friend-req db inv-params)
          (add-friend db params)
          (let [conv (new-conv db {})
                f #(insert-usr-conv db {:usr %
                                        :conversation conv})]
            (map f [usr friend]))
          "connection added")
      :else (do (add-friend-req db params) "request sent"))))


(defn post-handler [{{{friend :user-id} :path} :parameters
                     {usr :identity} :session}]
  (add-friend- {:usr usr :friend friend}))

(defn get-handler [{{usr :identity} :session}]
  {:status 200 :body (get-friends db {:usr usr})})

(def route
  ["/connections"
   ["/:user-id"
    {:name ::with-id
     :parameters {:path {:user-id user-id-spec}}}]
   [""
    {:name ::connections
     :handler get-handler}]])
