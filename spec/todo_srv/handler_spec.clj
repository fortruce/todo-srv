(ns todo-srv.handler-spec
  (:require [speclj.core :refer :all]
            [todo-srv.handler :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :refer [parse-string]]
            [ring.util.request :refer [request-url]]
            [todo-srv.database.utils :refer [reset-db]])
  (:import [java.net URL]))

(defn- absolute-url [request location]
  (str (URL. (URL. (request-url request))
             location)))

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

  (context "POST /lists"
           (it "status 201"
               (should= 201 (:status @resp)))
           (it "sets Location header to absolute url"
               (should= (absolute-url @req @uri)
                        (get-in @resp [:headers "Location"])))
           (it "returns the resource in the body"
               (should= "test"
                        (response-get @resp "name"))))

  (context "GET /lists/:id"

           (with get-resp (app (mock/request :get @uri)))

           (it "status 200"
               (should= 200 (:status @get-resp)))
           (it "returns correct resource"
               (should= (:body @resp)
                        (:body @get-resp))))

  (context "DELETE /lists/:id"

           (with del-resp (app (mock/request :delete @uri)))
           (it "status 204"
               (should= 204 (:status @del-resp)))))
