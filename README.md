# Unofficial Textocat Clojure SDK

[![Join the chat at https://gitter.im/radik/textocat-clj-sdk](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/radik/textocat-clj-sdk?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Build Status](https://travis-ci.org/radik/textocat-clj-sdk.svg?branch=master)](https://travis-ci.org/radik/textocat-clj-sdk)

[![Clojars Project](http://clojars.org/textocat-clj-sdk/latest-version.svg)](https://clojars.org/textocat-clj-sdk)

This is unofficial python sdk for [Textocat](http://textocat.com).

[Textocat API](http://docs.textocat.com).
## Usage

```clojure
;; Require lib
(ns myprog
  (:require [texocat.api :as textocat])) 

;; Specify your auth-token
(def auth-token "MY-AUTH-TOKEN")

;; Get service status
(textocat/status)

;; Enqueue doc for entity recognition
(textocat/entity-queue auth-token [{:text "Hello, World!" :tag "my-tag"}])
```

## License

Copyright 2015 Radik Fattakhov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
