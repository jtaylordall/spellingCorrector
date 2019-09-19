package spell;

import java.io.IOException;

/**
 * A simple main class for running the spelling corrector. corrector class is not
 * used by the passoff program.
 */
public class Main {

    /**
     * Give the dictionary file name as the first argument and the word to correct
     * as the second argument.
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            String dictionaryFileName = args[0];
            String inputWord = args[1];

            ISpellCorrector corrector = new SpellCorrector();
            ISpellCorrector corrector2 = new SpellCorrector();

            corrector.useDictionary(dictionaryFileName);
			corrector2.useDictionary(dictionaryFileName);
			String suggestion = corrector.suggestSimilarWord(inputWord);
            if (suggestion == null) {
                suggestion = "No similar word found";
            }

            System.out.println("Suggestion is: " + suggestion);
        }
    }

}
