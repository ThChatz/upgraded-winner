(ns upgraded-winner.handler
  (:require [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.params :refer [wrap-params]]
            [hiccup.middleware :refer [wrap-base-url]]
            [hiccup.core :as hcore]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.coercion :as rrc]
            [reitit.coercion.spec :as spec]
            [reitit.ring.middleware.exception]
            [ring.middleware.session :refer [wrap-session]]
            [clojure.spec.alpha :as s]
            [upgraded-winner.views.error :as views.error]
            [upgraded-winner.actions.account :as account-actions]
            [upgraded-winner.actions.post :as post-actions]
            [upgraded-winner.middleware :refer [wrap-create-session]]))

(defn init []
  (println "upgraded-winner is starting"))
(defn destroy []
  (println "upgraded-winner is shutting down"))

(defn spec-coercion-error-middleware [handler]
  (fn [request]
    (let [response (handler request)]
      (if (not (nil? (-> response :body :problems)))
        {:status (response :status)
         :body (views.error/coercion (-> response :body :problems))}
      response))))



(defn my-middleware [handler]
  (fn [request]
    (let [response (handler request)]
      (if (not (nil? (-> response :body :spec)))
        (reitit.ring.middleware.exception/default-handler response)))))

(def top-level-middleware
  [wrap-session
   wrap-create-session
   spec-coercion-error-middleware
   rrc/coerce-exceptions-middleware
   wrap-params
   rrc/coerce-request-middleware
   rrc/coerce-response-middleware])

(def app
  (ring/ring-handler
   (ring/router
    [["/test"
      {:name ::test
       :parameters {:query {:foo int?}}
       :handler (fn [req] {:status 200 :body (str req) :session {}})}]
     ["/actions"
      account-actions/login-route
      account-actions/register-route
      post-actions/create-post]]
    {:data
     {:coercion spec/coercion
      :middleware top-level-middleware}})))



