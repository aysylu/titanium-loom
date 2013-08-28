(ns loom.titanium-test
  (:use [clojure.test]
        [titanium-loom.core]
        [loom graph alg])
  (:require [clojurewerkz.titanium.edges :as edges]
            [clojurewerkz.titanium.vertices :as nodes]
            [clojurewerkz.titanium.query :as q]
            [clojurewerkz.titanium.graph :as tg]))

(deftest graph-ops
  (let [in-mem-graph (tg/open {"storage.backend" "inmemory"})]
    (tg/transact!
      (let [a (nodes/create! {:name "Node A"})
            b (nodes/create! {:name "Node B"})
            c (nodes/create! {:name "Node C"})
            d (nodes/create! {:name "Node D"})
            e (nodes/create! {:name "Node E"})
            f (nodes/create! {:name "Node F"})
            g (nodes/create! {:name "Node G"})
            e1  (edges/connect! a "edge A->B" b)
            e2  (edges/connect! a "edge A->C" c)
            e3  (edges/connect! b "edge B->C" c)
            e4  (edges/connect! b "edge B->D" d)
            e5  (edges/connect! c "edge C->E" e)
            e6  (edges/connect! c "edge C->F" f)
            e7  (edges/connect! e "edge E->D" d)
            e8  (edges/connect! f "edge F->E" e)
            e9  (edges/connect! g "edge G->A" a)
            e10 (edges/connect! g "edge G->F" f)
            graph (titanium->loom in-mem-graph)]
        (is (= [d e f c b a g] (post-traverse graph g)))
        (is (dag? graph))
        (is (= [g a b c f e d] (topsort graph)))
        (is (= #{a b c d e f g} (nodes graph)))
        (is (= (set (map
                      (juxt edges/tail-vertex edges/head-vertex)
                      #{e1 e2 e3 e4 e5 e6 e7 e8 e9 e10}))
               (edges graph)))
        (is (has-node? graph a))
        (is (has-edge? graph a b))
        (is (= false (has-edge? graph a g)))    
        (is (= [b c] (successors graph a))) 
        (is (= [a f] (successors graph g))) 
        (is (= 2 (out-degree graph a)))
        (is (= 2 (out-degree graph g)))
        (is (= [c f] (predecessors graph e))) 
        (is (= [g] (predecessors graph a))) 
        (is (= 2 (in-degree graph e)))
        (is (= 1 (in-degree graph a)))
        (is (= 1 (weight graph a b)))))))

;; Same graph as above +
;; node Z and edges G->Z and Z->A excluded in Loom
(deftest custom-graph-ops
  (let [in-mem-graph (tg/open {"storage.backend" "inmemory"})]
    (tg/transact!
      (let [a (nodes/create! {:name "Node A" :incl true})
            b (nodes/create! {:name "Node B" :incl true})
            c (nodes/create! {:name "Node C" :incl true})
            d (nodes/create! {:name "Node D" :incl true})
            e (nodes/create! {:name "Node E" :incl true})
            f (nodes/create! {:name "Node F" :incl true})
            g (nodes/create! {:name "Node G" :incl true})
            z (nodes/create! {:name "Node Z" :incl false})
            e1  (edges/connect! a "edge A->B" b)
            e2  (edges/connect! a "edge A->C" c)
            e3  (edges/connect! b "edge B->C" c)
            e4  (edges/connect! b "edge B->D" d)
            e5  (edges/connect! c "edge C->E" e)
            e6  (edges/connect! c "edge C->F" f)
            e7  (edges/connect! e "edge E->D" d)
            e8  (edges/connect! f "edge F->E" e)
            e9  (edges/connect! g "edge G->A" a)
            e10 (edges/connect! g "edge G->F" f)
            e11 (edges/connect! g "edge G->Z" z)
            e12 (edges/connect! z "edge Z->A" a)
            node-fn (nodes/find-by-kv :incl true)
            edge-fn (->> (edges/get-all-edges)
                         (filter #(and (not= % e12)
                                       (not= % e11))))
            graph (titanium->loom in-mem-graph
                                  :node-fn node-fn
                                  :edge-fn edge-fn)]
        (is (= [d e f c b a g] (post-traverse graph g)))
        (is (dag? graph))
        (is (= [g a b c f e d] (topsort graph)))
        (is (= #{a b c d e f g} (nodes graph)))
        (is (= (set (map
                      (juxt edges/tail-vertex edges/head-vertex)
                      #{e1 e2 e3 e4 e5 e6 e7 e8 e9 e10}))
               (edges graph)))
        (is (has-node? graph a))
        (is (has-edge? graph a b))
        (is (= false (has-edge? graph a g)))
        (is (= [b c] (successors graph a))) 
        (is (= [a f] (successors graph g))) 
        (is (= 2 (out-degree graph a)))
        (is (= 2 (out-degree graph g)))
        (is (= [c f] (predecessors graph e))) 
        (is (= [g] (predecessors graph a))) 
        (is (= 2 (in-degree graph e)))
        (is (= 1 (in-degree graph a)))
        (is (= 1 (weight graph a b)))    
        ; tests specific for this graph
        ; with Z, G->Z, and Z->A
        (is (= false (has-node? graph z)))
        (is (= false (has-edge? graph a z)))
        (is (= false (has-edge? graph g z)))))))
