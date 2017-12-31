(ns app.intro
  (:require [devcards.core :as rc :refer-macros [defcard]]
            [fulcro.client.dom :as dom]
            [fulcro.client.primitives :as prim :refer [defsc]]
            [app.ui.components :as comp]))

(defcard SVGPlaceholder
  "# SVG Placeholder"
  (comp/ui-placeholder {:w 200 :h 200}))

(defsc ThimgRenderer [this {:keys [name]}]
  (dom/div #js {:className "boo"}
    (dom/b nil
      (str "hi " name))))

(def thing-renderer (prim/factory ThimgRenderer))

(defcard my-card
  "# Stuff
  This is markdown..."
  (thing-renderer {:name "Ian"}))