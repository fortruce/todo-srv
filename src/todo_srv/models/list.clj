(ns todo-srv.models.list)

;; Temporary in-memory db
(defonce lists (atom {}))
(defonce counter (atom 0))

(defn get-list
  [id]
  (get @lists id nil))

(defn create-list
  [name]
  (let [id (str (swap! counter inc))
        l {:name name
           :_id id}]
    (swap! lists conj [id l])
    l))
