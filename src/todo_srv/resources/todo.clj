(ns todo-srv.resources.todo
  (:require [liberator.core :refer [defresource]]
            [todo-srv.utils :refer [absolute-url]]))

(defn todo-resource-post! [list-id ctx]
  {:location (absolute-url (:request ctx)
                           (format "/lists/%s/todos/%s" list-id 1))})

(defresource todo-resource [list-id]
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :post! (hash-map :location "test"))
