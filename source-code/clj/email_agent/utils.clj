
(ns email-agent.utils
    (:require [hiccup-converter.api :as hiccup-converter]
              [io.api               :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn acknowledge?
  ; @description
  ; Returns true if the message has been sent without errors.
  ;
  ; @param (*) n
  ;
  ; @usage
  ; (acknowledge? (send-message! {...} {...}))
  ; =>
  ; true
  ;
  ; @return (boolean)
  [{:keys [code]}]
  (zero? code))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sender-label
  ; @param (string) name
  ; @param (string) email-address
  ;
  ; @usage
  ; (sender-label "Sender" "sender@email.com")
  ;
  ; @usage
  ; (sender-label "Sender" "sender@email.com")
  ; =>
  ; "Sender <sender@email.com>"
  ;
  ; @return (string)
  [name email-address]
  (str name " <"email-address">"))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inline-file-body
  ; @note
  ; java.io.File objects and filepath strings are both accepted as 'content' parameter.
  ;
  ; @note
  ; If the 'content' parameter is provided as a filepath string and it has a valid extension,
  ; the 'mime-type' parameter is optional.
  ;
  ; @param (java.io.File object or string) content
  ; @param (string)(opt) mime-type
  ;
  ; @usage
  ; (inline-file-body "/my-file.pdf")
  ; =>
  ; {:content (java.io.File "/my-file.pdf") :content-type "application/pdf" :type :inline}
  ;
  ; @usage
  ; (inline-file-body "/my-file.pdf" "application/pdf")
  ; =>
  ; {:content (java.io.File "/my-file.pdf") :content-type "application/pdf" :type :inline}
  ;
  ; @usage
  ; (inline-file-body (java.io.File. "/my-file.pdf") "application/pdf")
  ; =>
  ; {:content (java.io.File "/my-file.pdf") :content-type "application/pdf" :type :inline}
  ;
  ; @return (map)
  ; {:content (java.io.File object)
  ;  :content-type (string)
  ;  :type (keyword)}
  ([content]
   (if (-> content string? not)
       (throw (Exception. (str "Pass MIME type or pass filepath as content!")))
       (let [mime-type (io/filepath->mime-type content)]
            (inline-file-body content mime-type))))

  ([content mime-type]
   (if (string? content)
       {:content (java.io.File. content) :content-type mime-type :type :inline}
       {:content content                 :content-type mime-type :type :inline})))

(defn attachment-file-body
  ; @note
  ; java.io.File objects and filepath strings are both accepted as 'content' parameter.
  ;
  ; @param (java.io.File object or string) content
  ;
  ; @usage
  ; (attachment-file-body "/my-file.pdf")
  ; =>
  ; {:content (java.io.File "/my-file.pdf") :type :attachment}
  ;
  ; @usage
  ; (attachment-file-body (java.io.File. "/my-file.pdf"))
  ; =>
  ; {:content (java.io.File "/my-file.pdf") :type :attachment}
  ;
  ; @return (map)
  ; {:content (java.io.File object)
  ;  :type (keyword)}
  [content]
  (if (string? content)
      {:content (java.io.File. content) :type :attachment}
      {:content content                 :type :attachment}))

(defn text-body
  ; @param (*) content
  ;
  ; @usage
  ; (text-body "My text")
  ; =>
  ; {:content "My text" :type "text/plain"}
  ;
  ; @return (map)
  ; {:content (string)
  ;  :type (string)}
  [content]
  {:type "text/plain" :content (str content)})

(defn hiccup-body
  ; @param (*) content
  ;
  ; @usage
  ; (hiccup-body [:html ...])
  ; =>
  ; {:content "<html>...</html>" :type "text/html"}
  ;
  ; @return (map)
  ; {:content (string)
  ;  :type (string)}
  [content]
  {:type "text/html" :content (hiccup-converter/to-html content)})

(defn html-body
  ; @param (*) content
  ;
  ; @usage
  ; (html-body "<html>...</html>")
  ; =>
  ; {:content "<html>...</html>" :type "text/html"}
  ;
  ; @return (map)
  ; {:content (string)
  ;  :type (string)}
  [content]
  {:type "text/html" :content (str content)})
