(ns ^{:doc "Defines an interface between Titanium and Loom."
      :author "Aysylu Greenberg"}
  loom.titanium
  (:require [clojure.set :as set]
            [clojurewerkz.titanium.graph :as tg]
            [clojurewerkz.titanium.elements :as te]
            [clojurewerkz.titanium.edges :as edges]
            [clojurewerkz.titanium.vertices :as nodes]
            [loom.io :as io])
  (:use [loom graph]))

(defn titanium->loom
  "Converts titanium graph into Loom representation"
  ([titanium-graph &
    {:keys [node-fn edge-fn weight-fn]
     :or {node-fn (nodes/get-all-vertices)
          edge-fn (edges/get-all-edges)
          weight-fn (constantly 1)}}]
   (let [nodes-set (set node-fn)
         edges-set (set (map (juxt edges/tail-vertex
                                   edges/head-vertex)
                             edge-fn))]
   (reify
     Graph
     (nodes [_] nodes-set)
     (edges [_] edges-set)
     (has-node? [g node] (contains? (nodes g) node))
     (has-edge? [g n1 n2] (contains? (edges g) [n1 n2]))
     (successors [g] (partial successors g))
     (successors [g node] (filter (nodes g) (seq (nodes/connected-out-vertices node))))
     (out-degree [g node] (count (successors g node)))
     Digraph
     (predecessors [g] (partial predecessors g))
     (predecessors [g node] (filter (nodes g) (seq (nodes/connected-in-vertices node))))
     (in-degree [g node] (count (predecessors g node)))
     WeightedGraph
     (weight [g] (partial weight g))
     (weight [g n1 n2] (weight-fn n1 n2))))))

(defn view [g]
  (loom.io/view (titanium->loom g)
                :node-label nodes/to-map
                :edge-label
                (fn [n1 n2]
                  (mapv edges/to-map
                        (edges/edges-between n1 n2)))))
