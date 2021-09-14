(ns upgraded-winner.handler
  (:require [reitit.ring :as ring]
            [reitit.ring.coercion :as coercion]
            [reitit.coercion.spec :as spec]
            [reitit.ring.middleware.exception]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery *anti-forgery-token*]]
            [ring.middleware.session :refer [wrap-session]]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [upgraded-winner.routes.user :as user]
            [upgraded-winner.routes.session :as session]
            [upgraded-winner.routes.anti-forgery-token :as anti-forgery-token]
            [upgraded-winner.routes.post :as post]
            [upgraded-winner.routes.post.comment :as post.comment]
            [upgraded-winner.routes.post.comments :as post.comments]
            [upgraded-winner.middleware :refer [wrap-create-session muuntaja-instance]]
            [reitit.ring.middleware.muuntaja :as muuntaja]))

(defn init []
  (println "upgraded-winner is starting"))
(defn destroy []
  (println "upgraded-winner is shutting down"))

(def test-mw (fn [handler] (fn [req] (do (print req "\n") (handler req)))))

(def top-level-middleware
  [muuntaja/format-middleware
   parameters-middleware
   [wrap-anti-forgery {:read-token (fn [req] (-> req :body-params :__anti-forgery-token))}]
   coercion/coerce-exceptions-middleware
   coercion/coerce-request-middleware
   coercion/coerce-response-middleware
   test-mw])

(def app
  (ring/ring-handler
   (ring/router
    [["/test"
      {:name ::test
       :handler (fn [req] {:status 200 :body {:foo "bar"}})}]
     anti-forgery-token/route
     user/route
     session/route
     post/route-id
     post/route
     post.comment/route
     post.comments/route]
    {:data
     {:muuntaja muuntaja-instance
      :coercion spec/coercion
      :middleware top-level-middleware}})
   (ring/create-resource-handler {:path "/"})
   {:middleware [wrap-session]}))
