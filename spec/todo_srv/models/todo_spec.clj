(ns todo-srv.models.todo-spec
  (:require [speclj.core :refer :all]
            [todo-srv.models.todo :refer :all]
            [todo-srv.models.list :as l]
            [todo-srv.database.utils :refer [reset-db]]
            [clojure.java.jdbc :as sql]
            [todo-srv.database :refer [db]]))

(describe "Todo Model"

  (after-all (reset-db))

  ;; Violates foreign key constraint if we don't have a list to belong to
  (before-all
    (def test-list (l/create-list! "test list"))
    (def todo1 (create-todo! 1 "todo1"))
    (def todo2 (create-todo! 1 "todo2")))

  (context "create-todo!"

    (it "creates todo in DB with 'name'"
        (should= "todo1"
                 (:name (first (sql/query db ["SELECT name FROM todos WHERE id = ?" (:id todo1)])))))

    (it "returns created todo"
        (should= "todo1" (:name todo1)))

    (it "creates todo with correct list id"
        (should= 1 (:lid todo1))))

  (context "get-todos"

    (it "gets matching todos"
        (should= (hash-set todo1 todo2)
                 (apply hash-set (get-todos 1))))
    (it "gets all todos with list-id"
        (should= 2 (count (get-todos 1))))))
