(ns upgraded-winner.install
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.params :refer [wrap-params]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.java.jdbc :refer [db-do-commands with-db-connection db-do-prepared]]
            [clojure.java.io :refer [resource]]
            [upgraded-winner.db :refer [db]]))

;; make the database
(defn make-database! []
  (try
    (db-do-commands
     (assoc db :dbname "") false
     (slurp (resource "db_install.sql")))
    nil))

(defn make-tables! []
  (db-do-prepared
       db false
       [(slurp (resource "db_maketables.sql"))]))

;; (make-database!)
;; (make-tables!)


