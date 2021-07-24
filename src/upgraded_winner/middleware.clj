(ns upgraded-winner.middleware)

(defn wrap-create-session [handler]
  (fn [req]
    (let [response (handler req)]
      (if (contains? response :session)
        response
        (assoc response :session {})))))
