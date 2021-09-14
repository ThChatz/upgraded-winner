(ns upgraded-winner.routes.session
  (:require [hugsql.core :as hugsql]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [upgraded-winner.db :refer [ db ]]))

(hugsql/def-db-fns "queries/session.sql")

;; from https://gist.github.com/kubek2k/8446062#gistcomment-3009390
(defn sha256 [string]
  (let [digest (.digest (java.security.MessageDigest/getInstance "SHA-256") (.getBytes string "UTF-8"))]
    (apply str (map (partial format "%02x") digest))))

(defn hash-password [user-info]
  (update user-info :password sha256))

(defn post-handler [{{user-info :body} :parameters session :session}]
  (let [user (->> user-info (get-login db ,,,) first)]
    (if (nil? user)
      {:status 418 :body {:error "Invalid email or password"}}
      {:status 303
       :body user
       :session (assoc
                 session
                 :identity (user :id))})))

(defn get-handler [{{user-info :body} :parameters session :session}]
  (let [user (->> user-info (get-login db ,,,) first)]
    (if (nil? user)
      {:status 401 :body {}}
      {:status 200
       :body {}
       :session (assoc
                 session
                 :user-id (user :id))})))


(defn delete-handler [req]
  {:status 200
   :session {}})

;; ================================================================================
;;                                     ROUTES
;; ================================================================================

(def route
  ["/session"
   {:name ::session
    :post
    {:parameters {:body {:password :upgraded-winner.routes.user/password
                         :email :upgraded-winner.routes.user/email}}
     :handler post-handler}
    :delete
    {:handler delete-handler}}])

