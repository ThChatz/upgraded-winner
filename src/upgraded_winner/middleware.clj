(ns upgraded-winner.middleware
  (:require [upgraded-winner.xml-format :refer [xml-format]]
            [muuntaja.core :as m]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.exception :as exception]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery *anti-forgery-token*]]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.memory :refer [memory-store]]
            [reitit.ring.middleware.multipart :as multipart]))



(defn wrap-create-session [handler]
  (fn [req]
    (let [response (handler req)]
      (if (contains? response :session)
        response
        (assoc response :session {})))))

;; Muuntaja configuration

(def muuntaja-instance
  (m/create
   (-> m/default-options
       (assoc-in ,,, [:formats "application/xml"] xml-format))))

(def anti-forgery-opts
  {:read-token (fn [req] (or (-> req :body-params :__anti-forgery-token)
                            (get-in req [:headers "x-csrf-token"])(print req)))})

(def my-defaults
  (->
   site-defaults
   (assoc :params false)
   (assoc :session false)
   (assoc-in [:security :anti-forgery] false)
   (assoc :static false)
   (assoc-in [:responses :content-types] false)))


(def session-store (memory-store))

(def top-level-middleware
  [   
[wrap-session {:store session-store}]
   muuntaja/format-middleware
   exception/exception-middleware
   parameters-middleware
   multipart/multipart-middleware
   [wrap-defaults my-defaults]
   [wrap-anti-forgery anti-forgery-opts]
   coercion/coerce-request-middleware
   coercion/coerce-response-middleware
   coercion/coerce-exceptions-middleware])
