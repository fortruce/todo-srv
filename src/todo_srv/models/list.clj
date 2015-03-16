(ns todo-srv.models.list
  (:require [clojure.java.jdbc :as sql]
            [todo-srv.database :refer [db]]
            [clj-time.coerce :as c]))

(defn- convert-timestamps
  [res]
  (reduce #(update-in %1 [%2] (comp str c/from-sql-time)) res [:updated_at :created_at]))

(defn- convert-first
  [query]
  (some-> query
      first
      convert-timestamps))

(defonce ^:private lists-table :lists)

(defn get-list
  [id]
  (convert-first (sql/query db ["SELECT * FROM lists WHERE id = ? LIMIT 1" id])))

(defn create-list!
  [name]
  (convert-first (sql/insert! db lists-table {:name name})))

(defn delete-list!
  [id]
  (sql/delete! db lists-table ["id = ?" id]))
