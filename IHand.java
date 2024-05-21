import java.util.List;

public interface IHand {
    /**
     *
     * @return cardsList for hand
     */
    public List<Card> getCardsList();

    /**
     * set cardsList (for testing)
     *
     * @param cardsList the cards in the hand
     */
    public void setCardList(List<Card> cardsList);

    /**
     * adds card to cardList
     *
     * @param card the dealt card
     */
    public void addCard(Card card);

    /**
     * evaluate the value of the hand based on
     * the values of each card (conditional on Aces)
     *
     * @return hand value
     */
    public String evaluateValue();

    /**
     *
     * @return hand value
     */
    public String getValue();

    /**
     * set hand value
     *
     * @param value the hand value
     */
    public void setValue(int value);

    /**
     * evaluate the id of the hand based on the
     * value of the hand and id's of the cards
     * (conditional on if there are Aces)
     *
     * @return hand id
     */
    public String evaluateId();

    /**
     *
     * @return hand id
     */
    public String getId();

    /**
     * set hand id
     *
     * @param id the id of the hand
     */
    public void setId(String id);
    /**
     *
     * @return value of isWaiting
     */
    public boolean isWaiting();

    /**
     * set whether this hand is waiting to be compared
     * to the players (true if hasn't busted and didn't
     * have blackjack)
     *
     * @param isWaiting whether the hand is waiting
     */
    public void setIsWaiting(boolean isWaiting);

    /**
     * @return whether the hand has busted
     */
    public boolean isOver();

    /**
     * @param isOver whether the hand busted
     */
    public void setIsOver(boolean isOver);
}
