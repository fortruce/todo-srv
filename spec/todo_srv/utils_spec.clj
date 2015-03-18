(ns todo-srv.utils-spec
  (:require [speclj.core :refer :all]
            [todo-srv.utils :refer :all]))

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
