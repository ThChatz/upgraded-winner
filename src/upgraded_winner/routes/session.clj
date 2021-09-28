(ns upgraded-winner.routes.session
  (:require [hugsql.core :as hugsql]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [upgraded-winner.db :refer [ db ]]))

(hugsql/def-db-fns "queries/session.sql")


(defn post-handler [{{user-info :body} :parameters session :session}]
  (let [user (get-login db ,,,)]
    (if (nil? user)
      {:status 418 :body {:error "Invalid email or password"}}
      (let [uinfo (into {} (filter (comp not nil?)
                                   (dissoc user :password)))]
        {:status 200
         :body uinfo
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
   :session {}})

;; ================================================================================
;;                                     ROUTES
;; ================================================================================

(def route
  ["/session"
   {:name ::session
    :post
    {:parameters {:body {:password :upgraded-winner.routes.user/password
                         :email :upgraded-winner.routes.user/email}}
     :handler post-handler}
    :delete
    {:handler delete-handler}
    :get
    {:handler get-handler}}])

