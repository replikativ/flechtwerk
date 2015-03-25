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
  (let [port (if args
               (-> args first read-string)
               8082)]
    (info "SERVER - Warming up...")
    (run-server (site #'handler) {:port port :join? false})
    (info "SERVER - running!")
    (info  (str "Visit http://localhost:" port))))
