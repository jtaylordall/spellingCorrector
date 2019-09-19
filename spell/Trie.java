package spell;

public class Trie implements ITrie {

    public TrieNode root;
    public String out;
    public int hash;
    public int nodeCount;
    public int wordCount;

    public Trie() {
        this.root = new TrieNode();
        this.hash = 0;
        this.nodeCount = 0;
        this.wordCount = 0;
    }

    @Override
    public void add(String word) {
        wordCount++;
        this.hash += word.hashCode() + wordCount * wordCount / 3 % 2;
        if (find(word) == null){
            nodeCount++;
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
        return nodeCount;
    }

    public String toString() {
        return root.toString();
    }

    @Override
    public int hashCode() {
        return hash;
    }

    //@Override
    public boolean equals(Object o) {
        Trie t = (Trie) o;
        System.out.println(this.hash == t.hashCode());
        System.out.println(this.wordCount == t.getWordCount());
        System.out.println(this.nodeCount == t.getNodeCount());

        if (hashCode() == t.hashCode() && getWordCount() == t.getWordCount() && getNodeCount() == t.getNodeCount()) {
            return this.root.equals(t.root);
        }
        return false;
    }
}
