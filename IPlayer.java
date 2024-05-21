import java.util.HashMap;
import java.util.List;

public interface IPlayer {


    /**
     * adds card to player's current hand from the deck
     * and changes hand field values
     *
     * @param dealtCard the card dealt to the player
     * @param indexOfHand index of the current hand
     *                    in the hand list
     *
     */
    public void hit(Card dealtCard, int indexOfHand);

    /**
     * changes hand status to isWaiting;
     *
     * @param indexOfHand index of the current hand
     *                    in the hand list
     *
     */
    public void stand(int indexOfHand);

    /**
     * adds card to player's current hand from the deck
     * and changes hand field values
     *
     * @param dealtCard the card dealt to the player
     * @param indexOfHand index of the current hand
     *                    in the hand list
     *
     */
    public void doubleDown(Card dealtCard, int indexOfHand);

    /**
     * sets hand to isOver
     *
     * @param indexOfHand index of the current hand
     *                    in the hand list
     *
     */
    public void surrender(int indexOfHand);

    /**
     * true if player's performance is being tracked by sim
     *
     * @return isTracked value
     */
    public boolean isTracked();

    /**
     * get player's decision map
     *
     * @return decision map
     */
    public HashMap<String, String> getDecisionMap();

    /**
     * get player's bet spread
     *
     * @return bet spread
     */
    public double[][] getBetSpread();

    /**
     * Calls PlayerHand constructor and initializes the handsList
     * as some number of empty hands with some bet value specified
     * by the bet spread and true count. Deducts the total of all
     * bets from the bankroll. Unless, if bet is 0 do nothing
     *
     * @param trueCount the current true count as calculated by Game
     *
     */
    public void placeBets(int trueCount);

    /**
     * gets the handList
     *
     * @return the current handList
     */
    public List<PlayerHand> getHandsList();

    /**
     * gets the hand at the given index
     *
     * @param index the index of the hand in the handsList
     * @return the hand
     */
    public PlayerHand getHand(int index);

    /**
     * using the current hand and the true count
     * make a decision about what to do with the hand
     *
     * @param playerHand the current player hand
     * @param dealerHand the dealer's up card
     * @param trueCount the current true count as calculated by Game
     * @param runningCount the current running count
     *
     * @return the decision as a string (H,S,D,P, or U)
     */
    public String makeHandDecision(PlayerHand playerHand, DealerHand dealerHand, int trueCount, int runningCount);

    /**
     *
     * @return player position
     */
    public int getPlayerPosition();

    /**
     * @return current bankroll
     */
    public double getBankroll();

    /**
     * updates the bankroll number
     * @param bankroll the new bankroll number
     */
    public void setBankroll(double bankroll);

    /**
     * clears hands list
     */
    public void removeHands();

    /**
     * @return if this player is a counter
     */
    public boolean isCounter();

}
