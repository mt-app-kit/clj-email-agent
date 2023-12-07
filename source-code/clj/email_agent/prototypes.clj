
(ns email-agent.prototypes
    (:require [email-agent.utils :as utils]
              [fruits.hiccup.api :refer [hiccup?]]
              [fruits.mixed.api  :as mixed]
              [fruits.vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn server-props-prototype
  ; @ignore
  ;
  ; @param (map) server-props
  ; {:port (integer or string)
  ;  :tls (boolean)(opt)}
  ;
  ; @return (map)
  ; {:port (integer)
  ;  :ssl (boolean)}
  [{:keys [port tls] :as server-props}]
  (merge (if-not tls {:ssl true})
         (-> server-props)
         (if port {:port (mixed/to-number port)})))

(defn message-props-prototype
  ; @ignore
  ;
  ; @param (map) message-props
  ; {:body (hiccup, map, string or hiccups, keyword (as first item), maps or strings in vector)}
  ;
  ; @return (map)
  ; {:body (maps in vector)}
  [{:keys [body] :as message-props}]
  (letfn [; ...
          (body-part-f [%] (cond (map?     %) (-> %)
                                 (hiccup?  %) (-> % utils/hiccup-body)
                                 (string?  %) (-> % utils/text-body)
                                 (keyword? %) (-> %)))

          ; ...
          (body-f [%] (cond (vector? %) (vector/->items % body-part-f)
                            :convert    (body-f [%])))]

         ; ...
         (merge message-props {:body (body-f body)})))
