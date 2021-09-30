(ns upgraded-winner.routes.jobs
  (:require [hugsql.core :as hugsql]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [upgraded-winner.db :refer [db]]
            [upgraded-winner.routes.post.comment :as comment]
            [upgraded-winner.routes.post.comments :as comments]
            [spec-tools.data-spec :as ds]
            [spec-tools.core :as st]))

(hugsql/def-db-fns "queries/jobs.sql")
(hugsql/def-sqlvec-fns "queries/jobs.sql")

(def id-param-spec
  (st/spec
   {:name ::id
    :spec pos-int?
    :reason "id must be a positive integer"}))

(def pic-param-spec
  (st/spec
   {:name ::pic
    :spec pos-int?
    :reason "pic-id must be a positive integer"}))

(def title-param-spec
  (st/spec
   {:name ::pic
    :spec #(and (string? %) (<= (count %) 50))
    :reason "title must be a string with length <= 50"}))

(def description-short-param-spec
  (st/spec
   {:name ::pic
    :spec #(and (string? %) (<= (count %) 100))
    :reason "description-short must be a string with length <= 100"}))

(def description-full-param-spec
  (st/spec
   {:name ::pic
    :spec string?
    :reason "description-full must be a string"}))



(defn post-handler [req]
  {:status 200
   :body (put-job db (assoc (-> req :parameters :body)
                            :usr
                            (-> req :session :identity)))})

(defn get-handler [req]
  {:status 200
   :body (get-job db {:id (-> req :parameters :path :job-id)})})

(defn get-jobs-handler [req]
  {:status 200
   :body (get-jobs db {:id (-> req :parameters :path :job-id)})})


(def route
  [""
   ["/jobs"
    {:name ::jobs
     :post {:parameters {:body {:pic pic-param-spec
                                :title title-param-spec
                                :description_short description-short-param-spec
                                :description_full description-full-param-spec}}
            :handler post-handler}
     :get { 
     :handler get-jobs-handler}}]
   ["/jobs/:job-id"
    {:name ::jobs-by-id
     :get {:parameters {:path {:job-id id-param-spec}}
           :handler get-handler}}]])