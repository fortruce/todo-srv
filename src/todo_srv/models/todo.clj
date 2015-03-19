(ns todo-srv.models.todo
  (:require [clojure.java.jdbc :as sql]
            [todo-srv.database :refer [db]]
            [todo-srv.utils :refer [convert-first convert-timestamps]]))

(defonce todo-table :todos)

(defn create-todo!
  [list-id name]
  (convert-first (sql/insert! db todo-table {:name name
                               :lid list-id})))

(defn get-todos
  [list-id]
  (map convert-timestamps (sql/query db ["SELECT * FROM todos WHERE lid = ?" list-id])))
