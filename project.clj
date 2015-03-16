(defproject geschichte-gorilla "0.1.0-SNAPSHOT"

  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}


  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.6"]
                 [com.taoensso/timbre "3.3.1"]
                 [http-kit "2.1.19"]
                 [ring "1.3.1"]
                 [compojure "1.2.1"]
                 [gorilla-renderable "1.0.0"]
                 [org.clojure/clojurescript "0.0-2411"]
                 [net.drib/strokes "0.5.1"]]

  :source-paths ["src/clj" "src/cljs"]

  :main geschichte-gorilla.core

  :plugins [[lein-cljsbuild "1.0.3"]]

  :cljsbuild
  {:builds
   {:dev {:source-paths ["src/cljs"]
           :compiler
           {:output-to "resources/public/js/compiled/main.js"
            :output-dir "resources/public/js/compiled/out"
            :optimizations :none
            :pretty-print false
            :source-map "main.js.map"}}
    :main {:source-paths ["src/cljs"]
            :compiler
            {:output-to "resources/public/js/main.js"
             :optimizations :advanced }}}})
