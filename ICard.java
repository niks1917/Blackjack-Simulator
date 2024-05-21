public interface ICard {

    /**
     * gets the value of a card
     *
     * @return the integer value of the card
     */
    public String getValue();

    /**
     * sets the value of a card (used to convert
     * Aces to a value of 1 based on rest of hand)
     *
     * @param value the value of the card
     */
    public void setValue(int value);

    /**
     * get the identifier for the card (2,3,4...J,Q,K,A)
     * for strategy lookup
     *
     * @return the id of the card
     */
    public String getID();

    /**
     * @return value of isHoleCard
     */
    public boolean isHoleCard();

    /**
     * set a card to be the dealer's hole card
     *
     * @param isHoleCard the truth value of whether it is a hole card
     */
    public void setHoleCard(boolean isHoleCard);
}
