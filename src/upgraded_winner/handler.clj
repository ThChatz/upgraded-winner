(ns upgraded-winner.handler
  (:require [reitit.ring :as ring]
            [reitit.coercion.spec :as spec]
            [ring.middleware.session :refer [wrap-session]]
            [upgraded-winner.routes.user :as user]
            [upgraded-winner.routes.session :as session]
            [upgraded-winner.routes.anti-forgery-token :as anti-forgery-token]
            [upgraded-winner.routes.post :as post]
            [upgraded-winner.routes.post.comment :as post.comment]
            [upgraded-winner.routes.post.comments :as post.comments]
            [upgraded-winner.middleware :as mw]))

(defn init []
  (println "upgraded-winner is starting"))
(defn destroy []
  (println "upgraded-winner is shutting down"))

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
      :middleware mw/top-level-middleware}})
   (ring/create-resource-handler {:path "/"})))
