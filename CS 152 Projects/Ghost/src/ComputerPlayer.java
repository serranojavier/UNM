/*
 * Author: Javier Serrano
 * Date: 1/30/13
 */
import java.util.Collection;
import java.util.Random;

public class ComputerPlayer extends Player {

    private static final int ALPHABET_LENGTH = 26;
    private static final int FIRST_LETTER = 65;
    private Collection<String> wordList;
    private Collection<String> prefixes;
    private String[] alphabet = new String[ALPHABET_LENGTH];
    private int computerNumber = 0;

    Random random = new Random();

    /**
     * Creates a new Computer player object.
     * @param wordList The list of all possible words.
     * @param prefixes The list of all possible prefixes.
     */
    ComputerPlayer(Collection<String> wordList, Collection<String> prefixes) {
        this.wordList = wordList;
        this.prefixes = prefixes;

        // Generate an alphabet, need to cast int value to 'char' then to 'String'
        for (int i = 0; i < ALPHABET_LENGTH; i++) {
            alphabet[i] = Character.toString((char) (i + FIRST_LETTER));
            alphabet[i] = alphabet[i].toLowerCase();
        }
    }

    /**
     * Generates a new Computer player object.
     * @param wordList The list of all possible words.
     * @param prefixes The list of all possible prefixes.
     * @param computerNumber An <code>int</code> value to identify different instances.
     */
    ComputerPlayer(Collection<String> wordList, Collection<String> prefixes, int computerNumber) {
        this.wordList = wordList;
        this.prefixes = prefixes;
        this.computerNumber = computerNumber;

        // Generate an alphabet, need to cast int value to 'char' then to 'String'
        for (int i = 0; i < ALPHABET_LENGTH; i++) {
            alphabet[i] = Character.toString((char) (i + FIRST_LETTER));
            // All prefixes and words in enable1 file are in lowercase.
            alphabet[i] = alphabet[i].toLowerCase();
        }
    }

    /**
     * Returns the computer's move. If the word fragment is empty, this method returns a random letter of the
     * alphabet. When it is not empty it iterates through the alphabet and finds a letter that is a valid prefix if
     * possible, otherwise it completes a word.
     * @param word The current word fragment.
     * @return A character as a <code>String</code> of length 1.
     */
    @Override
    public String getMove(String word) {

        // I just simply added this so that the game runs at 3 second intervals instead of terminating immediately
        // when the user selects both players to be computers.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String move = "";

        // It's the first move, guess a random letter
        if (word.isEmpty()) {
            return alphabet[random.nextInt(ALPHABET_LENGTH)];
        }

        // Guess letter that is a valid prefix but does not complete a word
        boolean letterFound = false;
        while (!letterFound) {
            move = "";
            move += alphabet[random.nextInt(ALPHABET_LENGTH)];
            if (prefixes.contains(word + move)) {
                letterFound = true;
            } else if (wordList.contains(word + move)) {
                letterFound = true;
            }
        }
        return move;
    }

    /**
     * Returns the computer's name.
     * @return Computer's name as a <code>String</code>
     */
    @Override
    public String getName() {
        if (computerNumber == 0) {
            return "Computer";
        } else {
            return "Computer " + computerNumber;
        }
    }
}