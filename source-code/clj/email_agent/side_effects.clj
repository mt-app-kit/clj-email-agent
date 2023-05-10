
(ns email-agent.side-effects
    (:require [email-agent.patterns   :as patterns]
              [email-agent.prototypes :as prototypes]
              [email-agent.utils      :as utils]
              [postal.core            :as postal.core]
              [validator.api          :as v]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn send-message!
  ; @param (map)(opt) server-props
  ; {:host (string)(opt)
  ;  :password (string)(opt)
  ;  :port (integer or string)(opt)
  ;  :ssl (boolean)(opt)
  ;   Default: true
  ;  :tls (boolean)(opt)
  ;  :username (string)(opt)}
  ; @param (map) message-props
  ; {:body (map, string or hiccups, keyword (as first item), maps or strings in vector)
  ;  :from (string)(opt)
  ;  :subject (string)(opt)
  ;  :to (string)}
  ;
  ; @usage
  ; (send-message! {:host     "smtp.my-host.com"}
  ;                 :password "..."
  ;                 :username "my-user@my-host.com"
  ;                 :port     465}
  ;                {:body     "Hello World!"
  ;                 :from     "sender@email.com"
  ;                 :subject  "Greatings"
  ;                 :to       "receiver@email.com"})
  ;
  ; @usage
  ; (send-message! {...}
  ;                {:body "Hello World!" ...})
  ;
  ; @usage
  ; (send-message! {...}
  ;                {:body [:html [:body [:div "Hello World!"]]] ...})
  ;
  ; @usage
  ; (send-message! {...}
  ;                {:body (text-body "Hello World!") ...})
  ;
  ; @usage
  ; (send-message! {...}
  ;                {:body ["Hello World!"
  ;                        [:html [:body [:div "Hello World!"]]]
  ;                        {:type "text/plain" :content "Hello World!"}
  ;                        (text-body            "Hello World!")
  ;                        (html-body            "<html><body>Hello World!</body></html>")
  ;                        (hiccup-body          [:html [:body "Hello World!"]])
  ;                        (attachment-file-body "my-file.pdf")
  ;                        (attachment-file-body (java.io.File. "my-file.pdf"))
  ;                        (inline-file-body     "my-file.pdf")
  ;                        (inline-file-body     "my-file.pdf" "application/pdf")
  ;                        (inline-file-body     (java.io.File. "my-file.pdf") "application/pdf")] ...})
  ;
  ; @usage
  ; (send-message! {...}
  ;                {:body [:alternative
  ;                        (text-body "Hello World!")
  ;                        (html-body "<html><body>Hello World!</body></html>")] ...})
  ;
  ; @return (map)
  ; {:code ()
  ;  :error ()
  ;  :message ()}
  ([message-props]
   (send-message! {} message-props))

  ([server-props message-props]
   (and (v/valid? server-props  {:pattern* patterns/SERVER-PROPS-PATTERN  :prefix* "server-props"})
        (v/valid? message-props {:pattern* patterns/MESSAGE-PROPS-PATTERN :prefix* "message-props"})
        (let [server-props  (prototypes/server-props-prototype  server-props)
              message-props (prototypes/message-props-prototype message-props)]
             (try (postal.core/send-message {:host    (:host     server-props)
                                             :pass    (:password server-props)
                                             :port    (:port     server-props)
                                             :ssl     (:ssl      server-props)
                                             :tls     (:tls      server-props)
                                             :user    (:username server-props)}
                                            {:body    (:body     message-props)
                                             :from    (:from     message-props)
                                             :subject (:subject  message-props)
                                             :to      (:to       message-props)})
                  (catch Exception e (println e)))))))
