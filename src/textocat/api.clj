(ns textocat.api
  (:require [clj-http.client :as client]
            [cheshire.core :refer [generate-string]]))

(def base-url "http://api.textocat.com")

(defn request-url
  ([] base-url)
  ([endpoint] (str base-url "/" endpoint)))

(defn status
  "Returns a status of the service."
  []
  (:body (client/get (request-url "status") {:as :json})))

(defn entity-queue
  "Enqueues text documents for an entity recognition.
  
  Max count of docs is 50. Max size of each document is 50 kB
  (approximatly, each character considered as 2 byte UTF-8 symbol).

  The service does not store sources texts.

  Returns collection identity."
  [auth-token docs]
  (:body (client/post (request-url "entity/queue") 
                      {:query-params {"auth_token" auth-token}
                       :body (generate-string docs)
                       :content-type :json
                       :as :json})))

(defn entity-request
  ""
  [auth-token batch-id]
  (:body (client/get (request-url "entity/request") 
                     {:query-params {"auth_token" auth-token
                                     "batch_id" batch-id} 
                      :as :json})))

(defn entity-retrieve
  ""
  [auth-token & batch-ids]
  (:body (client/get (request-url "entity/retrieve")
                     {:query-params {"auth_token" auth-token
                                     "batch_id" batch-ids}
                      :as :json})))

(defn entity-search
  ""
  [auth-token search-query]
  (:body (client/get (request-url "entity/search")
                     {:query-params {"auth_token" auth-token
                                     "search_query" search-query}
                      :as :json})))
