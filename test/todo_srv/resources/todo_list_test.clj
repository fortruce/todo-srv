(ns todo-srv.resources.todo-list-test
  (:require [clojure.test :refer :all]
            [todo-srv.resources.todo-list :refer :all]))

(deftest test-list-entry-exists?
  (with-redefs []))

(deftest test-list-resource-malformed?
  (let [ctx (fn [n] {:request {:params {:name n}}})]
   (testing "should be malformed if :name is not present or empty"
     (is (= true (list-resource-malformed? (ctx ""))))
     (is (= true (list-resource-malformed? (ctx " "))))
     (is (= true (list-resource-malformed? {}))))
   (testing "should NOT be malformed if :name is present"
     (is (= false (list-resource-malformed? (ctx "Name")))))))

(deftest test-list-resource-post!
  (let [id 1
        l {:_id id}]
    (with-redefs [create-list (fn [_] l)
                  absolute-url (fn [_ loc] loc)]
      (let [res (list-resource-post! {:request {:params {:name "Test List"}}})]
        (testing "should set :location to absolute url of resource"
          (is (= (format "/lists/%s" id)
                 (:location res))))
        (testing "should set ::list to newly created list"
          (is (= l
                 (:todo-srv.resources.todo-list/list res))))))))
