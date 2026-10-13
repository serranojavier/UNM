import java.util.Scanner;

/**
 * Author: Javier Serrano
 * Date: 1/30/13
 */
public class HumanPlayer extends Player {

    private int playerNum;

    /**
     * Creates a new HumanPlayer object.
     * @param playerNum The player number.
     */
    HumanPlayer(int playerNum) {
        this.playerNum = playerNum;
    }

    /**
     * Returns the player's next move
     * @return A character as a <code>String</code> of length 1.
     */
    @Override
    public String getMove(String word) {
        Scanner scanner = new Scanner(System.in);
        return String.valueOf(scanner.nextLine().charAt(0));
    }

    /**
     * Return the players name.
     * @return Player's name as  a <code>String</code>
     */
    @Override
    public String getName() {
        return "Player " + playerNum;
    }
}