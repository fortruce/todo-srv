(ns todo-srv.database
  (:require [environ.core :refer [env]]))

(defonce db {:classname "org.postgresql.Driver"
             :subprotocol "postgresql"
             :subname (env :db-url)
             :user (env :db-user)
             :password (env :db-pass)})
