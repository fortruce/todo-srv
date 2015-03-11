(ns todo-srv.resources.todo-list
  (:require [todo-srv.models.todo-list :as m]
            [ring.util.request :refer [request-url]]
            [liberator.core :refer [defresource]])
  (:import [java.net URL]))

(defn absolute-url
  ([req] (absolute-url req "/"))
  ([req uri]
   (str (URL. (URL. (request-url req))
              (if (clojure.string/blank? uri) "/" uri)))))

(defn list-resource-malformed?
  [ctx]
  (let [name (get-in ctx [:request :params :name] nil)]
    (clojure.string/blank? name)))

(defn list-resource-post!
  [ctx]
  (let [l  (m/create-list (get-in ctx [:request :params :name]))]
    {:location (absolute-url (:request ctx)
                             (format "/lists/%s" (:_id l)))
     ::list l}))

(defn list-entry-exists?
  [id]
  (let [e (m/get-list id)]
               (if-not (nil? e)
                 {::entry e})))
  

(defresource list-resource
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :handle-created ::list
  :malformed? list-resource-malformed?
  :post! list-resource-post!)

(defresource list-entry [id]
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :exists? (fn [_] (list-entry-exists? id))
  :handle-ok ::entry)
