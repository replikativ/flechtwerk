# geschichte-gorilla
Simple visualization of commit graph in gorilla using [vega](https://github.com/trifacta/vega) or [quil](https://github.com/quil/quil). Currently this is still a very unstable version. Use with caution.
See gorilla example use [here](http://viewer.gorilla-repl.org/view.html?source=github&user=kordano&repo=geschichte-gorilla&path=example.clj).

## Installation
Geschichte-gorilla is available at Clojars. Add the following to the `:dependencies` section of your `project.clj` file:

```
[geschichte-gorilla "0.1.0-SNAPSHOT"]
```

## Gorilla usage
Use in
```clojure
(ns your.gorilla.repl
  (:require  [gorilla-repl.vega :as v]
             [geschichte-gorilla.core :as g]))

(def some-repo {:causal-order {10 [] 20 [10] 30 [20] 40 [20]}
                :branches {"master" #{30} "dev" #{40}}
                :commits {10 "master" 20 "master" 30 "master" 40 "dev"}})

(v/vega-view (g/commit-graph some-repo))

```


## Repl usage
```clojure
(ns your.geschichte.project
  (:require [geschichte-gorilla.core :as g]))

(def some-repo {:causal-order {10 [] 20 [10] 30 [20] 40 [20]}
                :branches {"master" #{30} "dev" #{40}}
                :commits {10 "master" 20 "master" 30 "master" 40 "dev"}})

(g/sketch-graph some-repo)

```
## Vega TODO
- [ ] commit ids on mouseover
- [ ] commit value on click

## Quil TODO
- [x] build graph using quil 
- [ ] commit ids on mouseover
- [ ] branch name at head commits
- [ ] force-based build process animated
- [ ] branches and merges via bezier curves
- [ ] commit value on click

## License

Copyright © 2015 Konrad Kühne

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
