(ns todo-srv.resources.list
  (:require [todo-srv.models.list :as m]
            [ring.util.request :refer [request-url]]
            [todo-srv.utils :refer [absolute-url]]
            [liberator.core :refer [defresource]])
  (:import [java.net URL]))

(defn list-resource-malformed?
  [ctx]
  (let [name (get-in ctx [:request :params :name] nil)]
    (clojure.string/blank? name)))

(defn list-resource-post!
  [ctx]
  (let [l  (m/create-list! (get-in ctx [:request :params :name]))]
    {:location (absolute-url (:request ctx)
                             (format "/lists/%s" (:id l)))
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
  :allowed-methods [:get :delete]
  :available-media-types ["application/json"]
  :exists? (fn [_] (list-entry-exists? id))
  :handle-ok ::entry
  :delete! (fn [_] (m/delete-list! id)))
