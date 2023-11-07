
# clj-email-agent

### Overview

The <strong>clj-email-agent</strong> is a simple email agent for Clojure projects
based on the [dewr / postal] library.

### deps.edn

```
{:deps {bithandshake/clj-email-agent {:git/url "https://github.com/bithandshake/clj-email-agent"
                                      :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}}
```

### Current version

Check out the latest commit on the [release branch](https://github.com/bithandshake/clj-email-agent/tree/release).

### Documentation

The <strong>clj-email-agent</strong> functional documentation is [available here](documentation/COVER.md).

### Changelog

You can track the changes of the <strong>clj-email-agent</strong> library [here](CHANGES.md).

# Usage

> Some parameters of the following functions and some further functions are not discussed in this file.
  To learn more about the available functionality, check out the [functional documentation](documentation/COVER.md)!

### Index

- [How to send an email?](#how-to-send-an-email)

- [How to acknowledge the sending?](#how-to-acknowledge-the-sending)

### How to send an email?

The [`email-agent.api/send-message!`](documentation/clj/email-agent/API.md/#send-message)
function sends an email message.

```
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
(send-message! {...}
               {:body "Hello World!" ...})
```

```
(send-message! {...}
               {:body [:html [:body [:div "Hello World!"]]] ...})
```

```
(send-message! {...}
               {:body (text-body "Hello World!") ...})
```

```
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
(send-message! {...}
               {:body [:alternative
                       (text-body "Hello World!")
                       (html-body "<html><body>Hello World!</body></html>")] ...})
```

### How to acknowledge the sending?

The [`email-agent.api/acknowledge?`](documentation/clj/email-agent/API.md/#acknowledge)
function returns true if the message has been sent without errors.

```
(acknowledge? (send-message! {...} {...}))
```
