(ns todo-srv.resources.todo-list
  (:require [todo-srv.models.todo-list :as todo-list]
            [ring.util.request :refer [request-url]]
            [liberator.core :refer [defresource]])
  (:import [java.net URL]))

(defn absolute-url [req loc]
  (str (URL. (URL. (request-url req))
             loc)))

(defn create-list [name]
  (todo-list/create-list name))

(defn get-list-entry [id]
  (todo-list/get-list id))

(defn list-resource-malformed?
  [ctx]
  (let [name (get-in ctx [:request :params :name] nil)]
    (clojure.string/blank? name)))

(defn list-resource-post!
  [ctx]
  (let [l  (create-list (get-in ctx [:request :params :name]))]
    {:location (absolute-url (:request ctx)
                             (format "/lists/%s" (:_id l)))
     ::list l}))
  

(defresource list-resource
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :handle-created ::list
  :malformed? list-resource-malformed?
  :post! list-resource-post!)

(defresource list-entry [id]
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :exists? (fn [_]
             (let [e (get-list-entry id)]
               (if-not (nil? e)
                 {::entry e})))
  :handle-ok ::entry)
