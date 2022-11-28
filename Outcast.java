import stdlib.StdIn;
import stdlib.StdOut;

public class Outcast {
    private WordNet wordnet; // The wordnet semantic lexicon.

    // Constructs an Outcast object given the WordNet semantic lexicon.
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // Returns the outcast noun from nouns.
    public String outcast(String[] nouns) {
        String outC = "";
        int maxDistance = 0;
        for (String i : nouns) {
            int tempdistance = 0;
            for (String j : nouns) {
                tempdistance += wordnet.distance(i, j);
                if (tempdistance > maxDistance) {
                    maxDistance = tempdistance;
                    outC = i;
                }


            }

        }
        // return the outC (ith noun).
        return outC;

    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        String[] nouns = StdIn.readAllStrings();
        String outcastNoun = outcast.outcast(nouns);
        for (String noun : nouns) {
            StdOut.print(noun.equals(outcastNoun) ? "*" + noun + "* " : noun + " ");
        }
        StdOut.println();
    }
}
