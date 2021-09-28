(ns upgraded-winner.routes.install
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.java.io :refer [resource]]
            [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [db]]
            [upgraded-winner.specs.user :refer
             [user-email-spec user-password-spec]]))

(hugsql/def-db-fns "queries/install.sql")
;; insert-new-user

;; make the database
(defn make-database! []
  (try
    (do
      (jdbc/db-do-commands
       (assoc db :dbname "") false
       (slurp (resource "db_install.sql")))
      (jdbc/db-do-prepared
       db false
       [(slurp (resource "db_maketables.sql"))]))
    nil))

(defn make-admin! [{{usr-info :body} :parameters}]
  (insert-new-admin db usr-info))

(def route
  ["/install"
   {:post
    {:parameters {:body {:email user-email-spec
                         :password user-password-spec
                         :name user-name-spec}}
     :handler (fn [req] (do (make-database!)
                           (make-admin! req)))}}])


;; (make-database!)

