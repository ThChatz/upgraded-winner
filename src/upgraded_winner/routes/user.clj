(ns upgraded-winner.routes.user
  (:require [hugsql.core :as hugsql]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [upgraded-winner.db :refer [db]]
            [buddy.core.hash :refer [sha256]]
            [buddy.core.codecs :refer [bytes->hex]]
            [upgraded-winner.specs.user :as specs]))



(hugsql/def-db-fns "queries/user.sql")
(hugsql/def-sqlvec-fns "queries/user.sql")


(defn connected? [user1 user2]
  (-> (get-users-are-connected db {:user1 user1 :user2 user2})
      (nth 0)
      :result))

(defn post-handler [{{params :body} :parameters}]
  (do
    (insert-new-user db (-> params
                            (assoc ,,, :is_admin false)
                            (update
                             :password
                             #(-> % sha256 bytes->hex))))
    {:status 200
     :body "User successfully created!"}))


(defn get-handler [req]
  (let [user-id-param (-> req :parameters :path :user-id)
        session-identity (-> req :session :identity)
        user-id (if (int? user-id-param) user-id-param session-identity)
        connected (connected? session-identity user-id)
        user-info (get-usr db {:id user-id :connected connected})]
    (if (empty? user-info)
      {:status 404 :body {:error (str "User " user-id-param " not found.")}}
      {:status 200 :body (-> user-info first (dissoc :password))})))

;; ================================================================================
;;                                     ROUTES
;; ================================================================================

(def route
  [""
   ["/user"
    {:name ::user
     :post
     {:parameters {:body specs/sign-up-spec}
      :handler post-handler}}]
   ["/user"
    ["/:user-id"
     {:name ::user-id
      :get
      {:parameters {:path {:user-id specs/user-id-spec}}
       :handler get-handler}}]]])
