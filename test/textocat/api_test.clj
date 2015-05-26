(ns textocat.api-test
  (:require [clojure.test :refer :all]
            [textocat.api :refer :all]))

(deftest request-url-test
  (testing "Request url forming"
    (is (= (request-url) "http://api.textocat.com"))
    (is (= (request-url "status") "http://api.textocat.com/status"))
    (is (= (request-url "AUTH_TOKEN" "entity/queue") 
           "http://api.textocat.com/entity/queue?auth_token=AUTH_TOKEN"))))
