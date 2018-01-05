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
  {:query [:db/id
           :item/label
           :item/complete?
           :ui/editing?]
   :ident [:todo-item/by-id :db/id]
   :initial-state (fn [{:keys [id label]}]
                    {:db/id          id
                     :item/label     label
                     :item/complete? false
                     :ui/editing?    false})}
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

(defsc TodoList [this {:keys [db/id list/name list/items]}]
  {:query         [:db/id :list/name
                   {:list/items (prim/get-query TodoItem)}]
   :ident         [:todo-list/by-id :db/id]
   :initial-state (fn [{:keys [id name]}]
                    {:db/id      id
                     :list/name  name
                     :list/items [(prim/get-initial-state TodoItem
                                    {:id 1 :label "A"})
                                  (prim/get-initial-state TodoItem
                                    {:id 2 :label "B"})
                                  (prim/get-initial-state TodoItem
                                    {:id 3 :label "C"})]})}
  (dom/div nil
    (dom/h4 nil name)
    (mapv ui-todo-item items)))

(def ui-todo-list (prim/factory TodoList))

(defsc Root [this {:keys [ui/react-key root/todo-list]}]
  {:query         [:ui/react-key
                   {:root/todo-list (prim/get-query TodoList)}]
   :initial-state (fn [params]
                    {:root/todo-list (prim/get-initial-state TodoList
                                       {:id 1 :name "My List"})})}
  (dom/div #js {:key react-key}
    "TODO List..."
    (ui-todo-list todo-list)))

(defcard-fulcro queries-and-idents-1
  Root
  {} ;; empty initial db
  {:inspect-data true})
