(ns upgraded-winner.routes.user
  (:require [hugsql.core :as hugsql]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [upgraded-winner.db :refer [db]]
            [buddy.core.hash :refer [sha256]]
            [buddy.core.codecs :refer [bytes->hex]]))


;; specs
(s/def ::user-id (s/or
                  :id int?
                  :my-account (partial = "my-account")))

(s/def ::name
  #(re-matches #"[a-z]+" %))

(let [email-regex #"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])"]
  (s/def ::email #(re-matches email-regex %)))

(s/def ::password (complement nil?))

(s/def ::phone
       #(re-matches #"\+?[0-9]+" %))

(s/def ::bool
       (s/or
        :str-true (partial = "true")
        :str-false (partial = "false")
        :bool boolean?))

(hugsql/def-db-fns "queries/user.sql")
(hugsql/def-sqlvec-fns "queries/user.sql")


(defn connected? [user1 user2]
  (-> (get-users-are-connected db {:user1 user1 :user2 user2})
      (nth 0)
      :result))

;; (defn post-handler [{{params :body} :parameters}]
;;   (do
;;     (insert-new-user db (-> params
;;                             (assoc ,,, :is-admin false)
;;                             (update
;;                              :password
;;                              #(-> % sha256 bytes->hex))))
;;     {:status 200
;;      :body "User successfully created!"}))


(defn post-handler [req]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (-> req :multipart-params)})

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
     {:parameters {:body {:first-name ::name
                          :last-name ::name
                          :password ::password
                          :email ::email
                          :job (complement nil?)
                          :bio (complement nil?)
                          :phone ::phone
                          :email-private ::bool
                          :bio-private ::bool
                          :phone-private ::bool
                          :job-private ::bool
                          :network-private ::bool}
                   :multipart {:picture (complement nil?)}}
      :handler post-handler}}]
   ["/user"
    ["/:user-id"
     {:name ::user-id
      :get
      {:parameters {:path {:user-id ::user-id}}
       :handler get-handler}}]]])


