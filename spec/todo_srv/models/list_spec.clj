(ns todo-srv.models.list-spec
  (:require [todo-srv.models.list :refer :all]
            [speclj.core :refer :all]
            [clojure.java.jdbc :as sql]
            [todo-srv.database :refer [db]]
            [todo-srv.database.utils :refer [reset-db]]))

(describe "List Model"


  (after-all (reset-db))

  (with! test-list (create-list! "test"))
  
  (context "create-list!"
           (it "should return newly created list"
               (should= "test" (:name @test-list)))

           (it "creates list with name in db"
               (should= "test" (:name (first (sql/query db ["SELECT * FROM lists"]))))))

  (context "get-list"
           (it "returns list if exists"
               (should= @test-list (get-list (:id @test-list))))
           (it "returns nil if doesn't exist"
               (should= nil (get-list 100))))

  (context "delete-list!"
           (it "deletes list from db"
               (delete-list! (:id @test-list))
               (should= nil (first (sql/query db ["SELECT * FROM lists WHERE id = ?"
                                                  (:id @test-list)]))))))

