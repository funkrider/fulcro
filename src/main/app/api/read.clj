(ns app.api.read
  (:require
    [fulcro.server :refer [defquery-entity defquery-root]]
    [taoensso.timbre :as timbre]))

;; Server queries can go here

(defquery-root :todo-list
  (value [env params]
    {:db/id 1
     :list/name "A list"
     :list/items [{:db/id 4 :item/label "D" :item/complete? false}
                  {:db/id 5 :item/label "E" :item/complete? false}
                  {:db/id 6 :item/label "F" :item/complete? true}]}))

