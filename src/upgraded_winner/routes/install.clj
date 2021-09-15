(ns upgraded-winner.install
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.java.io :refer [resource]]
            [hugsql.core :as hugsql]
            [buddy.core.hash :refer [sha256]]
            [buddy.core.codecs :refer [bytes->hex]]
            [upgraded-winner.db :refer [db]]))

(hugsql/def-db-fns "queries/user.sql") ;; insert-new-user

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
  (insert-new-user db (update usr-info :password #(-> % sha256 bytes->hex))))

(def route
  ["/install"
   ["/make-db"
    {:post
     {:handler (fn [_] (make-database!))}}]
   ["/make-admin"
    {:post
     {:parameters {:body {:first-name ::name
                          :last-name ::name
                          :password ::password
                          :email ::email}}
      :handler make-admin!}}]])


;; (make-database!)

