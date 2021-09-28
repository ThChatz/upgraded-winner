(ns upgraded-winner.routes.user.profile-pic
  (:require [hugsql.core :as hugsql]
            [clojure.spec.alpha :as s]
            [upgraded-winner.db :refer [db]]
            [babashka.fs :as fs]
            ))

;; (hugsql/def-db-fns "queries/user.profile_pic.sql")
;; (hugsql/def-db-fns "queries/media.sql")


;; (defn post-handler [req]
;;   {:status 200
;;    :body (do (-> req
;;                :parameters
;;                :multipart
;;                :picture
;;                :tempfile
;;                (fs/move "resources/public/test2.txt" {:replace-existing true}))
;;              {:uri "test2.txt"})})

;; (def route
;;   ["/profile-pic"
;;    {:name ::profile-pic
;;     :post {:parameters {:multipart
;;                         {:picture media-spec}}
;;            :handler post-handler}}])
