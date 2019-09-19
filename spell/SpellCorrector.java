package spell;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    private Collection<Edit> edits;
    private Collection<Edit> suggestions;
    private Trie dictionary;

    public SpellCorrector() {
        this.edits = new TreeSet<>();
        this.suggestions = new TreeSet<>();
        this.dictionary = new Trie();
    }

    private void printEdits() {
        for (Edit edit : edits) {
            System.out.println(edit.toString());
        }
    }

    private void printSuggestions() {
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
        if ("".equals(inputWord)){
            return null;
        }
        if (dictionary.find(inputWord) != null) {
            return inputWord.toLowerCase();
        }
        int distance = 1;
        generateEdits(inputWord, distance);
        boolean found = searchEdits();
        //edits.add(new Edit(inputWord, 0));
        if(!found){
            distance++;
            generateMoreEdits(distance);
            searchEdits();
        }
        printEdits();
        //printSuggestions();
        Iterator iterator = suggestions.iterator();
        if (suggestions.size() > 0) {
            return suggestions.iterator().next().getWord();
        } else {
            return null;
        }
    }

    private boolean searchEdits() {
        for (Edit edit : edits) {
            INode n = dictionary.find(edit.getWord());
            if (n != null) {
                edit.setCount(n.getValue());
                suggestions.add(edit);
            }
        }
        return suggestions.size() > 0;
    }

    private void generateEdits(String inputWord, int distance) {
        deletion(inputWord, distance);
        transposition(inputWord, distance);
        alteration(inputWord, distance);
        insertion(inputWord, distance);
    }

    private void generateMoreEdits(int distance) {
        Vector<String> v = new Vector<>();
        for (Edit edit : edits){
            v.add(edit.getWord());
        }
        for(String s : v) {
            generateEdits(s, distance);
        }
    }

    private void deletion(String inputWord, int distance) {
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
                edits.add(new Edit(sub, distance));
            }
        }
    }

    private void alteration(String inputWord, int distance) {
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
                    edits.add(new Edit(alter, distance));
                }
            }
        }
    }

    private void insertion(String inputWord, int distance) {
        for (int a = 0; a < inputWord.length(); a++) {
            char here = inputWord.charAt(a);
            for (int b = 0; b < 26; b++) {
                StringBuilder sb = new StringBuilder();
                if (a == 0) {
                    sb.append((char) (b + 'a'));
                    sb.append(inputWord);
                } else if (a == inputWord.length() - 1) {
                    sb.append(inputWord);
                    sb.append((char) (b + 'a'));
                } else {
                    sb.append(inputWord.substring(0, a));
                    sb.append((char) (b + 'a'));
                    sb.append(inputWord.substring(a));
                }
                String ins = sb.toString();
                edits.add(new Edit(ins, distance));
            }
        }
    }

    private void transposition(String inputWord, int distance) {
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
