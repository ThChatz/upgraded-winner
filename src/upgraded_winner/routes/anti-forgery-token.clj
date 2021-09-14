(ns upgraded-winner.routes.anti-forgery-token
  (:require [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))




(defn get-handler [{{user-info :body} :parameters session :session}]
  {:status 200
   :body {:token *anti-forgery-token*}})


;; ================================================================================
;;                                     ROUTES
;; ================================================================================

(def route
  ["/anti-forgery-token"
   {:name ::anti-forgery-token
    :post
    {:parameters {:body {:password :upgraded-winner.routes.user/password
                         :email :upgraded-winner.routes.user/email}}
     :handler post-handler}
    :delete
    {:handler delete-handler}}])

