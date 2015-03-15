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
          l (create-list name)]
      (is (= (:name (first (sql/query db ["SELECT name FROM lists WHERE id = ?" (:id l)])))
            name)))))
