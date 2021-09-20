(ns upgraded-winner.routes.media
  (:require [reitit.ring :refer [create-resource-handler]]
            [babashka.fs :as fs]
            [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [db]]
            [spec-tools.data-spec :as ds]
            [spec-tools.core :as st]
            [clojure.spec.alpha :as s]))

(def content-type-spec
  (st/spec
   {:name ::content-type
    :type :str
    :reason "Invalid content-type"
    :spec (fn [str] (some 
                    #(re-matches % str)
                    [#"image/.*"
                     #"audio/.*"
                     #"video/.*"]))}))

(def filename-spec
  (st/spec
   {:name ::filename
    :type :str
    :reason "Invalid filename"
    :spec (complement nil?)}))

(def tempfile-spec
  (st/spec
   {:name ::tempfile
    :reason "Tempfile not created"
    :spec #(= (type %) java.io.File)}))

(def media-spec
  (ds/spec
   {:name ::media
    
    :spec {:content-type content-type-spec
           :filename filename-spec
           :tempfile tempfile-spec
           :size pos-int?}}))


(hugsql/def-db-fns "queries/media.sql")

(defn post-handler [req]
  {:status 200
   :body (do (-> req
               :parameters
               :multipart
               :picture
               :tempfile
               (fs/move "resources/public/test2.txt" {:replace-existing true}))
             {:uri "test2.txt"})})



;; ================================================================================
;;                                     ROUTES
;; ================================================================================

(def route
  ["media"
   {:name ::media
    :post
    {:parameters {:multipart {:file {:content-type ::media-type}}}
     :handler post-handler}}])

