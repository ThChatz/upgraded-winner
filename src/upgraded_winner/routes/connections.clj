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
          (let [conv (:id (new-conv db {}))]
            (insert-usr-conv db {:usr usr :conversation conv})
            (insert-usr-conv db {:usr friend :conversation conv}))
          "connection added")
      :else (do (add-friend-req db params) "request sent"))))


(defn post-handler [{{{friend :user-id} :path} :parameters
                     {usr :identity} :session}]
  
  {:status 200
   :body {:message (add-friend- {:usr usr
                                 :friend friend})}})

(defn get-handler [{{usr :identity} :session}]
  {:status 200 :body (get-friends db {:usr usr})})

(defn get-requests-handler [{{usr :identity} :session}]
  {:status 200 :body (get-friend-reqs db {:usr usr})})



(def route
  [""
   ["/connections/requests"
    {:name ::requests
     :handler get-requests-handler}]
   ["/connections/:user-id"
    {:name ::with-id
     :parameters {:path {:user-id user-id-spec}}
     :post post-handler}]
   ["/connections"
    {:name ::connections
     :handler get-handler}]])


;; (add-friend- {:usr 1 :friend 2})
;; (add-friend- {:usr 2 :friend 1})

;; (new-conv db {})
;; (:id (new-conv db {}))
