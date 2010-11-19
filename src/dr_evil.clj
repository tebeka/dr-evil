(ns dr.evil
  (:use [clojure.contrib.json :only (json-str)]))

(defn input-page []
  (slurp "src/index.html"))

(defn eval-expr [expr]
  (try
    (let [result (load-string (str "(in-ns 'dr.evil) " expr))]
      { :result (with-out-str (println result)) :error false })
    (catch Exception e
      (let [result (with-out-str (.printStackTrace e))]
        { :result result :error true }))))

(defn evil [params]
  (json-str (eval-expr (params "expr"))))

(defroutes app
  (GET "/" [] (input-page))
  (POST "/" {params :params} (evil params))
  (ANY "/*" [path] (redirect "/"))
