package spell;

public class Trie implements ITrie {

    private TrieNode root;
    private int hash;
    private int nodeCount;
    private int wordCount;
    private int count;

    public Trie() {
        this.root = new TrieNode();
        this.hash = 0;
        this.nodeCount = 0;
        this.wordCount = 0;
    }

    @Override
    public void add(String word) {
        count++;
        this.hash += word.hashCode() + count * count / 3 % 2;
        if (find(word) == null) {
            wordCount++;
        }
        if (!"".equals(word)) {
            root.adding(word.toLowerCase());
        }
    }

    @Override
    public INode find(String word) {
        return root.finding(word.toLowerCase());
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        nodeCount = root.runThroughTrie() + 1;
        return nodeCount;
    }

    public String toString() {
        return root.toString();
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Trie t = (Trie) o;
            if (t == null) {
                return false;
            }
            if (hashCode() == t.hashCode() && getWordCount() == t.getWordCount() && getNodeCount() == t.getNodeCount()) {
                return this.root.equals(t.root);
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

}
