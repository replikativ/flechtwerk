# geschichte-gorilla

Simple visualization of commit graph in gorilla using vega. Currently this is still a very unstable version. Use with caution.
See example use [here](http://viewer.gorilla-repl.org/view.html?source=github&user=kordano&repo=geschichte-example&path=ggexample.clj).

## Installation
Geschichte-gorilla is available at Clojars. Add the following to the `:dependencies` section of your `project.clj` file:

```
[geschichte-gorilla "0.1.0-SNAPSHOT"]
```

## Example Usage

```clojure
(ns your.gorilla.repl 
  (:require  [gorilla-repl.vega :as v]
             [geschichte-gorilla.core :as g]))
             
(def some-repo {:causal-order {10 [] 20 [10] 30 [20] 40 [20]} 
                :branches {"master" #{30} "dev" #{40}}})

(v/vega-view (g/commit-graph some-repo))
```


## License

Copyright © 2015 Konrad Kühne

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
