(ns dr.evil.run
  (:require dr.evil)
  (:use ring.adapter.jetty))

(run-jetty #'dr.evil/app {:port 8080})
