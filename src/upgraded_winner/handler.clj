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
            [upgraded-winner.actions.account :as account-actions]))

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
       :handler (fn [req] {:status 200 :body (str req)})
       :middleware [wrap-params wrap-session]}]
     ["/actions"
      {:coercion spec/coercion
       :middleware [spec-coercion-error-middleware
                    rrc/coerce-exceptions-middleware
                    rrc/coerce-request-middleware
                    rrc/coerce-response-middleware]}
      ["/login"
       {:post
        {:name :account-actions/login
         :parameters {:body {:username #(not (nil? %))
                             :password #(not (nil? %))}}
         :handler (fn [req]
                    {:status 303
                     :body (str (req))
                     :session (assoc-in
                               req
                               [:session :user-id]
                               ((account-actions/login (get-in req [:parameters :body])) :id))})}}]]]
    {:data {:middleware [wrap-session
                         wrap-params]}})))

(defn get-cookie []
  (second
   (clojure.string/split
    (first
     (get-in
      (app {:request-method :post
            :uri "/actions/login"
            :body-params {:username "foo" :password "bar"}})
      [:headers "Set-Cookie"]))
    #"=|;")))
(get-cookie)

(get-in
 (app {:request-method :post
       :uri "/actions/login"
       :body-params {:username "foo" :password "bar"}})
 [:headers "Set-Cookie"])
 
(app {:request-method :post :uri "/actions/login" :headers {"cookie" (str "ring-session=" "15660de2-06f3-4866-9a44-962b3e5cc5ce")} :body-params {:username "foo" :password "bar"}})
