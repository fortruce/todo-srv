(ns todo-srv.routes.list
  (:require [todo-srv.models.list :as list]))

(defn post-response [body resource-uri]
  {:status 201
   :headers {"Location" resource-uri}
   :body body})

(defn create-list [name]
  (let [l (list/create-list name)]
   (post-response l (str "/lists/" (:_id l)))))


