(ns todo-srv.handler-spec
  (:require [speclj.core :refer :all]
            [todo-srv.handler :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :refer [parse-string]]
            [todo-srv.models.list :as m]
            [todo-srv.utils :refer [absolute-url]]
            [todo-srv.database.utils :refer [reset-db]]))

(defn- json-response-body
  [response]
  (parse-string (:body response)))

(defn- response-get
  [response key]
  (-> response
      json-response-body
      (get key)))

(describe "/lists"
  
  (after-all
    (reset-db))

  (with-all req (mock/request :post "/lists" {:name "test"}))
  (with-all resp (app @req))
  (with-all id (response-get @resp "id"))
  (with-all uri (format "/lists/%s" @id))

  (context "POST"
    
    (it "status 201"
        (should= 201 (:status @resp)))
    (it "sets Location header to absolute url"
        (should= (absolute-url @req @uri)
                 (get-in @resp [:headers "Location"])))
    (it "returns the resource in the body"
        (should= "test" (response-get @resp "name"))))

  (context "/:id"
    
    (context "GET"
      (with get-resp (app (mock/request :get @uri)))

      (it "status 200"
          (should= 200 (:status @get-resp)))
      (it "returns correct resource"
          (should= (:body @resp) (:body @get-resp))))

    (context "DELETE"
      (it "status 204"
          (should= 204 (:status (app (mock/request :delete @uri))))))))

(describe "/lists/:id/todos"

  (after-all (reset-db))

  (with-all test-list (m/create-list! "Test List"))
  (with-all list-id (:id @test-list))
  (with-all req (mock/request :post (format "/lists/%s/todos" @list-id)))
  (with-all resp (app @req))

  (context "POST"

    (it "status 201"
        (should= 201 (:status @resp)))
    (it "sets Location header"
        (should (get-in @resp [:headers "Location"]))
        (should= (absolute-url @req (format "/lists/%s/todos/%s" @list-id 1))
                 (get-in @resp [:headers "Location"])))))
