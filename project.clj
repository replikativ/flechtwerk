(defproject io.replikativ/flechtwerk "0.1.1-SNAPSHOT"
  :description "Vega specifications for replikativ commit graph"
  :url "https://github.com/replikativ/flechtwerk"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.34"]
                 [org.clojure/core.async "0.2.374"]
                 [io.replikativ/full.async "0.9.1-SNAPSHOT"]
                 [io.replikativ/konserve "0.3.5"]
                 [quil "2.2.6"]]

  :profiles {:dev {:dependencies [[com.cemerick/piggieback "0.2.1"]]
                   :figwheel {:nrepl-port 7888
                              :nrepl-middleware ["cider.nrepl/cider-middleware"
                                                 "cemerick.piggieback/wrap-cljs-repl"]}
                   :plugins [[lein-figwheel "0.5.0-2"]]}}


  :plugins [[lein-cljsbuild "1.1.2"]]

  :clean-targets ^{:protect false} ["target" "resources/public/js"]

  :cljsbuild
  {:builds
   [{:id "cljs_repl"
     :source-paths ["src/"]
     :figwheel true
     :compiler
     {:main flechtwerk.demo
      :asset-path "js/out"
      :output-to "resources/public/js/main.js"
      :output-dir "resources/public/js/out"
      :optimizations :none
      :pretty-print true}}
    {:id "dev"
     :source-paths ["src"]
     :compiler
     {:main flechtwerk.demo
      :output-to "resources/public/js/main.js"
      :optimizations :simple
      :pretty-print true}}]})
