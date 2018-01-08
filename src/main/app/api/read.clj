(ns app.api.read
  (:require
    [fulcro.client.primitives :as prim]
    [fulcro.server :refer [defquery-entity defquery-root]]
    [taoensso.timbre :as timbre]))

;; Server queries can go here

(def todo-list-database (atom
                          {:items {4 {:db/id 4 :item/label "D" :item/complete? false}
                                   5 {:db/id 5 :item/label "E" :item/complete? false}
                                   6 {:db/id 6 :item/label "F" :item/complete? true}}
                           :lists {1 {:db/id     1
                                      :list/name "My List"
                                      :list/items [[:items 4] [:items 5] [:items 6]]}}}))

(defquery-root :todo-list
  (value [{:keys [query] :as env} {:keys [id] :as params}]
    (timbre/info :fullQuery query)
    (let [data (prim/db->tree [{[:lists 1] query}] @todo-list-database @todo-list-database)
          result (get data [:lists 1])]
      (timbre/info result)
      result)))

