(ns todo-srv.resources.todo
  (:require [liberator.core :refer [defresource]]
            [todo-srv.utils :refer [absolute-url]]
            [todo-srv.models.todo :as t]))

(defn todo-resource-post! [list-id ctx]
  (let [todo (t/create-todo! list-id
                             (get-in ctx [:request :params :name]))]
    {:location (absolute-url (:request ctx)
                             (format "/lists/%s/todos/%s" list-id (:id todo)))
     ::todo todo}))

(defn todo-resource-malformed?
  [ctx]
  (when (#{:post} (get-in ctx [:request :request-method]))
    (when (clojure.string/blank? (get-in ctx [:request :params :name]))
      true)))

(defn todo-resource-exists?
  [list-id ctx]
  (when-let [todos (t/get-todos list-id)]
    {::todos todos}))

(defresource todo-resource [list-id]
  :allowed-methods [:post :get]
  :available-media-types ["application/json"]
  :post! (partial todo-resource-post! list-id)
  :malformed? todo-resource-malformed?
  :exists? (partial todo-resource-exists? list-id)
  :handle-ok ::todos
  :handle-created ::todo)
