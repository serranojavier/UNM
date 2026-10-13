import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Author: Javier Serrano
 * Date: 1/29/13
 */

public class Ghost  {
    private static final int HUMAN = 1;
    private static final int COMPUTER = 2;

    private Collection<String> wordList = new HashSet<String>();
    private Collection<String> prefixes = new HashSet<String>();

    /**
     * Game constructor, generates a Collection of possible words and prefixes.
     * @throws FileNotFoundException
     */
    public Ghost() throws FileNotFoundException {

        Scanner wordScanner = new Scanner(new File("enable1.txt"));

        while (wordScanner.hasNextLine()) {
            wordList.add(wordScanner.nextLine());
        }
        wordScanner.close();

        for (String word : wordList) {
            String prefix = "";
            for (int i = 0; i < (word.length() - 1); i++) {
                prefix += word.charAt(i);
                prefixes.add(prefix);
            }
        }
    }

    public static void main(String args []) throws FileNotFoundException {
        Ghost ghost = new Ghost();
        ghost.startGame();
    }

    private void startGame() {
        System.out.println("Enter the type of player for Player 1");
        int player1Type = chosePlayer();
        System.out.println("Enter the type of player for Player 2");
        int player2Type = chosePlayer();

        boolean humanVsHuman = (player1Type == HUMAN && player1Type == player2Type);
        boolean computerVsComputer = (player1Type == COMPUTER && player1Type == player2Type);

        boolean gameOver = false;

        String word = "";

        Player curPlayer;
        Player nextPlayer;
        if (humanVsHuman) {
            curPlayer = new HumanPlayer(1);
            nextPlayer = new HumanPlayer(2);
        } else if (computerVsComputer) {
            curPlayer = new ComputerPlayer(wordList, prefixes, 1);
            nextPlayer = new ComputerPlayer(wordList, prefixes, 2);
        } else {
            curPlayer = new HumanPlayer(1);
            nextPlayer = new ComputerPlayer(wordList, prefixes);
        }

        while (!gameOver) {
            System.out.println("\nIt's " + curPlayer.getName() + "'s turn.");
            System.out.println("The current word fragment is: " + word);
            word += curPlayer.getMove(word);
            System.out.println("The word fragment has changed to: " + word);

            if (wordList.contains(word)) {
                gameOver = true;
                System.out.println(word + " is a word. " + curPlayer.getName() + " loses.");
            } else if (!prefixes.contains(word)) {
                gameOver = true;
                System.out.println(word + " is not a valid prefix; " + curPlayer.getName() + " loses.");
            }

            Player temp = curPlayer;
            curPlayer = nextPlayer;
            nextPlayer = temp;
        }
    }

    private int chosePlayer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 1 for a human player or 2 for a computer player.");
        int player = scanner.nextInt();
        while (player != HUMAN && player != COMPUTER) {
            System.out.println("Please enter only 1 and 2.");
            System.out.println("Enter 1 for a human player or 2 for a computer player.");
            player = scanner.nextInt();
        }
        return player;
    }
}
