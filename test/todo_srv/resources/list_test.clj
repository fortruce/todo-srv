(ns todo-srv.resources.list-test
  (:require [clojure.test :refer :all]
            [todo-srv.models.list :as m]
            [todo-srv.resources.list :as r :refer :all]))

(deftest test-absolute-url
  (testing "should concat loc to host url found in request"
    (let [request {:headers {"host" "localhost.com"}
                   :scheme "http"
                   :uri "/lists/test"}]
      (is (= "http://localhost.com/test"
             (absolute-url request "/test")))
      (is (= "http://localhost.com/"
             (absolute-url request))))))

(deftest test-list-entry-exists?
  (testing "should set ::entry to entry if exists"
    (let [l {:_id 1}]
      (with-redefs [m/get-list (constantly l)]
        (is (= (::r/entry (list-entry-exists? 1))
               l)))))
  (testing "should return nil if entry doesn't exist"
    (with-redefs [m/get-list (constantly nil)]
      (is (= nil (list-entry-exists? 1))))))

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
    (with-redefs [m/create-list (constantly l)
                  absolute-url (fn [_ loc] loc)]
      (let [res (list-resource-post! {:request {:params {:name "Test List"}}})]
        (testing "should set :location to absolute url of resource"
          (is (= (format "/lists/%s" id)
                 (:location res))))
        (testing "should set ::list to newly created list"
          (is (= l
                 (::r/list res))))))))
