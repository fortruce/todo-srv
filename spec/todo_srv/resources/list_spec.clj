(ns todo-srv.resources.list-spec
  (:require [speclj.core :refer :all]
            [todo-srv.models.list :as m]
            [todo-srv.utils :refer [absolute-url]]
            [todo-srv.resources.list :as r :refer :all]))

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

(describe "list-resource-post!"

  (with-all! test-list {:id 1})
  (with-all! ctx {:request {:params {:name "test"}}})

  (around [it]
          (with-redefs [m/create-list! (constantly @test-list)
                        absolute-url #(str %2)]
            (it)))

  (it "sets :location to absolute url of resource"
      (should= "/lists/1" (:location (list-resource-post! @ctx))))
  (it "sets ::list to newly created list"
      (should= @test-list (::r/list (list-resource-post! @ctx)))))
