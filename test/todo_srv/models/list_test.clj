(ns todo-srv.models.list-test
  (:require [todo-srv.models.list :refer :all]
            [clojure.test :refer :all]))

(def db "postgresql://localhost:5432/todo-srv-test")
