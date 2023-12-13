
(ns email-agent.tests
    (:require [fruits.hiccup.api :refer [hiccup?]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @description
; https://github.com/bithandshake/cljc-validator
;
; @constant (map)
(def SERVER-PROPS-TEST
     {:host     {:opt* true
                 :f*   string?
                 :not* empty?
                 :e*   ":host must be a nonempty string!"}
      :password {:opt* true
                 :f*   string?
                 :not* empty?
                 :e*   ":password must be a nonempty string!"}
      :port     {:opt* true
                 :or*  [integer? string?]
                 :e*   ":port must be an integer or string!"}
      :ssl      {:opt* true}
      :tls      {:opt* true}
      :username {:opt* true
                 :f*   string?
                 :not* empty?
                 :e*   ":username must be a nonempty string!"}})

; @ignore
;
; @description
; https://github.com/bithandshake/cljc-validator
;
; @constant (map)
(def MESSAGE-PROPS-TEST
     {:body    {:not* empty?
                :or*  [map? string? hiccup? vector?]
                :e*   ":body must be a nonempty map, a string, a hiccup or a vector!"}
      :from    {:f*   string?
                :not* empty?
                :e*   ":to must be a nonempty string!"}
      :subject {:opt* true}
      :to      {:f*   string?
                :not* empty?
                :e*   ":to must be a nonempty string!"}})
