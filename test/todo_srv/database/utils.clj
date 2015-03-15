(ns todo-srv.database.utils
  (:require [clojure.java.jdbc :as sql]
            [todo-srv.database :refer [db]]))

(defn- clear-table [tname]
  (sql/execute! db [(str "DELETE FROM " tname)]))

(defn- reset-sequence [sname]
  (sql/execute! db [(str "ALTER SEQUENCE " sname " RESTART WITH 1")]))

(defn- reset-db []
  (dorun (map clear-table ["lists" "todos"]))
  (dorun (map reset-sequence ["lid_serial" "tid_serial"])))

(defn clean-database [f]
  (reset-db)
  (f)
  (reset-db))
