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

(defn kw->enum [keyword]
  (format "'%s'" (name keyword)))

(defn page [params]
  (let [f_ (fn [x] (if (empty? x) nil x))
        f #(f_ (str %1 %2 %3))]
    (f (if (contains? params :before)
         "AND time > :before")
       (if (contains? params :after)
         "AND time < :after")
       (if (contains? params :limit)
         "LIMIT :limit"))))

