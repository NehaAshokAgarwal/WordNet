import dsa.BFSPaths;
import dsa.Graph;
import dsa.RedBlackBinarySearchTreeST;
import stdlib.In;
import stdlib.StdOut;

public class GraphProperties {
    private RedBlackBinarySearchTreeST<Integer, Integer> st; // degree -> frequency
    private double avgDegree;                                // average degree of the graph
    private double avgPathLength;                            // average path length of the graph
    private double clusteringCoefficient;                    // clustering coefficient of the graph

    // Computes graph properties for the undirected graph G.
    public GraphProperties(Graph G) {

        this.st = new RedBlackBinarySearchTreeST<>();
        int path = 0;
        double local = 0;
        // for all the vertex in the graph G...
        for (int i = 0; i < G.V(); i++) {
            // get the degree of the vertex and assign it to the variable degree.
            int edge = 0;
            int degree = G.degree(i);
            // if st contains does not contain that degree then add it with a value of 1.
            if (!st.contains(degree)) {
                st.put(degree, 0);
            }

            st.put(degree, st.get(degree) + 1);

            BFSPaths bsf = new BFSPaths(G, i);
            for (int j = 0; j < G.V(); j++) {
                if (bsf.hasPathTo(j)) {
                    path += bsf.distTo(j);
                }
            }

            for (int u : G.adj(i)) {
                for (int w : G.adj(i)) {
                    if (hasEdge(G, u, w)) {
                        edge += 1;
                    }
                }
            }
            double possible = (G.degree(i) * (G.degree(i) - 1)) / 2.0;
            edge /= 2;
            if (possible > 0) {
                local += (edge / possible);
            }
        }
        // applying the formula
        this.avgDegree = ((2.0 * G.E()) / G.V());
        this.avgPathLength = (path * 1.0) / (G.V() * (G.V() - 1));
        this.clusteringCoefficient = (local / G.V());

    }

    // Returns the degree distribution of the graph (a symbol table mapping each degree value to
    // the number of vertices with that value).
    public RedBlackBinarySearchTreeST<Integer, Integer> degreeDistribution() {
        return st;
    }

    // Returns the average degree of the graph.
    public double averageDegree() {
        return avgDegree;
    }

    // Returns the average path length of the graph.
    public double averagePathLength() {
        return avgPathLength;
    }

    // Returns the global clustering coefficient of the graph.
    public double clusteringCoefficient() {
        return clusteringCoefficient;
    }

    // Returns true if G has an edge between vertices v and w, and false otherwise.
    private static boolean hasEdge(Graph G, int v, int w) {
        for (int u : G.adj(v)) {
            if (u == w) {
                return true;
            }
        }
        return false;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        GraphProperties gp = new GraphProperties(G);
        RedBlackBinarySearchTreeST<Integer, Integer> st = gp.degreeDistribution();
        StdOut.println("Degree distribution:");
        for (int degree : st.keys()) {
            StdOut.println("  " + degree + ": " + st.get(degree));
        }
        StdOut.printf("Average degree         = %7.3f\n", gp.averageDegree());
        StdOut.printf("Average path length    = %7.3f\n", gp.averagePathLength());
        StdOut.printf("Clustering coefficient = %7.3f\n", gp.clusteringCoefficient());
    }
}
