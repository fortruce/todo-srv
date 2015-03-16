(ns todo-srv.models.list-test
  (:require [todo-srv.models.list :refer :all]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as sql]
            [environ.core :refer [env]]
            [todo-srv.database :refer [db]]
            [todo-srv.database.utils :refer [clean-database]]))

(use-fixtures :each clean-database)

(deftest test-create-list
  (testing "should create list with 'name'"
    (let [name "my list"
          l (create-list! name)]
      (is (= (:name (first (sql/query db ["SELECT name FROM lists WHERE id = ?" (:id l)])))
            name)))))

(deftest test-get-list
  (testing "should get list with 'id'"
    (let [{id :id} (create-list! "test")]
      (is (= "test" (:name (get-list id))))))
  (testing "should return nil if no 'id' found"
    (is (nil? (get-list 100)))))

(deftest test-delete-list
  (testing "should delete list with 'id'"
    (let [{id :id} (create-list! "test")]
      (delete-list! id)
      (is (= nil (first (sql/query db ["SELECT * FROM lists WHERE id = ?" id])))))))
