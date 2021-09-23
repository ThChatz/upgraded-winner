(ns upgraded-winner.db)

(def db {:dbtype "postgresql"
         :dbname "upgraded_winner"
         :user "postgres"
         :host "db"
         :password "1234"})

(defn kw->enum [keyword]
  (format "'%s'" (name keyword)))

(defn page [params]
  (str
   (if (contains? params :page-size)
     "LIMIT :page-size")
   (if (and (contains? params :page-size)
            (contains? params :page-num))
     (do
       (assoc params :offset (* (:page-size params) 
                                (:page-num params)))
       "OFFSET :offset"))))