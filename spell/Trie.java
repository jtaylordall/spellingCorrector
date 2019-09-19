package spell;

public class Trie implements ITrie {

    public TrieNode root;
    public String out;
    private int hash;
    private int count;

    public Trie() {
        this.root = new TrieNode();
        this.hash = 0;
        this.count = 0;
    }

    @Override
    public void add(String word) {
        count++;
        this.hash += word.hashCode() + count * count / 3 % 2;
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
        return 0;
    }

    @Override
    public int getNodeCount() {
        return count;
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
        Trie t = (Trie) o;
        if (this.hash == t.hashCode()) {
            return this.root.equals(t.root);
        }
        return false;
    }
}
