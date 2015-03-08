(ns todo-srv.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [todo-srv.routes.list :as list]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]))

(defroutes list-routes
  (context "/lists" []
           (POST "/" [name] (list/create-list name))))

(defroutes app-routes
  list-routes
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-response {:pretty true})
      (wrap-defaults api-defaults)))
