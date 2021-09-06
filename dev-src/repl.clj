(ns upgraded-winner.repl
  (:require [upgraded-winner.handler :refer [app init destroy]]
            [ring.server.standalone :refer [serve]]
            [ring.middleware file-info file]))

(defonce server (atom nil))

(defn start-server
  "used for starting the server in development mode from REPL"
  [& [port]]
  (let [port (if port (Integer/parseInt port) 3000)]
    (reset! server
            (serve app
                   {:port port
                    :init init
                    :auto-reload? true
                    :open-browser? false
                    :destroy destroy
                    :join true}))
    (println (str "You can view the site at http://localhost:" port))))

(defn stop-server []
  (.stop @server)
  (reset! server nil))
