(ns dr.evil)

(defn input-page []
  (slurp "src/index.html"))

(defn eval-expr [expr]
  (

(defn evil [params]
  (let [expr (params "expr")]
    (

(defroutes handler
  (GET "/" [] (input-page))
  (POST "/" {params :params} (evil params))
  (ANY "/*" [path] (redirect "/"))
