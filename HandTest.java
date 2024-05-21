import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class HandTest {
    private IHand dealer;
    private IHand player;
    private List<Card> cardList;

    @org.junit.Before
    public void setUp() throws Exception {
        dealer =  new DealerHand();
        cardList = new LinkedList<>();
    }

    @org.junit.Test
    public void evaluateValue() {
        // test "A,9,A,A"
        cardList.add(new Card("A"));
        cardList.add(new Card("9"));
        cardList.add(new Card("A"));
        cardList.add(new Card("A"));
        dealer.setCardList(cardList);
        assertEquals("12", dealer.getValue());
        // test "A,2,5"
        cardList.clear();
        dealer.addCard(new Card("A"));
        dealer.addCard(new Card("2"));
        dealer.addCard(new Card("5"));
        assertEquals("18", dealer.getValue());
        // test "A,2,5,6"
        cardList.clear();
        dealer.addCard(new Card("A"));
        dealer.addCard(new Card("2"));
        dealer.addCard(new Card("5"));
        dealer.addCard(new Card("6"));
        assertEquals("14", dealer.getValue());
        //test "AJ"
        cardList.clear();
        dealer.addCard(new Card("A"));
        dealer.addCard(new Card("J"));
        assertEquals("21", dealer.getValue());
        // test "3A"
        cardList.clear();
        dealer.addCard(new Card("3"));
        dealer.addCard(new Card("A"));
        assertEquals("14", dealer.getValue());
        assertEquals("A3", dealer.getId());
    }

    @org.junit.Test
    public void evaluateId() {
        // test "A,9,A,A"
        cardList.add(new Card("A"));
        cardList.add(new Card("9"));
        cardList.add(new Card("A"));
        dealer.setCardList(cardList);
        assertEquals("AT", dealer.getId());
        dealer.addCard(new Card("A"));
        assertEquals("12", dealer.getId());
        cardList.clear();
        //test AA
        dealer.addCard(new Card("A"));
        dealer.addCard(new Card("A"));
        assertEquals("AA", dealer.getId());
        //test 77
        cardList.clear();
        dealer.addCard(new Card("7"));
        dealer.addCard(new Card("7"));
        assertEquals("77", dealer.getId());
        assertEquals("14", dealer.getValue());
        //test 77
        cardList.clear();
        dealer.addCard(new Card("7"));
        dealer.addCard(new Card("7"));
        dealer.addCard(new Card("7"));
        assertEquals("21", dealer.getId());
        assertEquals("21", dealer.getValue());
        //test AT
        cardList.clear();
        dealer.addCard(new Card("A"));
        dealer.addCard(new Card("J"));
        assertEquals("AT", dealer.getId());
        //test A,5,A
        cardList.clear();
        dealer.addCard(new Card("A"));
        dealer.addCard(new Card("5"));
        dealer.addCard(new Card("A"));
        assertEquals("A6", dealer.getId());
        assertEquals("17", dealer.getValue());
        // test "A,2,3,A,A,2"
        cardList.clear();
        dealer.addCard(new Card("A"));
        dealer.addCard(new Card("2"));
        dealer.addCard(new Card("3"));
        dealer.addCard(new Card("A"));
        dealer.addCard(new Card("A"));
        dealer.addCard(new Card("2"));
        assertEquals("20", dealer.getValue());
        assertEquals("A9", dealer.getId());
        // test "A,2,3,A,A,3"
        cardList.clear();
        dealer.addCard(new Card("A"));
        dealer.addCard(new Card("2"));
        dealer.addCard(new Card("3"));
        dealer.addCard(new Card("A"));
        dealer.addCard(new Card("A"));
        dealer.addCard(new Card("3"));
        assertEquals("21", dealer.getValue());
        assertEquals("AT", dealer.getId());
        // test "3,2,3,A"
        cardList.clear();
        dealer.addCard(new Card("3"));
        dealer.addCard(new Card("2"));
        dealer.addCard(new Card("3"));
        dealer.addCard(new Card("A"));
        assertEquals("19", dealer.getValue());
        assertEquals("A8", dealer.getId());
    }
}