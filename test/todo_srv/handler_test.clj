(ns todo-srv.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [ring.util.request :refer [request-url]]
            [cheshire.core :refer [parse-string]]
            [todo-srv.handler :refer :all])
  (:import [java.net URL]))

(defn- absolute-url [request location]
  (str (URL. (URL. (request-url request))
             location)))

(defn- json-response-body
  [response]
  (parse-string (:body response)))

(deftest test-list-endpoint
  (testing "POST /lists"
    (let [name "Test list"
          req (mock/request :post "/lists" {:name name})
          response (app req)
          body (-> response :body parse-string)]
      (is (= (:status response) 201))
      (is (= (get-in response [:headers "Location"])
             (absolute-url req (format "/lists/%s"
                                       (get body "_id")))))
      (is (= (get body "name")
             name))))

  (testing "POST /lists without name field"
    (let [response (app (mock/request :post "/lists"))]
      (is (= (:status response) 400))))

  (testing "GET /lists/:id"
    (let [init-resp (app (mock/request :post "/lists" {:name "test"}))
          body (json-response-body init-resp)
          id (get body "_id")
          get-uri (format "/lists/%s" id)
          response (app (mock/request :get get-uri))]
      (is (= (:status response) 200))
      (is (= (json-response-body response)
             body))
      (is (= (get (json-response-body response)
                  "name")
             "test")))))
