(ns todo-srv.utils
  (:require [ring.util.request :refer [request-url]]
            [clj-time.coerce :as c])
  (:import [java.net URL]))

(defn convert-timestamps
  [res]
  (reduce #(update-in %1 [%2] (comp str c/from-sql-time))
          res
          [:updated_at :created_at]))

(defn convert-first
  [query]
  (some-> query
      first
      convert-timestamps))

(defn absolute-url
  ([req] (absolute-url req "/"))
  ([req uri]
   (str (URL. (URL. (request-url req))
              (if (clojure.string/blank? uri) "/" uri)))))
