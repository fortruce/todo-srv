(ns todo-srv.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [todo-srv.handler :refer :all]))

(comment (deftest test-app
   (testing "main route"
     (let [response (app (mock/request :get "/"))]
       (is (= (:status response) 200))
       (is (= (:body response) "Hello World"))))

   (testing "not-found route"
     (let [response (app (mock/request :get "/invalid"))]
       (is (= (:status response) 404))))))

(deftest test-list-endpoint
  (testing "POST /lists"
    (let [response (app (mock/request :post "/lists" {:name "Test List"}))]
      (is (= (:status response) 201))
      (is (= (get-in response [:headers "Location"]) "test")))))
