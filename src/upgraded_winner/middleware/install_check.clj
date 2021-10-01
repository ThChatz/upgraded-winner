(ns upgraded-winner.middleware.install-check
  (:require [clojure.java.jdbc :as jdbc]
            [upgraded-winner.db :refer [db]]))


(def ^{:private true} *installed?* (atom false))

(def response-not-installed
  "Response to send when application is not installed"
  {:status 503
   :body {:cause "Application not Installed"
          :message "Please install the application to continue"}})

(def not-installed-exception-regex
  "Regex that matches 'database' does not exist exception"
  #"org.postgresql.util.PSQLException: FATAL: database \".*\" does not exist")

(defn installed-check-catcher []
  (try
    (do (jdbc/db-do-prepared db "") true)
    (catch Exception e
      (if ((complement nil?)
           (re-matches
            not-installed-exception-regex
            (str e))) false))))



(defn wrap-install-check [handler]
  (fn [req]
    (cond (and (not @*installed?*)
               (not (installed-check-catcher))
               (and (not= (:uri req) "/install"))
               (and (not= (:uri req) "/anti-forgery-token")))
          response-not-installed
          (not *installed?*)
          (do (swap! *installed?* (constantly true))
              (handler req))
          :else (handler req))))


