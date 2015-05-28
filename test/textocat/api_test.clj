(ns textocat.api-test
  (:require [clojure.test :refer :all]
            [textocat.api :refer :all]
            [cheshire.core :refer [generate-string]])
  (:use clj-http.fake))

(deftest request-url-test
  (testing "Request url forming"
    (is (= (request-url) "http://api.textocat.com"))
    (is (= (request-url "status") "http://api.textocat.com/status"))
    (is (= (request-url "AUTH_TOKEN" "entity/queue") 
           "http://api.textocat.com/entity/queue?auth_token=AUTH_TOKEN"))))

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
           {(request-url auth-token "entity/queue")
            (fn [request]
              {:status 200 :headers {} :body (generate-string {:batchId "123"
                                                               :status "IN_PROGRESS"})})}
           (:batchId (entity-queue auth-token [{:text "Hello, World!" :tag "test"}]))) "123")))
