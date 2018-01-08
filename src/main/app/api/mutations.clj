(ns app.api.mutations
  (:require
    [taoensso.timbre :as timbre]
    [app.api.read :refer [todo-list-database]]
    [fulcro.server :refer [defmutation]]))

;; Place your server mutations here

(defmutation app.server-interactions/change-item-label [{:keys [id text] :as params}]
  (action [env]
    (timbre/info "Change label " params)
    (swap! todo-list-database assoc-in [:items id :item/label] text)))