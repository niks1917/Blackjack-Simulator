import java.util.HashMap;

public interface IDealer {

    /**
     * initializes an empty DealerHand by calling
     * DealerHand constructor
     */
    public void initHand();

    /**
     * adds card to hand from the deck
     * and changes hand field values
     *
     * @param dealtCard the card dealt to the dealer
     *
     */
    public void hit(Card dealtCard);


    /**
     * changes hand status to isWaiting;
     *
     *
     */
    public void stand();

    /**
     * gets current hand
     *
     * @return current hand
     */
    public DealerHand getHand();

    /**
     * based on if the game is "S17" or "H17" the dealer
     * will hit or stand on their hand
     *
     * @param dealerHand the dealer's current hand
     *
     * @return The decision (either S or H)
     */
    public String makeHandDecision(DealerHand dealerHand);

    /**
     * is the dealer being tracked (always false
     * but needed for game class to skip over)
     *
     * @return the value of isTracked
     */
    public boolean isTracked();

    /**
     * get decision map
     *
     * @return decision map
     */
    public HashMap<String, String> getDecisionMap();


}
