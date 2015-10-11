(ns flechtwerk.graph-test
  (:require [clojure.test :refer :all]
            [flechtwerk.graph :refer :all]))


(deftest test-inversion
  (testing "Testing inversion of edges in DAG."
    (is (= {7 #{9}, 20 #{21}, 1 #{2}, 24 #{26}, 4 #{17 5}, 15 #{16}, 21 #{22}, 13 #{15 14}, 22 #{23}, 6 #{8}, 25 #{28}, 17 #{18}, 3 #{10}, :root 1, 2 #{4 3 8}, 23 #{24 25}, 19 #{20}, 11 #{13 12}, 9 #{27}, 5 #{7 6 10}, 14 #{16}, 26 #{27}, 10 #{14}, 18 #{19}, 8 #{11 9}}
           (invert test-graph)))))


(deftest test-igraph->tracks
  (testing "Testing track calculation from inverse DAG."
    (is (= [1 2 [[4 [[17 18 19 20 21 22 23 [[24 26 27] [25 28]]] [5 [[7 9 27] [6 8 [[11 [[13 [[15 16] [14 16]]] [12]]] [9 27]]] [10 14 16]]]]] [3 10 14 16] [8 [[11 [[13 [[15 16] [14 16]]] [12]]] [9 27]]]]]
           (igraph->tracks (invert test-graph))))))


(deftest test-tracks->indexed-nodes
  (testing "Testing track conversion to index slicing of graph."
    (is (= (tracks->indexed-nodes (igraph->tracks (invert test-graph)))
           {7 [20 27 11 9 16 16 16], 1 [1], 4 [17 5 10 11 9], 13 [27], 6 [19 9 8 14 16 15 14], 3 [4 3 8], 12 [26 28], 2 [2], 11 [24 25], 9 [22 15 14], 5 [18 7 6 10 14 13 12 27], 10 [23 16 16], 8 [21 13 12 27]}
           ))))


(deftest test-only-last-merge-point
  (testing "Only track last merge position of a merge commit."
    (is (= (only-last-merge-point
            #{}
            {}
            (sort-by first > {1 [1],
                              2 [2],
                              3 [4 3],
                              4 [5],
                              5 [7 6 10],
                              6 [9 8],
                              7 [9]}))
           {7 [9], 6 [8], 5 [7 6 10], 4 [5], 3 [4 3], 2 [2], 1 [1]}))))


(deftest test-compute-positions
  (testing "Position computation."
    (is (= (compute-positions test-graph)
           {:nodes {7 {:y 2/5, :x 5/13}, 20 {:y 1/4, :x 7/13}, 27 {:y 1/2, :x 1}, 1 {:y 1/2, :x 1/13}, 24 {:y 1/3, :x 11/13}, 4 {:y 1/3, :x 3/13}, 15 {:y 1/2, :x 9/13}, 21 {:y 1/4, :x 8/13}, 13 {:y 1/2, :x 8/13}, 22 {:y 1/4, :x 9/13}, 6 {:y 3/5, :x 5/13}, 28 {:y 2/3, :x 12/13}, 25 {:y 2/3, :x 11/13}, 17 {:y 1/3, :x 4/13}, 3 {:y 2/3, :x 3/13}, 12 {:y 3/4, :x 8/13}, 2 {:y 1/2, :x 2/13}, 23 {:y 1/4, :x 10/13}, 19 {:y 1/3, :x 6/13}, 11 {:y 1/2, :x 7/13}, 9 {:y 3/4, :x 7/13}, 5 {:y 2/3, :x 4/13}, 14 {:y 3/4, :x 9/13}, 26 {:y 1/3, :x 12/13}, 16 {:y 3/4, :x 10/13}, 10 {:y 4/5, :x 5/13}, 18 {:y 1/5, :x 5/13}, 8 {:y 2/3, :x 6/13}}, :edges {7 [5], 20 [19], 27 [9 26], 1 [], 24 [23], 4 [2], 15 [13], 21 [20], 13 [11], 22 [21], 6 [5], 28 [25], 25 [23], 17 [4], 3 [2], 12 [11], 2 [1], 23 [22], 19 [18], 11 [8], 9 [8 7], 5 [4], 14 [13 10], 26 [24], 16 [14 15], 10 [5 3], 18 [17], 8 [6 2]}}))))
