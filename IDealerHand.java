import java.util.List;

public interface IDealerHand extends IHand {

    /**
     *
     * @return holeCard
     */
    public Card getHoleCard();

    /**
     * set holeCard (forTesting)
     * @param holeCard the holeCard
     */
    public void setHoleCard(Card holeCard);

    /**
     *
     * @return upCard
     */
    public Card getUpCard();

    /**
     * set upCard (for testing)
     * @param upCard the upCard
     */
    public void setUpCard(Card upCard);
}