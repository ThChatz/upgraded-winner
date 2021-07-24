(ns upgraded-winner.mock
  (:require [upgraded-winner.handler :refer [app]]
            [clojure.string :refer [split]]))

(defn get-cookie []
  (let [req {:request-method :post
             :uri "/actions/login"
             :body-params {:username "foo" :password "bar"}}]
    (-> req
        app
        (get-in ,,, [:headers "Set-Cookie"])
        first
        (split ,,, #";")
        first)))

(def cookie (get-cookie))


(app {:request-method :post
      :uri "/actions/login"
      :headers {"cookie" cookie}
      :body-params {:username "foo" :password "bar"}})


(app {:request-method :post
      :uri "/actions/register"
      :headers {"cookie" cookie}
      :body-params {:username "foo" :password "bar" :email "foo@bar.org"}})
