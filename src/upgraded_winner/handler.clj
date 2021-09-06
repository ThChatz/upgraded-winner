(ns upgraded-winner.handler
  (:require [reitit.ring :as ring]
            [reitit.ring.coercion :as coercion]
            [reitit.coercion.spec :as spec]
            [reitit.ring.middleware.exception]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [upgraded-winner.actions.account :as account-actions]
            [upgraded-winner.actions.post :as post-actions]
            [upgraded-winner.middleware :refer [wrap-create-session]]))

(defn init []
  (println "upgraded-winner is starting"))
(defn destroy []
  (println "upgraded-winner is shutting down"))

(def top-level-middleware
  [[wrap-defaults site-defaults]
   coercion/coerce-exceptions-middleware
   coercion/coerce-request-middleware
   coercion/coerce-response-middleware])

(def app
  (ring/ring-handler
   (ring/router
    [["/test"
      {:name ::test
       :parameters {:query {:foo int?}}
       :handler (fn [req] {:status 200 :body (str "<div>"req"</div>") :session {}})}]
     ["/actions"
      account-actions/login-route
      account-actions/register-route
      post-actions/make-post-route]]
    {:data
     {:coercion spec/coercion
      :middleware top-level-middleware}})
   (ring/create-resource-handler {:path "/"})))



