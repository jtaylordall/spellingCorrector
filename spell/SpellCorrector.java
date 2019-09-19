package spell;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    public Collection<Edit> edits;
    public Collection<Edit> edits2;

    public Collection<Edit> suggestions;
    public Trie dictionary;

    public SpellCorrector() {
        this.edits = new TreeSet<>();
        this.edits2 = new TreeSet<>();
        this.suggestions = new TreeSet<>();
        this.dictionary = new Trie();
    }

    public void printEdits() {
        for (Edit edit : edits) {
            System.out.println(edit.toString());
        }
    }

    public Trie getDictionary() {
        return dictionary;
    }

    public void printSuggestions() {
        for (Edit edit : suggestions) {
            System.out.println(edit.toString());
        }
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        try (Scanner scan = new Scanner(new File(dictionaryFileName))) {
            while (scan.hasNext()) {
                dictionary.add(scan.next());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        this.edits = new TreeSet<>();
        this.edits2 = new TreeSet<>();
        this.suggestions = new TreeSet<>();
        if ("".equals(inputWord)) {
            return null;
        }
        if (dictionary.find(inputWord) != null) {
            return inputWord.toLowerCase();
        }
        int distance = 1;
        edits2.add(new Edit(inputWord.toLowerCase(), 0));
        generateEdits(inputWord, distance);
        boolean found = searchEdits();
        edits.addAll(edits2);
        edits2 = new TreeSet<>();
        if (!found) {
            distance++;
            generateMoreEdits(distance);
            edits.addAll(edits2);
            searchEdits();
        }

        //printEdits();

        if (suggestions.size() > 0) {
            //printSuggestions();
            String s = suggestions.iterator().next().getWord();
            suggestions = null;
            edits = null;
            return s;
        } else {
            return null;
        }
    }

    public boolean searchEdits() {
        for (Edit edit : edits) {
            INode n = dictionary.find(edit.getWord());
            if (n != null) {
                edit.setCount(n.getValue());
                suggestions.add(edit);
            }
        }
        return suggestions.size() > 0;
    }

    public void generateEdits(String inputWord, int distance) {
        deletion(inputWord, distance);
        transposition(inputWord, distance);
        alteration(inputWord, distance);
        insertion(inputWord, distance);
    }

    public void generateMoreEdits(int distance) {
        Vector<String> v = new Vector<>();
        for (Edit edit : edits) {
            v.add(edit.getWord());
        }
        for (String s : v) {
            generateEdits(s, distance);
        }
        v = null;
    }

    public void deletion(String inputWord, int distance) {
        if (inputWord.length() > 1) {
            for (int a = 0; a < inputWord.length(); a++) {
                String sub;
                if (a == 0) {
                    sub = inputWord.substring(1);
                } else if (a == inputWord.length() - 1) {
                    sub = inputWord.substring(0, inputWord.length() - 1);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(inputWord.substring(0, a));
                    sb.append(inputWord.substring(a + 1));
                    sub = sb.toString();
                }
                edits2.add(new Edit(sub, distance));
            }
        }
    }

    public void alteration(String inputWord, int distance) {
        for (int a = 0; a < inputWord.length(); a++) {
            char here = inputWord.charAt(a);
            for (int b = 0; b < 26; b++) {
                StringBuilder sb = new StringBuilder();
                if ((b + 'a') != (int) here) {
                    if (a == 0) {
                        sb.append((char) (b + 'a'));
                        sb.append(inputWord.substring(1));
                    } else if (a == inputWord.length() - 1) {
                        sb.append(inputWord.substring(0, inputWord.length() - 1));
                        sb.append((char) (b + 'a'));
                    } else {
                        sb.append(inputWord.substring(0, a));
                        sb.append((char) (b + 'a'));
                        sb.append(inputWord.substring(a + 1));
                    }
                    String alter = sb.toString();
                    edits2.add(new Edit(alter, distance));
                }
            }
        }
    }

    public void insertion(String inputWord, int distance) {
        /*if ("ye".equals(inputWord)) {
            System.out.print("---" + inputWord);
        }*/
        for (int b = 0; b < 26; b++) {
        StringBuilder sb;
        String ins = "";
        sb = new StringBuilder();
        sb.append((char) (b + 'a'));
        sb.append(inputWord);
        ins = sb.toString();
        edits2.add(new Edit(ins, distance));
        for (int a = 0; a < inputWord.length(); a++) {
                if (a < inputWord.length()) {
                    sb = new StringBuilder();
                    sb.append(inputWord.substring(0, a));
                    sb.append((char) (b + 'a'));
                    sb.append(inputWord.substring(a));
                    ins = sb.toString();
                    edits2.add(new Edit(ins, distance));
                }
                if (a == inputWord.length() - 1 || inputWord.length() == 2) {
                    sb = new StringBuilder();
                    sb.append(inputWord);
                    sb.append((char) (b + 'a'));
                    ins = sb.toString();
                    edits2.add(new Edit(ins, distance));

                }
                edits2.add(new Edit(ins, distance));
            }
        }
    }


    public void transposition(String inputWord, int distance) {
        if (inputWord.length() >= 3) {
            for (int a = 0; a < inputWord.length() - 1; a++) {
                char here = inputWord.charAt(a);
                char next = inputWord.charAt(a + 1);
                StringBuilder sb = new StringBuilder();
                if (a == 0) {
                    sb.append(next);
                    sb.append(here);
                    sb.append(inputWord.substring(a + 2));
                } else if (a == inputWord.length() - 1) {
                    sb.append(inputWord.substring(0, inputWord.length() - 2));
                    sb.append(next);
                    sb.append(here);
                } else {
                    sb.append(inputWord.substring(0, a));
                    sb.append(next);
                    sb.append(here);
                    sb.append(inputWord.substring(a + 2));
                }
                String trans = sb.toString();
                edits.add(new Edit(trans, distance));
            }
        } else if (inputWord.length() == 2) {
            StringBuilder sb = new StringBuilder();
            char here = inputWord.charAt(0);
            char next = inputWord.charAt(1);
            sb.append(next);
            sb.append(here);
            String trans = sb.toString();
            edits.add(new Edit(trans, distance));
        }
    }


}
