(ns upgraded-winner.routes.core
  (:require [reitit.ring :refer [create-resource-handler]]
            [upgraded-winner.routes.user :as user]
            [upgraded-winner.routes.session :as session]
            [upgraded-winner.routes.anti-forgery-token :as anti-forgery-token]
            [upgraded-winner.routes.post :as post]
            [upgraded-winner.routes.post.comment :as post.comment]
            [upgraded-winner.routes.post.comments :as post.comments]
            [upgraded-winner.routes.jobs :as jobs]
            [upgraded-winner.routes.media :as media]
            [upgraded-winner.routes.network :as network]))


(def route-tree
  [anti-forgery-token/route
   user/route
   session/route
   post/route
   media/route
   jobs/route
   network/route
   ["/*" {:handler (create-resource-handler)}]])


