(ns todo-srv.resources.todo
  (:require [liberator.core :refer [defresource]]
            [todo-srv.utils :refer [absolute-url]]
            [todo-srv.models.todo :as t]))

(defn todo-resource-post! [list-id ctx]
  (let [todo (t/create-todo! list-id
                             (get-in ctx [:request :params :name]))]
    {:location (absolute-url (:request ctx)
                             (format "/lists/%s/todos/%s" list-id (:id todo)))}))

(defresource todo-resource [list-id]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :post! (partial todo-resource-post! list-id))
