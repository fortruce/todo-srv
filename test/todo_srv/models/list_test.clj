(ns todo-srv.models.list-test
  (:require [todo-srv.models.list :refer :all]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as sql]
            [environ.core :refer [env]]))

(def db {:classname "org.postgresql.Driver"
         :subprotocol "postgresql"
         :subname (env :db-url)
         :user (env :db-user)
         :password (env :db-pass)})

(defn clean-database [f]
  (f)
  (sql/execute! db ["DELETE FROM lists"]))

(use-fixtures :each clean-database)

(deftest test-create-list
  (testing "should create list with 'name'"
    (let [name "my list"
          l (create-list name)]
     (is (= (sql/query db ["SELECT name FROM lists WHERE id = ?" (:_id l)])
            name)))))
