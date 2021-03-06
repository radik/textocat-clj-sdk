(ns textocat.api-test
  (:require [clojure.test :refer :all]
            [textocat.api :refer :all]
            [cheshire.core :refer [generate-string]])
  (:use clj-http.fake))

(deftest request-url-test
  (testing "Request url forming"
    (is (= (request-url) "http://api.textocat.com"))
    (is (= (request-url "status") "http://api.textocat.com/status"))))

(deftest status-test
  (is (= (with-fake-routes
           {(request-url "status")
            (fn [request]
              {:status 200 :headers {} :body (generate-string {:message "The service is operating normally"
                                                               :statusCode "200 OK"})})}
           (:statusCode (status))) "200 OK")))

(def auth-token "AUTH_TOKEN")

(deftest entity-queue-test
  (is (= (with-fake-routes
           {(str (request-url "entity/queue") "?auth_token=" auth-token)
            (fn [request]
              {:status 200 :headers {} :body (generate-string {:batchId "123"
                                                               :status "IN_PROGRESS"})})}
           (:batchId (entity-queue auth-token [{:text "Hello, World!" :tag "test"}]))) "123")))

(deftest entity-request-test
  (is (= (with-fake-routes
           {(str (request-url "entity/request") "?auth_token=" auth-token "&batch_id=123")
            (fn [request]
              {:status 200 :headers {} :body (generate-string {:batchId "123"
                                                               :status "FINISHED"})})}
           (:batchId (entity-request auth-token "123"))) "123")))

(deftest entity-retrieve-test
  (is (= (with-fake-routes
           {(str (request-url "entity/retrieve") "?auth_token=" auth-token "&batch_id=1&batch_id=2")
            (fn [request]
              {:status 200 
               :headers {} 
               :body (generate-string {:batchIds ["1","2"]
                                       :documents [{:status "FINISHED"
                                                    :tag "clj-test"
                                                    :entities [{:span "World"
                                                                :beginOffset 7
                                                                :endOffset 12
                                                                :category "LOCATION"}]}
                                                   {:status "FINISHED"
                                                    :tag "clj-test"
                                                    :entities [{:span "Мир"
                                                                :beginOffset 8
                                                                :endOffset 11
                                                                :category "LOCATION"}]}]})})}
           (:batchIds (entity-retrieve auth-token "1" "2"))) ["1", "2"])))

(deftest entity-search-test
  (is (= (with-fake-routes
           {(str (request-url "entity/search") "?auth_token=" auth-token "&search_query=LOCATION%3AWorld")
            (fn [request]
              {:status 200
               :headers {}
               :body (generate-string {:searchQuery "LOCATION:World"
                                       :documents [{:status "FINISHED"
                                                    :tag "clj-test"
                                                    :entities [{:span "World"
                                                                :beginOffset 7
                                                                :endOffset 12
                                                                :category "LOCATION"}]}]})})}
           (:searchQuery (entity-search auth-token "LOCATION:World"))) "LOCATION:World")))
