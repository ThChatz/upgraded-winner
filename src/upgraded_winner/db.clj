(ns upgraded-winner.db)

(def db {:dbtype "postgresql"
         :dbname "upgraded_winner"
         :user "postgres"
         :host "db"
         :password "1234"})

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
