(ns dr.evil.run
  (:use dr.evil)
  (:use compojure.core)
  (:use ring.adapter.jetty)
  (:require [compojure.route :as route]))

(defroutes app
  (GET "/" [] "Nothing here! (try /evil)")
  (EVIL "/evil")
  (route/not-found "Dude! I can't find it."))

(run-jetty app {:port 8080})
