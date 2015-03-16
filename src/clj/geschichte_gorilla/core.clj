(ns geschichte-gorilla.core
  (:require [taoensso.timbre :as timbre]
            [org.httpkit.server :refer [run-server]]
            [clojure.java.io :as io]
            [compojure.route :refer [resources]]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.handler :refer [site api]]))

(timbre/refer-timbre)

(defroutes handler
  (resources "/")
  (GET "/*" [] (io/resource "public/index.html")))


(defn -main [& args]
  (let [port (-> args first read-string)]
    (info "SERVER - Warming up...")
    (run-server (site #'handler) {:port (or port 8082) :join? false})
    (info "SERVER - running!")
    (info  (str "Visit http://localhost:" (or port 8082)))))
