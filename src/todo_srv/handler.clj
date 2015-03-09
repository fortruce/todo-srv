(ns todo-srv.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [todo-srv.resources.todo-list :as todo-list]
            [liberator.dev :refer [wrap-trace]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]))

(defroutes list-routes
  (ANY "/lists" [] todo-list/list-resource)
  (ANY "/lists/:id" [id] (todo-list/list-entry id)))

(defroutes app-routes
  list-routes
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-trace :header :ui)
      (wrap-defaults api-defaults)))
