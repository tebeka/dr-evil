(ns dr.evil
  (:use compojure.core)
  (:use ring.util.response)
  (:use [clojure.stacktrace :only (print-stack-trace)])
  (:use [clojure.contrib.json :only (json-str)]))

(defn input-page []
  (slurp "src/index.html"))

; FIXME: Give use ability to change namespace
(defn wrapped-expr [expr]
  (str "(binding [*print-length* 100] (do (in-ns 'dr.evil) " expr "))"))

(defn eval-expr [expr]
  (try
    (let [result (load-string (wrapped-expr expr))]
      { :result (with-out-str (println result)) :error false })
    (catch Exception e
      (let [result (with-out-str (print-stack-trace e))]
        { :result result :error true }))))

(defn evil [params]
  (json-str (eval-expr (params "expr"))))

(defroutes app
  (GET "/" [] (input-page))
  (POST "/" {params :params} (evil params))
  (ANY "/*" [path] (redirect "/")))
