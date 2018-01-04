(ns app.mutations-part1
     (:require [devcards.core :as rc :refer-macros [defcard]]
               [fulcro.client.cards :refer [defcard-fulcro]]
               [fulcro.client.dom :as dom]
               [fulcro.client.primitives :as prim :refer [defsc]]
               [app.ui.components :as comp]))

(defsc TodoItem [this {:keys [db/id
                              item/label
                              item/complete?
                              ui/editing?] :as props}]
  {:initial-state {:db/id 1
                   :item/label "Buy stuff"
                   :item/complete? false
                   :ui/editing? true}}
       (dom/li nil
         (dom/input #js {:type "checkbox"
                         :checked complete?})
         (if editing?
           (dom/input #js {:type "text"
                           :value label})
           label)))

(defcard-fulcro active-todo-item-1
  "An active todo item with mutations"
  TodoItem
  {} ;; empty initial db
  {:inspect-data true})
