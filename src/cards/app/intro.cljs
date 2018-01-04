(ns app.intro
  (:require [devcards.core :as rc :refer-macros [defcard]]
            [fulcro.client.cards :refer [defcard-fulcro]]
            [fulcro.client.dom :as dom]
            [fulcro.client.primitives :as prim :refer [defsc]]
            [app.ui.components :as comp]))

(defsc Root [this {:keys [name] :as props}]
  {:initial-state {:name "Ianx"}}
  (js/console.log props)
  (dom/div nil
    (str "hi " name)))

(defcard-fulcro my-card
  Root
  {} ;; Default data
  {:inspect-data true :name "Ian"})