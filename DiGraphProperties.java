import dsa.DiCycle;
import dsa.DiGraph;
import dsa.LinkedBag;
import stdlib.In;
import stdlib.StdOut;

public class DiGraphProperties {
    private boolean isDAG;              // is the digraph a DAG?
    private boolean isMap;              // is the digraph a map?
    private LinkedBag<Integer> sources; // the sources in the digraph
    private LinkedBag<Integer> sinks;   // the sinks in the digraph

    // Computes graph properties for the digraph G.
    public DiGraphProperties(DiGraph G) {
        DiCycle a = new DiCycle(G);
        isDAG = !a.hasCycle();
        isMap = true;
        this.sources = new LinkedBag<>();
        this.sinks = new LinkedBag<>();

        for (int v = 0; v < G.V(); v++) {
            int outDegree = 0;
            for (int n : G.adj(v)) {
                outDegree++;
            }
            // StdOut.println(outDegree);
            if (outDegree == 0) {
                this.sinks.add(v);
            }
            if (outDegree != 1) {
                isMap = false;
            }

            int inDegree = 0;
            for (int i = 0; i < G.V(); i++) {
                for (int k : G.adj(i)) {
                    if (k == v) {
                        inDegree++;
                    }
                }
            }
            if (inDegree == 0) {
                this.sources.add(v);
            }
        }
    }

    // Returns true if the digraph is a directed acyclic graph (DAG), and false otherwise.
    public boolean isDAG() {
        return isDAG;
    }

    // Returns true if the digraph is a map, and false otherwise.
    public boolean isMap() {
        return isMap;
    }

    // Returns all the sources (ie, vertices without any incoming edges) in the digraph.
    public Iterable<Integer> sources() {
        if (!sources.isEmpty()) {
            return sources;
        }
        return null;
    }

    // Returns all the sinks (ie, vertices without any outgoing edges) in the digraph.
    public Iterable<Integer> sinks() {
        if (!sinks.isEmpty()) {
            return sinks;
        }
        return null;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        DiGraph G = new DiGraph(in);
        DiGraphProperties gp = new DiGraphProperties(G);
        StdOut.print("Sources: ");
        for (int v : gp.sources()) {
            StdOut.print(v + " ");
        }
        StdOut.println();
        StdOut.print("Sinks: ");
        for (int v : gp.sinks()) {
            StdOut.print(v + " ");
        }
        StdOut.println();
        StdOut.println("Is DAG? " + gp.isDAG());
        StdOut.println("Is Map? " + gp.isMap());
    }
}
