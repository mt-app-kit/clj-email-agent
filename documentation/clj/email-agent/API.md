
### email-agent.api

Functional documentation of the email-agent.api Clojure namespace

---

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > email-agent.api

### Index

- [acknowledge?](#acknowledge)

- [attachment-file-body](#attachment-file-body)

- [hiccup-body](#hiccup-body)

- [html-body](#html-body)

- [inline-file-body](#inline-file-body)

- [send-message!](#send-message)

- [sender-label](#sender-label)

- [text-body](#text-body)

---

### acknowledge?

```
@description
Returns true if the message has been sent without errors.
```

```
@param (*) n
```

```
@usage
(acknowledge? (send-message! {...} {...}))
```

```
@return (boolean)
```

<details>
<summary>Source code</summary>

```
(defn acknowledge?
  [{:keys [code]}]
  (zero? code))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [email-agent.api :refer [acknowledge?]]))

(email-agent.api/acknowledge? ...)
(acknowledge?                 ...)
```

</details>

---

### attachment-file-body

```
@description
The content could be a java.io.File object or a filepath string that will
be used as a source path for the File object.
```

```
@param (java.io.File object or string) content
```

```
@usage
(attachment-file-body "/my-file.pdf")
```

```
@usage
(attachment-file-body (java.io.File. "/my-file.pdf"))
```

```
@example
(attachment-file-body "/my-file.pdf")
=>
{:content (java.io.File "/my-file.pdf") :type :attachment}
```

```
@example
(attachment-file-body (java.io.File. "/my-file.pdf"))
=>
{:content (java.io.File "/my-file.pdf") :type :attachment}
```

```
@return (map)
{:content (java.io.File object)
 :type (keyword)}
```

<details>
<summary>Source code</summary>

```
(defn attachment-file-body
  [content]
  (if (string? content)
      {:content (java.io.File. content) :type :attachment}
      {:content content                 :type :attachment}))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [email-agent.api :refer [attachment-file-body]]))

(email-agent.api/attachment-file-body ...)
(attachment-file-body                 ...)
```

</details>

---

### hiccup-body

```
@param (*) content
```

```
@usage
(hiccup-body [:html ...])
```

```
@example
(hiccup-body [:html ...])
=>
{:content "<html>...</html>" :type "text/html"}
```

```
@return (map)
{:content (string)
 :type (string)}
```

<details>
<summary>Source code</summary>

```
(defn hiccup-body
  [content]
  {:type "text/html" :content (hiccup-converter/to-html content)})
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [email-agent.api :refer [hiccup-body]]))

(email-agent.api/hiccup-body ...)
(hiccup-body                 ...)
```

</details>

---

### html-body

```
@param (*) content
```

```
@usage
(html-body "<html>...</html>")
```

```
@example
(html-body "<html>...</html>")
=>
{:content "<html>...</html>" :type "text/html"}
```

```
@return (map)
{:content (string)
 :type (string)}
```

<details>
<summary>Source code</summary>

```
(defn html-body
  [content]
  {:type "text/html" :content (str content)})
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [email-agent.api :refer [html-body]]))

(email-agent.api/html-body ...)
(html-body                 ...)
```

</details>

---

### inline-file-body

```
@description
The content could be a java.io.File object or a filepath string that will
be used as a source path for the File object.
If you pass the content as a filepath string and it has a correct extension,
the mime-type parameter is optional.
```

```
@param (java.io.File object or string) content
@param (string)(opt) mime-type
```

```
@usage
(inline-file-body "/my-file.pdf")
```

```
@usage
(inline-file-body "/my-file.pdf" "application/pdf")
```

```
@usage
(inline-file-body (java.io.File. "/my-file.pdf") "application/pdf")
```

```
@example
(inline-file-body "/my-file.pdf")
=>
{:content (java.io.File "/my-file.pdf") :content-type "application/pdf" :type :inline}
```

```
@example
(inline-file-body "/my-file.pdf" "application/pdf")
=>
{:content (java.io.File "/my-file.pdf") :content-type "application/pdf" :type :inline}
```

```
@example
(inline-file-body (java.io.File. "/my-file.pdf") "application/pdf")
=>
{:content (java.io.File "/my-file.pdf") :content-type "application/pdf" :type :inline}
```

```
@return (map)
{:content (java.io.File object)
 :content-type (string)
 :type (keyword)}
```

<details>
<summary>Source code</summary>

```
(defn inline-file-body
  ([content]
   (if (-> content string? not)
       (throw (Exception. (str "Pass MIME type or pass filepath as content!")))
       (let [mime-type (io/filepath->mime-type content)]
            (inline-file-body content mime-type))))

  ([content mime-type]
   (if (string? content)
       {:content (java.io.File. content) :content-type mime-type :type :inline}
       {:content content                 :content-type mime-type :type :inline})))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [email-agent.api :refer [inline-file-body]]))

(email-agent.api/inline-file-body ...)
(inline-file-body                 ...)
```

</details>

---

### send-message!

```
@param (map)(opt) server-props
{:host (string)(opt)
 :password (string)(opt)
 :port (integer or string)(opt)
 :ssl (boolean)(opt)
  Default: true
 :tls (boolean)(opt)
 :username (string)(opt)}
@param (map) message-props
{:body (map, string or hiccups, keyword (as first item), maps or strings in vector)
 :from (string)(opt)
 :subject (string)(opt)
 :to (string)}
```

```
@usage
(send-message! {:host     "smtp.my-host.com"}
                :password "..."
                :username "my-user@my-host.com"
                :port     465}
               {:body     "Hello World!"
                :from     "sender@email.com"
                :subject  "Greatings"
                :to       "receiver@email.com"})
```

```
@usage
(send-message! {...}
               {:body "Hello World!" ...})
```

```
@usage
(send-message! {...}
               {:body [:html [:body [:div "Hello World!"]]] ...})
```

```
@usage
(send-message! {...}
               {:body (text-body "Hello World!") ...})
```

```
@usage
(send-message! {...}
               {:body ["Hello World!"
                       [:html [:body [:div "Hello World!"]]]
                       {:type "text/plain" :content "Hello World!"}
                       (text-body            "Hello World!")
                       (html-body            "<html><body>Hello World!</body></html>")
                       (hiccup-body          [:html [:body "Hello World!"]])
                       (attachment-file-body "my-file.pdf")
                       (attachment-file-body (java.io.File. "my-file.pdf"))
                       (inline-file-body     "my-file.pdf")
                       (inline-file-body     "my-file.pdf" "application/pdf")
                       (inline-file-body     (java.io.File. "my-file.pdf") "application/pdf")] ...})
```

```
@usage
(send-message! {...}
               {:body [:alternative
                       (text-body "Hello World!")
                       (html-body "<html><body>Hello World!</body></html>")] ...})
```

```
@return (map)
{:code (integer)
 :error (?)
 :message (?)}
```

<details>
<summary>Source code</summary>

```
(defn send-message!
  ([message-props]
   (send-message! {} message-props))

  ([server-props message-props]
   (and (v/valid? server-props  {:pattern* patterns/SERVER-PROPS-PATTERN  :prefix* "server-props"})
        (v/valid? message-props {:pattern* patterns/MESSAGE-PROPS-PATTERN :prefix* "message-props"})
        (let [server-props  (prototypes/server-props-prototype  server-props)
              message-props (prototypes/message-props-prototype message-props)]
             (postal.core/send-message {:host    (:host     server-props)
                                        :pass    (:password server-props)
                                        :port    (:port     server-props)
                                        :ssl     (:ssl      server-props)
                                        :tls     (:tls      server-props)
                                        :user    (:username server-props)}
                                       {:body    (:body     message-props)
                                        :from    (:from     message-props)
                                        :subject (:subject  message-props)
                                        :to      (:to       message-props)})))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [email-agent.api :refer [send-message!]]))

(email-agent.api/send-message! ...)
(send-message!                 ...)
```

</details>

---

### sender-label

```
@param (string) name
@param (string) email-address
```

```
@usage
(sender-label "Sender" "sender@email.com")
```

```
@example
(sender-label "Sender" "sender@email.com")
=>
"Sender <sender@email.com>"
```

```
@return (string)
```

<details>
<summary>Source code</summary>

```
(defn sender-label
  [name email-address]
  (str name " <"email-address">"))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [email-agent.api :refer [sender-label]]))

(email-agent.api/sender-label ...)
(sender-label                 ...)
```

</details>

---

### text-body

```
@param (*) content
```

```
@usage
(text-body "My text")
```

```
@example
(text-body "My text")
=>
{:content "My text" :type "text/plain"}
```

```
@return (map)
{:content (string)
 :type (string)}
```

<details>
<summary>Source code</summary>

```
(defn text-body
  [content]
  {:type "text/plain" :content (str content)})
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [email-agent.api :refer [text-body]]))

(email-agent.api/text-body ...)
(text-body                 ...)
```

</details>

---

<sub>This documentation is generated with the [clj-docs-generator](https://github.com/bithandshake/clj-docs-generator) engine.</sub>

