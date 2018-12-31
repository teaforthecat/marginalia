(ns marginalia.test.doc-test
  (:require [clojure.test :refer [deftest testing is]]
            [marginalia.doc :refer [doc check shoot reset-docs]]))


(defn on-earth? [w]
  (and (:flat w) (> 5 (get-in w [:voyage :days]))))

(deftest simple-doc
  (testing "example"
    (reset-docs)
    (doc "Given the world is flat"
         (let [w (atom {:flat true})]
           (doc "When I sail for 5 days"
                (swap! w assoc-in [:voyage :days] 5)
                (doc "Then I should fall off the edge"
                     ;; no reporting of pass/fail, an exception will be thrown instead
                     (assert (= false (on-earth? @w)))))))
    (is (= @marginalia.doc/docs [{:docstring "Then I should fall off the edge"}
                                 {:docstring "When I sail for 5 days"}
                                 {:docstring "Given the world is flat"}]))))

(defn screenshot []
  "example/path/to/image.png")

(deftest doc-with-shoot
  (testing "no nesting"
    (reset-docs)
    (doc "Given the world is flat"
         (shoot {} screenshot))
    (is (= @marginalia.doc/docs [{:docstring "Given the world is flat"
                                  :image "example/path/to/image.png"}])))
  (testing "nested"
    (reset-docs)
    (doc "Given the world is flat"
         (doc "When I sail for 5 days"
              (shoot {} screenshot)))
    (is (= @marginalia.doc/docs [{:docstring "When I sail for 5 days",
                                  :image "example/path/to/image.png"}
                                 {:docstring "Given the world is flat"}]))))

(deftest with-shoot-and-check
  (testing "reporting a failure"
    (reset-docs)
    (doc "Given the world is flat"
         (shoot
          (check false)
          screenshot))
    (is (= @marginalia.doc/docs [{:docstring "Given the world is flat",
                                  :result false
                                  :image "example/path/to/image.png"}]))))
