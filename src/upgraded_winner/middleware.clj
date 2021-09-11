(ns upgraded-winner.middleware
  (:require [upgraded-winner.xml-format :refer [xml-format]]
            [muuntaja.core :as m]))

(defn wrap-create-session [handler]
  (fn [req]
    (let [response (handler req)]
      (if (contains? response :session)
        response
        (assoc response :session {})))))

;; Muuntaja configuration

(def muuntaja-instance
  (m/create
   (-> m/default-options
       (assoc-in ,,, [:formats "application/xml"] xml-format))))
