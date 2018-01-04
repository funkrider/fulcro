(ns app.intro
  (:require [devcards.core :as rc :refer-macros [defcard]]
            [fulcro.client.cards :refer [defcard-fulcro]]
            [fulcro.client.dom :as dom]
            [fulcro.client.primitives :as prim :refer [defsc]]
            [app.ui.components :as comp]))

(defsc TodoItem [this {:keys [db/id item/label item/complete? ui/editing?] :as props}]
  (dom/li nil
    (dom/input #js {:type "checkbox"
                    :checked complete?})
    (if editing?
      (dom/input #js {:type "text"
                      :value label})
      label)))

(def ui-todo-item (prim/factory TodoItem))

(defcard todo-item-unchecked
  "An un-checked todo item"
  (ui-todo-item {:db/id 1 :item/label "Buy Milk" :item/complete? false}))

(defcard todo-item-checked
  "An checked todo item"
  (ui-todo-item {:db/id 2 :item/label "Buy Milk" :item/complete? true}))

(defcard todo-item-checked-editing
  "An checked todo item"
  (ui-todo-item {:db/id 3 :item/label "Buy Milk" :item/complete? true :ui/editing? true}))

#_(defsc Root [this {:keys [name] :as props}]
  {:initial-state {:name "Ianx"}}
  (js/console.log props)
  (dom/div nil
    (str "hi " name)))

#_(defcard-fulcro my-card
  Root
  {} ;; Default data
  {:inspect-data true :name "Ian"})