(ns dr-evil-test
  (:use [dr.evil] :reload-all)
  (:use [clojure.test]))

(deftest eval-expr-test
  (is (= (eval-expr "(+ 1 1)") {:result "2\n" :error false}))
  (is (:error (eval-expr "(/ 1 0)"))))

(deftest html-test
  (is (= (subs (html "/blah") 0 6) "<html>"))
  (is (re-find (re-pattern "/blah") (html "/blah"))))

(deftest evil-test
  (is (= (subs (evil "/blah" nil) 0 6) "<html>"))
  (is (= (evil "/blah" "(+ 2 3)") "{\"result\":\"5\\n\",\"error\":false}")))
