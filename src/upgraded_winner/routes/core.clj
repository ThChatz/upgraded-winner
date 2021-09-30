(ns upgraded-winner.routes.core
  (:require [reitit.ring :refer [create-resource-handler]]
            [upgraded-winner.routes.user :as user]
            [upgraded-winner.routes.session :as session]
            [upgraded-winner.routes.anti-forgery-token :as anti-forgery-token]
            [upgraded-winner.routes.post :as post]
            [upgraded-winner.routes.jobs :as jobs]
            [upgraded-winner.routes.media :as media]
            [upgraded-winner.routes.network :as network]
            [upgraded-winner.routes.feed :as feed]
            [upgraded-winner.routes.install :as install]
            [upgraded-winner.routes.connections :as conn]
            [upgraded-winner.routes.conversations :as conv]))


(def route-tree
  [anti-forgery-token/route
   user/route
   session/route
   post/route
   media/route
   jobs/route
   network/route
   feed/route
   conn/route
   install/route
   conv/route
   ["/*" {:handler (create-resource-handler)}]])



