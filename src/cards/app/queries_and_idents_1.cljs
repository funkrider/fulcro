(ns app.queries-and-idents-1
  (:require [devcards.core :as rc :refer-macros [defcard]]
            [fulcro.client.cards :refer [defcard-fulcro]]
            [fulcro.client.dom :as dom]
            [fulcro.client.mutations :refer [defmutation]]
            [fulcro.client.primitives :as prim :refer [defsc]]
            [fulcro.events :refer [enter-key?]]
            [app.ui.components :as comp]))



(defmutation change-item-label [{:keys [text]}]
  (action [{:keys [state]}]
    (swap! state assoc :item/label text)))

(defmutation toggle-complete [{:keys [_]}]
  (action [{:keys [state]}]
    (swap! state update :item/complete? not)))

(defmutation finish-editing [{:keys [_]}]
  (action [{:keys [state]}]
    (swap! state assoc :ui/editing? false)))



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
      (dom/input #js {:type      "text"
                      :onChange  (fn [evt] (prim/transact! this `[(change-item-label {:text ~(.. evt -target -value)})]))
                      :onKeyDown (fn [evt]
                                   (if (enter-key? evt)
                                     (prim/transact! this `[(finish-editing {})])))
                      :value     label})
      label)))

(def ui-todo-item (prim/factory TodoItem))

(defsc Root [this {:keys [ui/react-key todo/items]}]
  {:initial-state (fn [params]
                    {:todo/items [(prim/get-initial-state TodoItem
                                    {:id 1 :label "A"})
                                  (prim/get-initial-state TodoItem
                                    {:id 2 :label "B"})
                                  (prim/get-initial-state TodoItem
                                    {:id 3 :label "C"})]})}
  (dom/div #js {:key react-key}
    "TODO List..."))

(defcard-fulcro queries-and-itents-1
  Root
  {} ;; empty initial db
  {:inspect-data true})
