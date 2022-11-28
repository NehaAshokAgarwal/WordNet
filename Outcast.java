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
        // set a variable outC to an empty String to keep track of the outcast.
        String outC = "";
        // Set a variable maxDistance to 0 to keep track of the max distance.
        int maxDistance = 0;
        // for all the ith nouns in the array nouns...
        for (String i : nouns) {
            // Set a variable tempDistance to 0.
            int tempdistance = 0;
            // for the jth noun in the array nouns...
            for (String j : nouns) {
                // compute the shortest ancestral distance between the ith noun and the jth noun.
                tempdistance += wordnet.distance(i, j);
                // if the tempDistance turns out to be bigger than the current maxDistance, then
                // update the value od the maxDistance.
                if (tempdistance > maxDistance) {
                    maxDistance = tempdistance;
                    // set outCast to ith noun.
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
