(ns upgraded-winner.db)

(def db {:dbtype "postgresql"
         :dbname "upgraded_winner"
         :user "postgres"
         :host "db"
         :password "1234"})

(defn kw->enum [keyword]
  (format "'%s'" (name keyword)))
