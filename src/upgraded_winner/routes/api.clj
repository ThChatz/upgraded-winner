(ns upgraded-winner.routes.api
  (:require [compojure.core :refer :all]
            [ring.middleware.params :refer [wrap-params]]
            [upgraded-winner.views.layout :as layout]))

(defn api [func foo]
  (str func foo))

(defn print-request [handler]
  (fn [request]
    (let [response (handler request)]
      (println request)
      response)))


(defroutes api-routes
  (print-request (GET "/:func" [func foo] (api func foo))))
