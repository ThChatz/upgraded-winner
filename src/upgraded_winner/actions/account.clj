(ns upgraded-winner.actions.account
  (:require [hugsql.core :as hugsql]))

(def db {:dbtype "postgresql"
         :dbname "upgraded_winner"
         :user "postgres"
         :host "db"
         :password "1234"})


(hugsql/def-sqlvec-fns "queries/account.sql")

(hugsql/def-db-fns "queries/account.sql")

;; from https://gist.github.com/kubek2k/8446062#gistcomment-3009390
(defn sha256 [string]
  (let [digest (.digest (java.security.MessageDigest/getInstance "SHA-256") (.getBytes string "UTF-8"))]
    (apply str (map (partial format "%02x") digest))))

(defn create-new [user-info]
  (insert-new-user db (update user-info :password sha256)))

(defn login [user-info]
  (first (get-login db (update user-info :password sha256))))