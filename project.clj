(defproject geschichte-gorilla "0.1.0-SNAPSHOT"

  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}


  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.6"]
                 [gorilla-renderable "1.0.0"]
                 [org.clojure/clojurescript "0.0-2411"]
                 [net.drib/strokes "0.5.1"]]

  :paths ["src/clj" "src/cljs"]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :cljsbuild
  {:builds
   [{:source-paths ["src/cljs"]
     :compiler
     {:output-to "resources/public/js/compiled/main.js"
      :output-dir "resources/public/js/compiled/out"
      :optimizations :none
      :pretty-print false
      :source-map "main.js.map"}}]}
  )
