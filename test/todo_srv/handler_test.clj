(ns todo-srv.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [ring.util.request :refer [request-url]]
            [cheshire.core :refer [parse-string]]
            [todo-srv.database.utils :refer [clean-database]]
            [todo-srv.handler :refer :all]
            [todo-srv.models.list :as m])
  (:import [java.net URL]))

(use-fixtures :each clean-database)

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

(deftest test-list-endpoint
  (testing "POST /lists"
    (let [name "Test list"
          req (mock/request :post "/lists" {:name name})
          response (app req)
          id (response-get response "id")]
      (is (= (:status response) 201))
      (is (= (get-in response [:headers "Location"])
             (absolute-url req (format "/lists/%s" id))))
      (is (= (response-get response "name") name))
      (is (= id (:id (m/get-list id))))))

  (testing "POST /lists without name field"
    (let [response (app (mock/request :post "/lists"))]
      (is (= (:status response) 400))))

  (testing "GET /lists/:id"
    (let [init-resp (app (mock/request :post "/lists" {:name "test"}))
          id (response-get init-resp "id")
          get-uri (format "/lists/%s" id)
          response (app (mock/request :get get-uri))]
      (is (= (:status response) 200))
      (is (= (:body response) (:body init-resp)))
      (is (= id (:id (m/get-list id))))))

  (testing "DELETE /lists/:id"
    (let [init-resp (app (mock/request :post "/lists" {:name "test"}))
          id (response-get init-resp "id")
          resp (app (mock/request :delete (format "/lists/%s" id)))]
      (is (= (:status resp) 204))
      (is (= nil (m/get-list id))))))
