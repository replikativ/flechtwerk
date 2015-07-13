(ns geschichte-gorilla.graph-test
  (:require [clojure.test :refer :all]
            [geschichte-gorilla.graph :refer :all]))

(deftest test-positions
  (let [causal-order {10 [] 20 [10]}
        commits {10 :master 20 :master}
        branches {:master #{20}}]
    (testing "FIXME, I fail."
      (is (= (compute-positions causal-order branches commits)
             {:branches [[:master 20]]
              :links '([10 20 :master])
              :nodes [[10 :master] [20 :master]]
              :x-order '(:master)
              :x-positions {10 0.05, 20 0.95}
              :y-positions {10 1/2, 20 1/2}})))))
