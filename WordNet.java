import dsa.DiGraph;
import dsa.SeparateChainingHashST;
import dsa.Set;
import stdlib.In;
import stdlib.StdOut;

public class WordNet {
    private SeparateChainingHashST<String, Set<Integer>> st; // A ST to map synsets noun synsets id.
    private SeparateChainingHashST<Integer, String> rst; // A ST to map noun to a set oof IDS.
    private ShortestCommonAncestor sca; // For shortest common ancestor computation.

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) 
            throw new NullPointerException("synsets is null");
        }
        if (hypernyms == null) {
            throw new NullPointerException("hypernyms is null");
        }
        this.st = new SeparateChainingHashST<>();
        this.rst = new SeparateChainingHashST<>();
        In in1 = new In(synsets);
        In in2 = new In(hypernyms);
        String[] lines = in1.readAllLines();
        for (String line : lines) {
            String[] a = line.split(",");
            String[] b = a[1].split(" ");
            rst.put(Integer.parseInt(a[0]), a[1]);
            for (int i = 0; i < b.length; i++) {
                if (!st.contains(b[i])) {
                    st.put(b[i], new Set<Integer>());
                }
                st.get(b[i]).add(Integer.parseInt(a[0]));
            }
        }

        DiGraph G = new DiGraph(rst.size());
        String[] lines2 = in2.readAllLines();
        for (String line : lines2) {
            String[] a = line.split(",");
            for (int i = 1; i < a.length; i++) {
                G.addEdge(Integer.parseInt(a[0]), Integer.parseInt(a[i]));
            }
        }
        sca = new ShortestCommonAncestor(G);
    }

    public Iterable<String> nouns() {
        return st.keys();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException("word is null");
        }
        return st.contains(word);
    }

    public String sca(String noun1, String noun2) {
        if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        }
        if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        }
        if (!st.contains(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if (!st.contains(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }
        Set<Integer> seta = st.get(noun1);
        Set<Integer> setb = st.get(noun2);
        int a = sca.ancestor(seta, setb);
        return rst.get(a);
    }
    public int distance(String noun1, String noun2) {
        if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        }
        if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        }
        if (!st.contains(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if (!st.contains(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }
        Set<Integer> seta = st.get(noun1);

        Set<Integer> setb = st.get(noun2);
        return sca.length(seta, setb);

    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }
        StdOut.printf("# of nouns = %d\n", nouns);
        StdOut.printf("isNoun(%s)? %s\n", word1, wordnet.isNoun(word1));
        StdOut.printf("isNoun(%s)? %s\n", word2, wordnet.isNoun(word2));
        StdOut.printf("isNoun(%s %s)? %s\n", word1, word2, wordnet.isNoun(word1 + " " + word2));
        StdOut.printf("sca(%s, %s) = %s\n", word1, word2, wordnet.sca(word1, word2));
        StdOut.printf("distance(%s, %s) = %s\n", word1, word2, wordnet.distance(word1, word2));
    }
}
