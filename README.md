# Titanium-Loom

Titanium-Loom defines an interface between [Titanium](http://titanium.clojurewerkz.org/), a Clojure graph library by Clojurewerkz, and [Loom](https://github.com/aysylu/loom), a Clojure graph algorithms and visualization library.

## Usage

### Leiningen/Clojars [group-id/name version]

    [aysylu/titanium-loom "0.1.0"]

### Namespaces

    loom.titanium - integration API
    
### Creation and Interaction

For information on how to create a Titanium graph, see [tutorial](http://titanium.clojurewerkz.org/articles/getting_started.html).

#### Create a Loom Graph from Titanium Representation

    (def default-graph (titanium->loom titanium-graph))
    (def custom-graph (titanium->loom titanium-graph
                                      :node-fn (clojurewerkz.titanium.vertices/find-by-kv :age 22)
                                      :edge-fn (->> (clojurewerkz.titanium.edges/get-all-edges)
                                                    (filter #(not= % 1)))
                                      :weight-fn (constantly 22))

The <code>custom-graph</code> is created with custom node, edge, and weight functions. This allows the user to convert to Loom representation a subset of the graph stored in the database, if the graph is too big and would not fit into memory, or if only parts of the data are of interest for the analysis.

The <code>default-graph</code> is created using node, edge, and weight functions with default behavior. The node and edge functions return all of the vertices and edges (respectively) in the Titanium graph. The default weight function returns 1 for any given edge.

#### Visualize

If you have [GraphViz](http://www.graphviz.org) installed, and its binaries are in the path, you can view graphs with <code>loom.io/view</code>:

    (view default-graph) ;opens image in default image viewer
    
#### Inspect and Run Algorithms

See Loom's [documentation](https://github.com/aysylu/loom) on how to inspect the Loom graph and run graph algorithms on it.

## Dependencies

* Clojure
* Loom
* Titanium
* Optional for visualization: [GrapViz](http://graphviz.org).

## TODO

* Add support for infinite graphs
* Use more efficient data structures
* Maybe add validations

## Contributors

* [Aysylu Greenberg] (https://github.com/aysylu), [aysylu [dot] greenberg [at] gmail [dot] com](mailto:aysylu.greenberg@gmail.com), [@aysylu22](http://twitter.com/aysylu22)

## License

Copyright (C) 2013 Aysylu Greenberg.

Distributed under the [Eclipse Public License](http://opensource.org/licenses/eclipse-1.0.php), the same as Clojure.
