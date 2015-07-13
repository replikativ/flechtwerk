(ns geschichte-gorilla.core-test
  (:require [clojure.test :refer :all]
            [geschichte-gorilla.core :refer :all]))

(def test-repo
    {:causal-order {10 []
                    20 [10]
                    30 [20]
                    40 [20]
                    50 [40]
                    60 [30 50]
                    70 [60]
                    80 [30]
                    90 [80]
                    100 [70 140]
                    110 [100]
                    120 [90]
                    130 [30]
                    140 [130]
                    150 [50]
                    160 [260]
                    170 [160 110]
                    180 [120]
                    190 [180]
                    200 [190]
                    210 [200]
                    220 [210]
                    230 [140]
                    240 [120 220]
                    250 [240]
                    260 [150]}
     :commits
     {10 "master"
      20 "master"
      30 "master"
      40 "fix"
      50 "fix"
      60 "master"
      70 "master"
      80 "dev"
      90 "dev"
      100 "master"
      110 "master"
      120 "dev"
      130 "fix-2"
      140 "fix-2"
      150 "fix"
      160 "fix"
      170 "master"
      180 "fix-dev"
      190 "fix-dev"
      200 "fix-dev"
      210 "fix-dev"
      220 "fix-dev"
      230 "fix-2"
      240 "dev"
      250 "dev"
      260 "fix"}
     :branches {"master" #{170}
                "fix" #{160}
                "dev" #{250}
                "fix-dev" #{220}
                "fix-2" #{230}}})

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))
