(ns marginalia.test.html-test
  (:require [clojure.test :refer [deftest testing is]]
            [marginalia.html :as html]))


(deftest section-to-html
  (testing "empty"
    (let [output (html/section-to-html {})]
      (is (= "<tr><td class=\"docs passed\">\n</td><td class=\"codes\"></td></tr>" output))))
  (testing "with doc"
    (let [output (html/section-to-html {:docstring "and it was good"})]
      (is (= "<tr><td class=\"docs passed\"><p>and it was good</p>\n</td><td class=\"codes\"></td></tr>" output))))
  (testing "with doc and failure"
    (let [output (html/section-to-html {:docstring "and it was good" :result false})]
      ;; passed turns to failed
      (is (= "<tr><td class=\"docs failed\"><p>and it was good</p>\n</td><td class=\"codes\"></td></tr>" output))))
  (testing "with doc and screenshot"
    (let [output (html/section-to-html {:docstring "and it was good" :image "path/to/img.png"})]
      (is (= "<tr><td class=\"docs passed\"><p>and it was good</p>\n</td><td class=\"codes\"><img src=\"path/to/img.png\" /></td></tr>" output))))
  )
