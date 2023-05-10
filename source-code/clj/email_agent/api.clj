
(ns email-agent.api
    (:require [email-agent.side-effects :as side-effects]
              [email-agent.utils        :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; email-agent.side-effects
(def send-message! side-effects/send-message!)

; email-agent.utils
(def acknowledge?         utils/acknowledge?)
(def sender-label         utils/sender-label)
(def inline-file-body     utils/inline-file-body)
(def attachment-file-body utils/attachment-file-body)
(def text-body            utils/text-body)
(def hiccup-body          utils/hiccup-body)
(def html-body            utils/html-body)
