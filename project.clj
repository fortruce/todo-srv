(defproject todo-srv "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [liberator "0.12.2"]
                 [cheshire "5.4.0"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [ragtime "0.3.8"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [environ "1.0.0"]
                 [postgresql "9.3-1102.jdbc41"]]
  
  :plugins [[lein-ring "0.9.2"]
            [lein-environ "1.0.0"]
            [ragtime/ragtime.lein "0.3.8"]
            [clj-time "0.9.0"]]
  
  :ring {:handler todo-srv.handler/app
         :nrepl {:start? true
                 :port 3333}}
  
  :ragtime {:migrations ragtime.sql.files/migrations}
  
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]
         :ragtime {:database "jdbc:postgresql://localhost:5432/dev?user=postgres&password=password"}
         :env {:db-url "//localhost:5432/dev"
               :db-user "postgres"
               :db-pass "password"}}
   :test {:dependencies [[ring-mock "0.1.5"]]
          :ragtime {:database "jdbc:postgresql://localhost:5432/test?user=postgres&password=password"}
          :env {:db-url "//localhost:5432/test"
                :db-user "postgres"
                :db-pass "password"}}
   :production {:ragtime {:database (System/getenv "DB_URL")}}})
