(ns upgraded-winner.db
  (:require [environ.core :refer [env]]))

(def db {:dbtype "postgresql"
         :dbname (env :db-name)
         :user (env :db-user)
         :host (env :db-host)
         :password (env :db-password)})

(defn map-filter
  ([pred map]
  (->> map
       (filter pred)
       (into {})))
  ([pred]
   (partial map-filter pred)))

(defn sql-quote [s]
  (str "'" s "'"))

(defn kw->enum [keyword]
  (format "'%s'" (name keyword)))

(defn page
  ([time-key params]
   (format "%s
            %s
            %s
            %s"
           (if (contains? params :before)
             (format "AND %s < TO_TIMESTAMP(:before / 1000)"
                     time-key)
             "")
           (if (contains? params :after)
             (format "AND %s > TO_TIMESTAMP(:after / 1000)"
                     time-key)
             "")
           (format "ORDER BY %s DESC" time-key)
           (if (contains? params :limit)
             "LIMIT :limit"
             ""))))

