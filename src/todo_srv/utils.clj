(ns todo-srv.utils
  (:require [ring.util.request :refer [request-url]])
  (:import [java.net URL]))

(defn absolute-url
  ([req] (absolute-url req "/"))
  ([req uri]
   (str (URL. (URL. (request-url req))
              (if (clojure.string/blank? uri) "/" uri)))))
