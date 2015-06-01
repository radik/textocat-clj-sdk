(ns textocat.api
  (:require [clj-http.client :as client]
            [cheshire.core :refer [generate-string]]))

(def base-url "http://api.textocat.com")

(defn request-url
  ([] base-url)
  ([endpoint] (str base-url "/" endpoint)))

(defn status
  "Returns a status of the service.
  
  E.g. result {:message \"The service is operating normally\"
               :statusCode \"200 OK\"}"
  []
  (:body (client/get (request-url "status") {:as :json})))

(defn entity-queue
  "Enqueues text documents for an entity recognition.
  
  Max count of docs is 50. Max size of each document is 50 kB
  (approximatly, each character considered as 2 byte UTF-8 symbol).

  Takes collections of docs, e.g. [{:text \"Hello, World!\", :tag \"clj-test\"}]

  The service does not store sources texts.

  Returns collection identity. E.g. result
  {:batchId \"<requested batch id>\", :status \"IN_PROGRESS\"}
  "
  [auth-token docs]
  (:body (client/post (request-url "entity/queue") 
                      {:query-params {"auth_token" auth-token}
                       :body (generate-string docs)
                       :content-type :json
                       :as :json})))

(defn entity-request
  "Returns status of collection by specified batch id.

  Returns {:batchId \"<requested batch id>\", :status \"IN_PROGRESS\"}
  if collection is still processing or
  {:batchId \"<requested batch id>\", :status \"FINISHED\"}
  if collection processing finished."
  [auth-token batch-id]
  (:body (client/get (request-url "entity/request") 
                     {:query-params {"auth_token" auth-token
                                     "batch_id" batch-id} 
                      :as :json})))

(defn entity-retrieve
  "Returns array of documents with recognized entities by batch ids.

  It's possible to retrieve up to 100 collections of documents.
  E.g. result {:batchIds [\"1\",\"2\"]
               :documents [{:status \"FINISHED\"
                            :tag \"clj-test\"
                            :entities [{:span \"World\"
                                        :beginOffset 7
                                        :endOffset 12
                                        :category \"LOCATION\"}]}
                           {:status \"FINISHED\"
                            :tag \"clj-test\"
                            :entities [{:span \"Мир\"
                                        :beginOffset 8
                                        :endOffset 11
                                        :category \"LOCATION\"}]}]}"
  [auth-token & batch-ids]
  (:body (client/get (request-url "entity/retrieve")
                     {:query-params {"auth_token" auth-token
                                     "batch_id" batch-ids}
                      :as :json})))

(defn entity-search
  "Searching in all documents uploaded by user.

  Documents are sorted by relevance. Exteneded syntax for search 
  queries are supported. For more information about query syntax
  look at http://docs.textocat.com/search-query-syntax.pdf.

  E.g. result {:searchQuery \"LOCATION:World\"
               :documents [{:status \"FINISHED\"
                            :tag \"clj-test\"
                            :entities [{:span \"World\"
                                        :beginOffset 7
                                        :endOffset 12
                                        :category \"LOCATION\"}]}]}"
  [auth-token search-query]
  (:body (client/get (request-url "entity/search")
                     {:query-params {"auth_token" auth-token
                                     "search_query" search-query}
                      :as :json})))
