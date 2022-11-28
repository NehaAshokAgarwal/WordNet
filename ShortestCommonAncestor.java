import dsa.DiGraph;
import dsa.LinkedQueue;
import dsa.SeparateChainingHashST;
import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class ShortestCommonAncestor {
    private DiGraph G; // Digraph g

    // Constructs a ShortestCommonAncestor object given a rooted DAG.
    public ShortestCommonAncestor(DiGraph G) {
        // If G is null, then throw a NPE with a message saying that G is null.
        if (G == null) {
            throw new NullPointerException("G is null");
        }
        // Initialise the instance variable to G.
        this.G = G;
    }

    // Returns length of the shortest ancestral path between vertices v and w.
    public int length(int v, int w) {
        // If v is not in the range of the number of vertices in the graph then throw a NPE
        // with a message saying that the v is invalid.
        if (v < 0 || v >= G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        // If w is not in the range of the number of vertices in the graph, then throw a NPE,
        // with a message saying that the w is invalid.
        if (w < 0 || w >= G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }

        // Calling the ancestor method to get the shortest common ancestor.
        // Let's say the ancestor is a.
        int a = this.ancestor(v, w);

        // A call to the distFrom method on vertex v will give a hash table with all the vertices
        // which are reachable from v and its corresponding shortest distance.
        SeparateChainingHashST<Integer, Integer> first = distFrom(v);

        // Similarly, A call to the distFrom method on vertex w will give a hash table with all
        // the vertices which are reachable from w and its corresponding shortest distance
        SeparateChainingHashST<Integer, Integer> second = distFrom(w);

        // Return the sum of the distance from the vertex v to a and distance from vertex w to a,
        // which is the shortest ancestral path.
        return first.get(a) + second.get(a);
    }

    // Returns a shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) {
        // If v is not in the range of the number of vertices in the graph then throw a NPE
        // with a message saying that the v is invalid.
        if (v < 0 || v >= G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        // If w is not in the range of the number of vertices in the graph, then throw a NPE,
        // with a message saying that the w is invalid.
        if (w < 0 || w >= G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }

        // Set a variable distance to infinity to keep track of the shortest distance.
        int distance = Integer.MAX_VALUE;
        // set variable ances to 0(root is the ancestor of all vertex) to keep tract of the shortest
        // ancestor.
        int ances = 0;
        // A call to the distFrom method on vertex v will give a hash table with all the vertices
        // which are reachable from v and its corresponding shortest distance.
        SeparateChainingHashST<Integer, Integer> first = distFrom(v);

        // Similarly, A call to the distFrom method on vertex w will give a hash table with all
        // the vertices which are reachable from w and its corresponding shortest distance
        SeparateChainingHashST<Integer, Integer> second = distFrom(w);

        // Iterate over all the keys of one (reachable vertices from the vertex v)...
        for (int k : first.keys()) {
            // If that vertex is in the two ST as well (i.e. vertex is reachable from w as well)...
            if (second.contains(k)) {
                // then add the distances (v to common vertex) + (w to common vertex)
                int r = first.get(k) + second.get(k);
                // if the value of r turns out to be smaller than the previous distance, then
                // update the value of distance. Also, remember the common vertex in the variable
                // ances.
                if (r < distance) {
                    distance = r;
                    ances = k;
                }
            }
        }
        // return ances(Shortest common ancestor).
        return ances;
    }

    // Returns length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
        // If A is null, then throw a NPE with a message  saying that the A is null.
        if (A == null) {
            throw new NullPointerException("A is null");
        }
        // If B is null, then throw a NPE with a message saying that the B is null.
        if (B == null) {
            throw new NullPointerException("B is null");
        }
        // Checking if the iterable objects A and B are empty or not.
        // If either of them is empty then throw a NPE with an appropriate message.
        int a1 = 0;
        int b1 = 0;

        for (int a : A) {
            a1++;
        }
        for (int b : B) {
            b1++;
        }
        // if a1 is 0, then the subset A is empty. Thus, throw an IAE with a message
        // saying that A is empty.
        if (a1 == 0) {
            throw new IllegalArgumentException("A is empty");
        }
        // if b1 is 0, then the subset B is empty. Thus, throw an IAE with a message
        // saying that B is empty.
        if (b1 == 0) {
            throw new IllegalArgumentException("B is empty");
        }

        // Call the triad method and store the array in to the variable t.
        int[] t = triad(A, B);
        // first element of the array is the shortest common ancestor anmd call it n.
        int n = t[0];
        // second element of the array is the vertex from the subset A and call it v.
        int v = t[1];
        // third element of the array is the vertex from the subset B and call it w.
        int w = t[2];
        // Call the distFrom method on vertex v.
        SeparateChainingHashST<Integer, Integer> first = distFrom(v);
        // Call the distFro method on vertex w.
        SeparateChainingHashST<Integer, Integer> second = distFrom(w);
        // return the sum of the distances from the vertex v to n and distance from vertex w to n.
        return first.get(n) + second.get(n);

    }

    // Returns a shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        // If A is null, then throw a NPE with a message saying that the A is null.
        if (A == null) {
            throw new NullPointerException("A is null");
        }
        // If B is null then throw a NPE with a message saying that the B is null.
        if (B == null) {
            throw new NullPointerException("B is null");
        }

        // Checking if the iterable objects A and B are empty or not.
        // If either of them is empty then throw a NPE with an appropriate message.
        int a1 = 0;
        int b1 = 0;

        for (int a : A) {
            a1++;
        }
        for (int b : B) {
            b1++;
        }

        // if a1 is 0, then the subset A is empty. Thus, throw an IAE with a message
        // saying that A is empty.
        if (a1 == 0) {
            throw new IllegalArgumentException("A is empty");
        }
        // if b1 is 0, then the subset B is empty. Thus, throw an IAE with a message
        // saying that B is empty.
        if (b1 == 0) {
            throw new IllegalArgumentException("B is empty");
        }

        // Call the triad method on the vertex subsets A and B.
        int[] table = triad(A, B);
        // return the first element of the array that triad method returns as it is the shortest
        // common ancestor of vertex subsets A and B.
        return table[0];

    }

    // Returns a map of vertices reachable from v and their respective shortest distances from v.
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        // The goal is to perform bfs.
        // Create a Linked queue q.
        LinkedQueue<Integer> q = new LinkedQueue<>();
        // Create a hash table st.
        SeparateChainingHashST<Integer, Integer> st = new SeparateChainingHashST<>();
        // enqueue the root into the linked queue q.
        q.enqueue(v);
        // add the root with a value of 0 to st.Saying that the distance to the root to itself is 0.
        st.put(v, 0);
        // As long as q is not empty, dequeue an element from it.
        while (!q.isEmpty()) {
            int s = q.dequeue();
            // For all the neighbours of the dequeued element...
            for (int n : G.adj(s)) {
                // If it is not there in the st, then add it with a value of st.get(s) + 1.
                // As it is one hop distance away form the vertex 's'.
                // Also, enqueue the neighbour into q.
                if (!st.contains(n)) {
                    st.put(n, st.get(s) + 1);
                    q.enqueue(n);
                }
            }
        }
        // return st with all the keys as the vertices that are reachable and the values as the
        // shortest corresponding distances.
        return st;
    }

    // Returns an array consisting of a shortest common ancestor a of vertex subsets A and B,
    // vertex v from A, and vertex w from B such that the path v-a-w is the shortest ancestral
    // path of A and B.
    private int[] triad(Iterable<Integer> A, Iterable<Integer> B) {
        // Set a variable distance to infinity to keep track of the shortest distance.
        double distance = Double.POSITIVE_INFINITY;
        // Create an array table of three elements.
        int[] table = new int[3];
        // For all the vertices in the subset A...
        for (int a : A) {
            // for all the vertices in the subset B...
            for (int b : B) {
                // Calling the ancestor method to find out the shortest common ancestor
                // between vertex a and b.
                int n = ancestor(a, b);
                // Also, calling the length method to find out the length of the shortest ancestral
                // path between vertex a and b.
                int l = length(a, b);
                // If the length of the path is less than the value of the distance...
                if (l < distance) {
                    // then update the value of the distance.
                    distance = l;
                    // update the first element of the array as the n(the shortest common ancestor).
                    table[0] = n;
                    // update the value of the second element of the array as 'a'.
                    table[1] = a;
                    // update the value of the third element of the array as 'b'.
                    table[2] = b;
                }

            }
        }
        // return table.
        return table;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        DiGraph G = new DiGraph(in);
        in.close();
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
