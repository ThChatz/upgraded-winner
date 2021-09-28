(ns upgraded-winner.routes.session
  (:require [hugsql.core :as hugsql]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [upgraded-winner.db :refer [ db ]]
            [upgraded-winner.specs.user :refer
             [user-email-spec user-password-spec]]))

(hugsql/def-db-fns "queries/session.sql")
(hugsql/def-sqlvec-fns "queries/session.sql")


(defn post-handler [{{user-info :body} :parameters session :session}]
  (let [user (get-login db user-info)]
    (if (nil? user)
      {:status 418 :body {:error "Invalid email or password"}}
      (let [uinfo (into {} (filter (comp not nil?)
                                   (dissoc user :password)))]
        {:status 200
         :body {:user uinfo}
         :session (-> session
                      (assoc :identity (user :id))
                      (assoc :user-info uinfo))}))))


(defn get-handler [req]
  (if (nil? (-> req :session :user-info))
    {:status 401 :body {}}
    {:status 200
     :body (-> req :session :user-info)}))


(defn delete-handler [req]
  {:status 200
   :session {}
   :body {}})

;; ================================================================================
;;                                     ROUTES
;; ================================================================================

(def route
  ["/session"
   {:name ::session
    :post
    {:parameters {:body {:password user-password-spec
                         :email user-email-spec}}
     :handler post-handler}
    :delete
    {:handler delete-handler}
    :get
    {:handler get-handler}}])

