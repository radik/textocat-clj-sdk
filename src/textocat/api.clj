(ns textocat.api
  (:require [clj-http.client :as client]))

(def base-url "http://api.textocat.com")

(defn request-url
  ([] base-url)
  ([endpoint] (str base-url "/" endpoint)))

(defn status
  "Returns status of service."
  []
  (client/get (request-url "status")))
