public interface IDeck {

    /**
     * deals card at deck[dealtCounter]
     *
     * @return the Card object dealt
     */
    public Card dealCard();

    /**
     *
     * shuffles the deck
     */
    public void shuffleDeck();

    /**
     *
     * @return number of decks in deck
     */
    public int getNumOfDecks();
}
