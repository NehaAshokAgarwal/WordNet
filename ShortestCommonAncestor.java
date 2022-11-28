import dsa.DiGraph;
import dsa.LinkedQueue;
import dsa.SeparateChainingHashST;
import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class ShortestCommonAncestor {
    private DiGraph G; // Digraph g

    public ShortestCommonAncestor(DiGraph G) {
        if (G == null) {
            throw new NullPointerException("G is null");
        }
        this.G = G;
    }
    public int length(int v, int w) {
        if (v < 0 || v >= G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        if (w < 0 || w >= G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }
        int a = this.ancestor(v, w);
        SeparateChainingHashST<Integer, Integer> first = distFrom(v);
        SeparateChainingHashST<Integer, Integer> second = distFrom(w);
        return first.get(a) + second.get(a);
    }
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        if (w < 0 || w >= G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }
        int distance = Integer.MAX_VALUE;
        int ances = 0;
        SeparateChainingHashST<Integer, Integer> first = distFrom(v);
        SeparateChainingHashST<Integer, Integer> second = distFrom(w);
        for (int k : first.keys()) {
            if (second.contains(k)) {
                int r = first.get(k) + second.get(k);
                if (r < distance) {
                    distance = r;
                    ances = k;
                }
            }
        }
        return ances;
    }
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null) {
            throw new NullPointerException("A is null");
        }
        if (B == null) {
            throw new NullPointerException("B is null");
        }
        int a1 = 0;
        int b1 = 0;

        for (int a : A) {
            a1++;
        }
        for (int b : B) {
            b1++;
        }
        if (a1 == 0) {
            throw new IllegalArgumentException("A is empty");
        }
        if (b1 == 0) {
            throw new IllegalArgumentException("B is empty");
        }
        int[] t = triad(A, B);
        int n = t[0];
        int v = t[1];
        int w = t[2];
        SeparateChainingHashST<Integer, Integer> first = distFrom(v);
        SeparateChainingHashST<Integer, Integer> second = distFrom(w);
        return first.get(n) + second.get(n);

    }
    
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null) {
            throw new NullPointerException("A is null");
        }
        if (B == null) {
            throw new NullPointerException("B is null");
        }

        int a1 = 0;
        int b1 = 0;

        for (int a : A) {
            a1++;
        }
        for (int b : B) {
            b1++;
        }

        if (a1 == 0) {
            throw new IllegalArgumentException("A is empty");
        }
        if (b1 == 0) {
            throw new IllegalArgumentException("B is empty");
        }
        int[] table = triad(A, B);
        return table[0];

    }
    
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        LinkedQueue<Integer> q = new LinkedQueue<>();
        SeparateChainingHashST<Integer, Integer> st = new SeparateChainingHashST<>();
        q.enqueue(v);
        st.put(v, 0);
        while (!q.isEmpty()) {
            int s = q.dequeue();
            for (int n : G.adj(s)) {
                if (!st.contains(n)) {
                    st.put(n, st.get(s) + 1);
                    q.enqueue(n);
                }
            }
        }
        return st;
    }

    private int[] triad(Iterable<Integer> A, Iterable<Integer> B) {
        double distance = Double.POSITIVE_INFINITY;
        int[] table = new int[3];
        for (int a : A) {
            for (int b : B) {
                int n = ancestor(a, b);
                int l = length(a, b);.
                if (l < distance) {
                    distance = l;
                    table[0] = n;.
                    table[1] = a;
                    table[2] = b;
                }

            }
        }
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
