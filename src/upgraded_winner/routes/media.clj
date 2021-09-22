(ns upgraded-winner.routes.media
  (:require [reitit.ring :refer [create-file-handler]]
            [babashka.fs :as fs]
            [hugsql.core :as hugsql]
            [upgraded-winner.db :refer [db]]
            [spec-tools.data-spec :as ds]
            [spec-tools.core :as st]
            [clojure.spec.alpha :as s]))


(def media-id-spec
  (st/spec
   {:name ::media-id
    :reason "Media ids are positive integers"}))

(def media-spec-template
  {:name ::media
   :spec {:content-type (constantly nil)
          :filename (constantly nil)
          :tempfile tempfile-spec
          :size pos-int?}})

(def content-type-image-spec
  (st/spec
   {:name ::content-type
    :type :str
    :reason "Invalid image content-type"
    :spec #{"image/jpeg" "image/png"}}))

(def filename-image-spec
  (st/spec
   {:name ::content-type
    :type :str
    :reason "Invalid image format extension"
    :spec #(#{"jpg" "jpeg" "jfif" "pjpeg" "pjp" "png"} (fs/extension %))}))



(def tempfile-spec
  (st/spec
   {:name ::tempfile
    :reason "Tempfile not created"
    :spec #(= (type %) java.io.File)}))

(def content-type-audio-spec
  (st/spec
   {:name ::content-type
    :type :str
    :reason "Invalid audio content-type"
    :spec #{"audio/mpeg" "audio/mp4" "audio/wav" "audio/wave" "audio/x-wav" "audio/x-pn-wav"}}))

(def filename-audio-spec
  (st/spec
   {:name ::content-type
    :type :str
    :reason "Invalid audio format extension"
    :spec #(#{"mp3" "mp4" "wav"} (fs/extension %))}))

(def content-type-video-spec
  (st/spec
   {:name ::content-type
    :type :str
    :reason "Invalid audio content-type"
    :spec #{"video/mp4"}}))

(def filename-video-spec
  (st/spec
   {:name ::content-type
    :type :str
    :reason "Invalid audio format extension"
    :spec #(#{"mp4"} (fs/extension %))}))

(def audio-upload-spec
  (ds/spec
   (-> media-spec-template
       (assoc :name ::audio)
       (assoc-in [:spec :content-type] content-type-audio-spec)
       (assoc-in [:spec :filename] filename-audio-spec))))

(def video-upload-spec
  (ds/spec
   (-> media-spec-template
       (assoc :name ::video)
       (assoc-in [:spec :content-type] content-type-video-spec)
       (assoc-in [:spec :filename] filename-video-spec))))

(def image-upload-spec
  (ds/spec
   (-> media-spec-template
       (assoc :name ::image)
       (assoc-in [:spec :content-type] content-type-image-spec)
       (assoc-in [:spec :filename] filename-image-spec))))



(hugsql/def-db-fns "queries/media.sql")

(hugsql/def-sqlvec-fns "queries/media.sql")

(defn make-filename [tempfile-name media-id]
  (str media-id "." (fs/extension tempfile-name)))


(defn tempfile->media [media-root-dir tempfile type]
  (let [{id :id} (add-media db {:type type})
        media-path (make-filename tempfile id)]
    (do
      (fs/move tempfile (str media-root-dir "/" media-path))
      id)))


(def media-root "resources/public")

(defn make-post-handler [media-root type]
  (fn [req]
    (tempfile->media media-root
                     (-> req :parameters :multipart :file :tempfile)
                     type)))


;; ================================================================================
;;                                     ROUTES
;; ================================================================================

(def route
  ["/media"
   ["/image"
    {:name ::image-upload
     :post
     {:parameters {:multipart {:file image-upload-spec}}
      :handler (make-post-handler media-root :photo)}}]
   ["/video"
    {:name ::video-upload
     :post
     {:parameters {:multipart {:file video-upload-spec}}
      :handler (make-post-handler media-root :video)}}]
   ["/audio"
    {:name ::audio-upload
     :post
     {:parameters {:multipart {:file audio-upload-spec}}
      :handler (make-post-handler media-root :audio)}}]
   ["/:media-id"
    {:name ::media-by-id
     :get
     {:parameters {:path {:media-id pos-int?}}
      :handler (fn [req]
                 {:status 307
                  :body (:filename (get-media db {:id (-> req
                                                          :parameters
                                                          :path
                                                          :media-id)}))})}}]])

