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
            [clojure.spec.alpha :as s]
            [upgraded-winner.views.error :as views.error]))

(defn init []
  (println "upgraded-winner is starting")
)
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

(def app
  (ring/ring-handler
   (ring/router
    [["/test"
      {:name ::test
       :coercion spec/coercion
       :parameters {:query {:foo int?}}
       :middleware [spec-coercion-error-middleware
                    rrc/coerce-exceptions-middleware
                    wrap-params
                    rrc/coerce-request-middleware
                    rrc/coerce-response-middleware]
       :handler (fn [req] {:status 200 :body (str req)})}]])))
