import dsa.DiGraph;
import dsa.SeparateChainingHashST;
import dsa.Set;
import stdlib.In;
import stdlib.StdOut;

public class WordNet {
    private SeparateChainingHashST<String, Set<Integer>> st; // A ST to map synsets noun synsets id.
    private SeparateChainingHashST<Integer, String> rst; // A ST to map noun to a set oof IDS.
    private ShortestCommonAncestor sca; // For shortest common ancestor computation.

    // Constructs a WordNet object given the names of the input (synset and hypernym) files.
    public WordNet(String synsets, String hypernyms) {
        // If synsets is null, then throw  NPE saying that the synsets is null.
        if (synsets == null) {
            throw new NullPointerException("synsets is null");
        }
        // If hypernyms is null, then throw a NPE saying that the hypernyms is null.
        if (hypernyms == null) {
            throw new NullPointerException("hypernyms is null");
        }
        // Initialise the instance variable st.
        this.st = new SeparateChainingHashST<>();
        // Initialise the instance variable rst.
        this.rst = new SeparateChainingHashST<>();
        // Creating an instream from the two file provided - synsets and hypernyms.
        In in1 = new In(synsets);
        In in2 = new In(hypernyms);
        // Read all the lines of the synsets file and store it an array called lines.
        String[] lines = in1.readAllLines();
        // For all the line in lines...
        for (String line : lines) {
            // Split the line by the comma(,) as a delimiter.
            String[] a = line.split(",");
            // Split the second element of the array a by space ( ) as the delimiter.
            String[] b = a[1].split(" ");
            // Add the synsets id(a[0]) and synsets noun(a[2]) as the key value pair in the rst.
            rst.put(Integer.parseInt(a[0]), a[1]);
            // for all the elements(synsets noun) in the array b...
            for (int i = 0; i < b.length; i++) {
                // if the noun is already in the st, then add the corresponding synsets id
                // to the set. Otherwise, add the noun in the st with a new set and then add the
                // corresponding synsets id to the set.
                if (!st.contains(b[i])) {
                    st.put(b[i], new Set<Integer>());
                }
                st.get(b[i]).add(Integer.parseInt(a[0]));
            }
        }


        // In order to initialise sca which take digraph as an argument, creating a digraph object G
        DiGraph G = new DiGraph(rst.size());

        // Reading all the lines of the hypernyms file and storing the lined in an array lies2.
        String[] lines2 = in2.readAllLines();
        // For all the line in the lines2...
        for (String line : lines2) {
            // split the line by a comma(,) as the delimiter and store the elements into an array a.
            String[] a = line.split(",");
            // For all the elements in the array a...
            for (int i = 1; i < a.length; i++) {
                // Add an edge from the first element of the array to its corresponding next
                // element.
                G.addEdge(Integer.parseInt(a[0]), Integer.parseInt(a[i]));
            }
        }

        // Initialise the sca instance variable by passing the digraph G as an argument
        // created above.
        sca = new ShortestCommonAncestor(G);
    }

    // Returns all WordNet nouns.
    public Iterable<String> nouns() {
        // All the keys in the st are nouns. So simply return the keys of the st.
        return st.keys();
    }

    // Returns true if the given word is a WordNet noun, and false otherwise.
    public boolean isNoun(String word) {
        // If word is null, then throw a NPE with a message saying that the word in null.
        if (word == null) {
            throw new NullPointerException("word is null");
        }
        // If the st contains the word, then it is a noun. Otherwise, it is not.
        return st.contains(word);
    }

    // Returns a synset that is a shortest common ancestor of noun1 and noun2.
    public String sca(String noun1, String noun2) {
        // If noun1 is null, then throw a NPE with a message saying that noun1 is null.
        if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        }
        // If noun2 is null, then throw a NPE with a message saying that noun2 is null.
        if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        }
        // If st does not contain the noun1, then it is noun and hence throw an IAE
        // with a message saying that the given noun1 is not a noun.
        if (!st.contains(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        // If st does not contain the noun2, then it is noun and hence throw an IAE
        // with a message saying that the given noun2 is not a noun
        if (!st.contains(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }
        // St.get(noun1) will return a set with all the synsets ids that it belongs.
        Set<Integer> seta = st.get(noun1);
        // St.get(noun2) will return a set with all the synsets ids that it belongs
        Set<Integer> setb = st.get(noun2);
        // Calling the ancestor method on the vertex subset seta and setb to find the shortest
        // common ancestor.
        int a = sca.ancestor(seta, setb);
        // As we have to return the synset and not synset id, we do rst.get(a) as it will
        // return the synsets noun. Thus, I return it at the end.
        return rst.get(a);
    }

    // Returns the length of the shortest ancestral path between noun1 and noun2.
    public int distance(String noun1, String noun2) {
        // If noun1 is null, then throw a NPE saying that noun1 is null.
        if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        }
        // If noun2 is null, then throw a NPE saying that noun2 is null.
        if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        }
        // If st does not contain the noun1, then it is noun and hence throw an IAE
        // saying that the given noun1 is not a noun
        if (!st.contains(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        // If st does not contain the noun2, then it is noun and hence throw an IAE
        // saying that the given noun2 is not a noun
        if (!st.contains(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }
        // St.get(noun1) will return a set with all the synsets ids that it belongs.
        Set<Integer> seta = st.get(noun1);

        // St.get(noun2) will return a set with all the synsets ids that it belongs.
        Set<Integer> setb = st.get(noun2);
        // Calling the length method from the sca to find out the shortest length of the
        // common ancestral path and return it at the end.
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
