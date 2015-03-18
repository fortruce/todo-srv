(ns todo-srv.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [todo-srv.resources.list :as list]
            [todo-srv.resources.todo :as todo]
            [liberator.dev :refer [wrap-trace]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]))

(defroutes list-routes
  (ANY "/lists" [] list/list-resource)
  (ANY "/lists/:id" [id] (list/list-entry (Integer/parseInt id))))

(defroutes todo-routes
  (ANY "/lists/:list-id/todos" [list-id] (todo/todo-resource (Integer/parseInt list-id))))

(defroutes app-routes
  list-routes
  todo-routes
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-trace :header :ui)
      (wrap-defaults api-defaults)))
