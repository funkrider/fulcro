(ns app.server-interactions
  (:require [devcards.core :as rc :refer-macros [defcard]]
            [fulcro.client.cards :refer [defcard-fulcro]]
            [fulcro.client.dom :as dom]
            [fulcro.client.mutations :as m :refer [defmutation]]
            [fulcro.client.primitives :as prim :refer [defsc]]
            [fulcro.events :refer [enter-key?]]
            [app.ui.components :as comp]
            [fulcro.client.data-fetch :as df]))

(defn item-ident [id] [:todo-item/by-id id])
(defn item-path [id field] [:todo-item/by-id id field])
(defn change-item-label* [state id text]
  (assoc-in state (item-path id :item/label) text))

(defmutation change-item-label [{:keys [id text]}]
  (action [{:keys [state]}]
    (swap! state change-item-label* id text))
  (remote [env] true))

(defmutation toggle-complete [{:keys [id]}]
  (action [{:keys [state]}]
    (swap! state update-in [:todo-item/by-id id :item/complete?] not)))

(defmutation finish-editing [{:keys [id]}]
  (action [{:keys [state]}]
    (swap! state assoc-in [:todo-item/by-id id :ui/editing?] false)))



(defsc TodoItem [this {:keys [db/id
                              item/label
                              item/complete?
                              ui/editing?] :as props}]
  {:query [:db/id
           :item/label
           :item/complete?
           :ui/editing?]
   :ident [:todo-item/by-id :db/id]}

  (dom/li nil
    (dom/input #js {:type     "checkbox"
                    :onClick (fn [evt] (prim/transact! this `[(toggle-complete {:id ~id})]))
                    :checked  complete?})
    (if editing?
      (dom/input #js {:type      "text"
                      :onChange  (fn [evt] (prim/transact! this `[(change-item-label {:id ~id :text ~(.. evt -target -value)})]))
                      :onKeyDown (fn [evt]
                                   (if (enter-key? evt)
                                     (m/toggle! this :ui/editing?)))
                      :value     label})
      (dom/a #js {:onDoubleClick (fn [evt] (m/toggle! this :ui/editing?))} label))))

(def ui-todo-item (prim/factory TodoItem {:keyfn :db/id}))

(defsc TodoList [this {:keys [db/id list/name list/items]}]
  {:query         [:db/id :list/name
                   {:list/items (prim/get-query TodoItem)}]
   :ident         [:todo-list/by-id :db/id]}
  (dom/div nil
    (dom/h4 nil name)
    (mapv ui-todo-item items)))

(def ui-todo-list (prim/factory TodoList))

(defsc Root [this {:keys [ui/react-key root/todo-list]}]
  {:query         [:ui/react-key
                   {:root/todo-list (prim/get-query TodoList)}]}
  (dom/div #js {:key react-key}
    "TODO List..."
    (ui-todo-list todo-list)))

(defcard-fulcro queries-and-idents-1
  Root
  {} ;; empty initial db
  {:inspect-data true
   :fulcro       {:started-callback (fn [app]
                                      (df/load app :todo-list TodoList
                                        {:marker false
                                         :target [:root/todo-list]})
                                      (js/console.log :STARTED))}})
