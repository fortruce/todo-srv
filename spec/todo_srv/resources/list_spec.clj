(ns todo-srv.resources.list-spec
  (:require [speclj.core :refer :all]
            [todo-srv.models.list :as m]
            [todo-srv.resources.list :as r :refer :all]))

(describe "absolute-url"
  (with request {:headers {"host" "localhost.com"}
                 :scheme "http"
                 :uri "/lists/test"})
  (it "creates abs url with host from request and uri from params"
      (should= "http://localhost.com/test"
               (absolute-url @request "/test")))
  (it "defaults to base url"
      (should= "http://localhost.com/"
               (absolute-url @request))
      (should= "http://localhost.com/"
               (absolute-url @request "/"))
      (should= "http://localhost.com/"
               (absolute-url @request ""))))

(describe "list-entry-exists?"

  (with-all! test-list {:id 1})
  
  (it "sets ::entry if it exists"
      (with-redefs [m/get-list (constantly (:id @test-list))]
        (should= (:id @test-list)
                 (::r/entry (list-entry-exists? (:id @test-list))))))

  (it "returns nil if doesn't exist"
      (with-redefs [m/get-list (constantly nil)]
        (should= nil (list-entry-exists? (:id @test-list))))))

(describe "list-resource-malformed?"

  (with-all! ctx (fn [n] {:request {:params {:name n}}}))

  (it "is malformed if :name is not present or empty"
      (should (list-resource-malformed? (@ctx "")))
      (should (list-resource-malformed? (@ctx " ")))
      (should (list-resource-malformed? {})))

  (it "is is well formed if :name is present"
      (should-not (list-resource-malformed? (@ctx "test")))))
