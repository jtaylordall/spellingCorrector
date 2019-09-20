package spell;

import java.lang.reflect.Array;
import java.util.Vector;

public class TrieNode implements INode {

    private TrieNode[] nodes;
    private int count;

    TrieNode() {
        this.count = 0;
        this.nodes = new TrieNode[26]; // Array initialized with a slot for every letter in the alphabet
    }

    // Recursively traverses Trie to add indications of a word's appearance, adding branching connections as needed
    void adding(String word) {
        if (!"".equals(word)) { // Non-empty strings indicate the string has not reached the end
            char first = word.charAt(0);
            if ((int) first - 'a' < 0 || (int) first - 'a' > 26) {
                return;
            }
            if (nodes[first - 'a'] == null) { // Initializes new branch if necessary
                nodes[first - 'a'] = new TrieNode();
            }
            if (word.length() > 1) { // Recursively calls the method to add the next character
                nodes[first - 'a'].adding(word.substring(1));
            } else {
                nodes[first - 'a'].adding(""); // Empty string indicates the end of the string and count will increment
            }
        } else {
            count++; // End-of-word condition, one increment of count happens anytime the word is found in document
        }
    }

    //Recursively traverses Tree fo find a particular word, if found returns the node, else returns null
    TrieNode finding(String word) {
        TrieNode found;
        char first = word.charAt(0);
        if (nodes[first - 'a'] == null) { // if branch does not exist, returns null (not found)
            return null;
        }
        if (word.length() > 1) { // Recursively calls method to continue searching the Trie
            found = nodes[first - 'a'].finding(word.substring(1));
        } else { // Empty string indicates end of word
            if (nodes[first - 'a'].getValue() > 0) {
                return nodes[first - 'a'];
            } else {
                return null;
            }
        }
        return found;
    }

    // Constructs a string of all the words in the Trie, only called by root
    @Override
    public String toString() {
        Vector<String> v = toStringHelp(this, new StringBuilder(""));
        StringBuilder sb = new StringBuilder();
        for (String s : v) {
            sb.append(s + "\n");
        }
        return sb.toString();
    }

    public Vector<String> toStringHelp(TrieNode root, StringBuilder word) {
        Vector<String> v = new Vector<>();
        if (root == null) {
            return v;
        }
        for (int a = 0; a < 26; a++) {
            if (root.nodes[a] != null) {
                StringBuilder prep = new StringBuilder();
                prep.append(word);
                prep.append((char) (a + 'a'));
                if (root.nodes[a].getValue() > 0) {
                    v.add(prep.toString());
                }
                v.addAll(toStringHelp(root.nodes[a], prep));
            }
        }
        return v;
    }

    // Compares TrieNode connections to see if they match, checks both Tries in parallel
    boolean equals(TrieNode comp) {
        for (int a = 0; a < 26; a++) {
            if (nodes[a] != null && comp.nodes[a] != null) { // branches match
                if (!nodes[a].equals(comp.nodes[a])) { // recursively checks next nodes
                    return false;
                }
            } else if (nodes[a] != null && comp.nodes[a] == null) { // branch does not match
                return false;
            } else if (nodes[a] == null && comp.nodes[a] != null) { // branch does not match
                return false;
            } else if (getValue() != comp.getValue()) { // count values do not match, so different number of duplicates
                return false;
            }
        }
        return true;
    }

    // Returns count value -- how many times the word appeared in the file
    @Override
    public int getValue() {
        return count;
    }

    int runThroughTrie() {
        int getNodeCount = 0;
        for (int a = 0; a < 26; a++) {
            if (nodes[a] != null) { // branches match
                getNodeCount++;
                getNodeCount += nodes[a].runThroughTrie();
            }
        }
        return getNodeCount;
    }
}
