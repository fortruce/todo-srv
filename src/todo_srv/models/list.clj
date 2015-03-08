(ns todo-srv.models.list)

;; Temporary in-memory db
(defonce lists (atom {}))
(defonce counter (atom 0))

(defn create-list
  [name]
  (let [id (swap! counter inc)
        l {:name name
           :_id id}]
    (swap! lists conj [id l])
    l))
