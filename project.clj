(defproject geschichte-gorilla "0.1.0-SNAPSHOT"
  :description "Vega specifications for geschichte commit graph"
  :url "https://github.com/kordano/geschichte-gorilla"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [gorilla-repl "0.3.4" :exclusions [http-kit]]
                 [aprint "0.1.3"]]
  :plugins [[lein-gorilla "0.3.4"]]
  )
