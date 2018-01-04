(ns app.mutations-part1
     (:require [devcards.core :as rc :refer-macros [defcard]]
               [fulcro.client.cards :refer [defcard-fulcro]]
               [fulcro.client.dom :as dom]
               [fulcro.client.mutations :refer [defmutation]]
               [fulcro.client.primitives :as prim :refer [defsc]]
               [app.ui.components :as comp]))

(defmutation change-item-label [{:keys [text]}]
  (action [{:keys [state]}]
    (swap! state assoc :item/label text)))

(defmutation toggle-complete [{:keys [_]}]
  (action [{:keys [state]}]
    (swap! state update :item/complete? not)))

(defsc TodoItem [this {:keys [db/id
                              item/label
                              item/complete?
                              ui/editing?] :as props}]
  {:initial-state {:db/id 1
                   :item/label "Buy stuff"
                   :item/complete? false
                   :ui/editing? true}}
       (dom/li nil
         (dom/input #js {:type     "checkbox"
                         :onClick (fn [evt] (prim/transact! this `[(toggle-complete {})]))
                         :checked  complete?})
         (if editing?
           (dom/input #js {:type "text"
                           :onChange (fn [evt] (prim/transact! this `[(change-item-label {:text ~(.. evt -target -value)})]))
                           :value label})
           label)))

(defcard-fulcro active-todo-item-1
  "An active todo item with mutations"
  TodoItem
  {} ;; empty initial db
  {:inspect-data true})
