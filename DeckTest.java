import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeckTest {
    private IDeck deckNum;
    private IDeck deckDiscards;
    private Card[] cards;
    @Before
    public void setUp() throws Exception {
        deckNum = new Deck(1);
        cards = new Card[]{new Card("2"), new Card("3"),new Card("4"),new Card("5"),new Card("6"),
        new Card("7"),new Card("8"),new Card("9"),new Card("T"),new Card("A")};
        deckDiscards = new Deck(cards);
    }

    @Test
    public void shuffleDeck() {
        Card before = deckDiscards.dealCard();
        for (int i = 0; i < 10; i++) {
            deckDiscards.shuffleDeck();
        }
        Card after = deckDiscards.dealCard();
        assertNotEquals(before, after);

       before = deckNum.dealCard();
        for (int i = 0; i < 10; i++) {
            deckNum.shuffleDeck();
        }
        after = deckNum.dealCard();
        assertNotEquals(before, after);

    }
}