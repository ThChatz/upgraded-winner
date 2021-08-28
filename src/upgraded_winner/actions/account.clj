(ns upgraded-winner.actions.account
  (:require [hugsql.core :as hugsql]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [upgraded-winner.db :refer [ db ]]))
;; specs
(s/def ::username
  #(re-matches #"[a-zA-Z0-9_\-]{1,30}" %))

(let [email-regex #"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])"]
  (s/def ::email #(re-matches email-regex %)))

(s/def ::password (complement nil?))


(hugsql/def-sqlvec-fns "queries/account.sql")

(hugsql/def-db-fns "queries/account.sql")

;; from https://gist.github.com/kubek2k/8446062#gistcomment-3009390
(defn sha256 [string]
  (let [digest (.digest (java.security.MessageDigest/getInstance "SHA-256") (.getBytes string "UTF-8"))]
    (apply str (map (partial format "%02x") digest))))

(defn hash-password [user-info]
  (update user-info :password sha256))

(defn create-new [user-info]
  (insert-new-user db (hash-password user-info)))

(defn login [user-info]
  (first (get-login db (update user-info :password sha256))))

(defn login-handler [{{user-info :body} :parameters session :session}]
  (let [user (->> user-info (get-login db ,,,) first)]
    (if (nil? user)
      {:status 418 :body "invalid username/password"}
      {:status 303
       :body user
       :session (assoc
                 session
                 :user-id (user :id))})))


(defn register-handler [{{params :body} :parameters}]
  (do
    (insert-new-user db params)
    {:status 200
     :body "User successfully created!"}))

;; ================================================================================
;;                                     ROUTES
;; ================================================================================

(def login-route
  ["/login"
       {:post
        {:name ::login
         :parameters {:body {:username ::username
                             :password ::password}}
         :handler login-handler}}])


(def register-route
  ["/register"
   {:post
    {:name ::register
     :parameters {:body {:username ::username
                         :password ::password
                         :email ::email}}
     :handler register-handler}}])
