(defproject todo-srv "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [liberator "0.12.2"]
                 [cheshire "5.4.0"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-defaults "0.1.2"]]
  :plugins [[lein-ring "0.9.2"]
            [quickie "0.3.6"]]
  :ring {:handler todo-srv.handler/app
         :nrepl {:start? true
                 :port 3333}}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
