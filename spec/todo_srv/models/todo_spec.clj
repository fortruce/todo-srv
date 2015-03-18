(ns todo-srv.models.todo-spec
  (:require [speclj.core :refer :all]
            [todo-srv.models.todo :refer :all]
            [todo-srv.models.list :as l]
            [todo-srv.database.utils :refer [reset-db]]
            [clojure.java.jdbc :as sql]
            [todo-srv.database :refer [db]]))

(describe "Todo Model"

  (after-all (reset-db))

  ;; Violates foreing key constraint if we don't have a list to belong to
  (before-all (l/create-list! "test list"))

  (context "create-todo!"

    (with-all test-todo (create-todo! 1 "mytodo"))

    (it "creates todo in DB with 'name'"
        (should= "mytodo"
                 (:name (first (sql/query db ["SELECT name FROM todos WHERE id = ?" (:id @test-todo)])))))

    (it "returns created todo"
        (should= "mytodo" (:name @test-todo)))

    (it "creates todo with correct list id"
        (should= 1 (:lid @test-todo)))))
