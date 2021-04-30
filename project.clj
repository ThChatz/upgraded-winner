(defproject upgraded-winner "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [metosin/reitit "0.5.13"]
                 [hiccup "1.0.5"]
                 [ring-server "0.5.0"]
                 [ring/ring-json "0.5.1"]
                 [com.layerware/hugsql "0.5.1"]
                 [org.postgresql/postgresql "42.2.2"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler upgraded-winner.handler/app
         :init upgraded-winner.handler/init
         :destroy upgraded-winner.handler/destroy}
  :repl-options 
  {:host "0.0.0.0"
   :port 3001
   :init-ns upgraded-winner.repl}  
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring/ring-mock "0.4.0"] [ring/ring-devel "1.7.1"]]}
   :repl {:plugins [[cider/cider-nrepl "0.25.2"]]}}
  :main upgraded-winner.repl)
