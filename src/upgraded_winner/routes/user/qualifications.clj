(ns upgraded-winner.routes.user.qualifications
  (:require [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [db]]
            [spec-tools.core :as st]))

(hugsql/def-sqlvec-fns "queries/qualifications.sql")
(hugsql/def-db-fns "queries/qualifications.sql")

(defn put-or-get-qualification [name]
  (let [exists (get-qualification db {:name name})]
    (if (nil? exists)
      (add-qualification db {:name name})
      (:id exists))))

(defn post-handler [{{{p :is-private 
                       q :qualification} :body} :parameters
                     {id :identity} :session}]
  (add-user-qualification db 
         {:usr id
          :is-private p
          :qualification (put-or-get-qualification q)}))


(defn delete-handler [{{{q :qualification} :body} :parameters
                     {id :identity} :session}]
  (delete-user-qualification db 
         {:usr id :qualification (put-or-get-qualification q)}))

(defn get-handler [req]
  (let [qparams (-> req :parameters :query)
        pparams (-> req :parameters :path)
        params (merge qparams pparams)]
    (get-user-qualifications db params)))

(def route
  [""
   ["/qualifications"
   {:name ::qualifications
    :post
    {:paramteters {:body {:id-private boolean?
                          :qualification string?}}
     :handler post-handler}
    :delete
    {:paramteters {:body {:qualification pos-int?}}
     :handler delete-handler}}]
   ["/:user-id/qualifications"
    {:name ::usr-qualifications
     :get
     {:patameters {:path {:user-id pos-int?}
                   :query {:page-num pos-int? :page-size pos-int?}}}}]])