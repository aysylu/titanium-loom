(defproject aysylu/titanium-loom "0.1.3"
  :description "Titanium-Loom integration library"
  :url "https://github.com/aysylu/titanium-loom"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [aysylu/loom "0.4.1"]
                 [clojurewerkz/titanium "1.0.0-beta1"]]
  :aliases {"release" ["do" "clean," "with-profile" "default" "deploy" "clojars"]})
