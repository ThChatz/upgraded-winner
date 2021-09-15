(ns upgraded-winner.routes.session
  (:require [hugsql.core :as hugsql]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [upgraded-winner.db :refer [ db ]]
            [buddy.core.hash :refer [sha256]]
            [buddy.core.codecs :refer [bytes->hex]]))

(hugsql/def-db-fns "queries/session.sql")


(defn post-handler [{{user-info :body} :parameters session :session}]
  (let [user (->> (update user-info :password #(-> % sha256 bytes->hex))
                  (get-login db ,,,)
                  first)]
    (if (nil? user)
      {:status 418 :body {:error "Invalid email or password"}}
      {:status 200
       :body (dissoc user :password)
       :session (assoc
                 session
                 :identity (user :id))})))

(defn get-handler [req]
  (if (nil? (-> req :session :identity))
    {:status 401 :body {}}
    {:status 200
     :body {}}))


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

