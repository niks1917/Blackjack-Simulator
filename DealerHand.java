import java.util.LinkedList;
import java.util.List;

public class DealerHand implements IDealerHand {
    private List<Card> cardsList;

    //string id of the hand represented as
    //a two character string for look up in
    //strategy map (conditional on if curr hand
    //has an ace)
    private String id;

    //String value of current hand based on
    //total of values of current hand
    //conditional on if there is an Ace
    private String value;
    private Card holeCard;
    private Card upCard;
    private boolean isWaiting = false;
    private boolean isOver = false;

    public DealerHand() {
        this.cardsList = new LinkedList<>();
    }


    @Override
    public List<Card> getCardsList() {
        return cardsList;
    }

    @Override
    public void setCardList(List<Card> cardsList) {
        this.cardsList = cardsList;
        evaluateId();
        evaluateValue();
    }

    @Override
    public void addCard(Card card) {
        this.cardsList.add(card);
        evaluateId();
        evaluateValue();
    }

    @Override
    public String evaluateValue() {
        int totalValue = 0;
        int aceCount = 0;

        for (int i = 0; i < cardsList.size(); i++) {
            String currentID = cardsList.get(i).getID();
            int currentValue = Integer.parseInt(cardsList.get(i).getValue());
            totalValue += currentValue;
            if (currentID.equals("A")) {
                aceCount ++;
            }
        }

        while (totalValue > 21 && aceCount > 0) {
            totalValue -= 10;  // Convert an Ace from 11 to 1 if value > 21.
            aceCount--;
        }

        this.value = Integer.toString(totalValue);
        return value;
    }

    @Override
    public String getValue() {
        return this.value;
    }


    @Override
    public void setValue(int value) {
        this.value = Integer.toString(value);
    }

    @Override
    public String evaluateId() {
        int sum = 0;
        //split
        if ((cardsList.size() == 2) && (cardsList.get(0).getID().equals(cardsList.get(1).getID()))) {
            String firstID = cardsList.get(0).getID();
            if ((firstID.equals("J")) || (firstID.equals("Q")) || (firstID.equals("K"))) {
                return "TT";
            }
            this.id = firstID + firstID;
            return id;
        }
        int aceCounter = 0;
        for (int i = 0; i < cardsList.size(); i++) {
            String currentID = cardsList.get(i).getID();
            if (!(currentID.equals("A"))) {
                sum += Integer.parseInt(cardsList.get(i).getValue());
            } else {
                aceCounter++;
            }
        }

        if (aceCounter > 1) {
            while (aceCounter != 1) {
                sum++;
                aceCounter--;
            }
        }

        if ((aceCounter == 1) && (sum < 10)) {
            this.id = "A" + sum;
            return this.id;
        } else if ((aceCounter == 1) && (sum == 10)){
            this.id = "AT";
            return this.id;
        } else if ((aceCounter == 1)){
            this.id = Integer.toString(sum+1);
            return this.id;
        } else {
            this.id = Integer.toString(sum);
            return this.id;
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean isWaiting() {
        return isWaiting;
    }

    @Override
    public void setIsWaiting(boolean isWaiting) {
        this.isWaiting = isWaiting;
    }

    @Override
    public Card getHoleCard() {
        return this.holeCard;
    }

    @Override
    public void setHoleCard(Card holeCard) {
        this.holeCard = holeCard;
    }

    @Override
    public Card getUpCard() {
        return this.upCard;
    }

    @Override
    public void setUpCard(Card upCard) {
        this.upCard = upCard;
    }

    @Override
    public boolean isOver() {
        return isOver;
    }

    @Override
    public void setIsOver(boolean isOver) {
        this.isOver = isOver;
    }
}