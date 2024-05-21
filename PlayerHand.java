import java.util.*;

public class PlayerHand implements IPlayerHand {

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

    //are these the initial two cards
    private boolean canSurrender;
    private boolean canDouble = true;
    private boolean canSplit = true;
    private boolean hasSplitAces = false;

    //this will be false after splitting aces or doubling
    private boolean canHit = true;
    private boolean tookInsurance = false;

    //is hand waiting to be compared to dealer
    private boolean isWaiting = false;
    private double bet;
    private boolean isOver = false;
    private int numOfSplits = 0;

    public PlayerHand(double bet, boolean canSurrender) {
        this.cardsList = new LinkedList<>();
        this.bet = bet;
        this.canSurrender = canSurrender;
    }

    @Override
    public List<Card> getCardsList() {
        return this.cardsList;
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
        } else if ((aceCounter == 1) && (sum == 10)) {
            this.id = "AT";
            return this.id;
        } else if ((aceCounter == 1)) {
            this.id = Integer.toString(sum + 1);
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
    public boolean canSurrender() {
        return this.canSurrender;
    }

    @Override
    public void setCanSurrender(boolean canSurrender) {
        this.canSurrender = canSurrender;
    }

    @Override
    public boolean canDouble() {
        return this.canDouble;
    }

    @Override
    public void setCanDouble(boolean canDouble) {
        this.canDouble = canDouble;
    }

    @Override
    public boolean canSplit() {
        return this.canSplit;
    }

    @Override
    public void setCanSplit(boolean canSplit) {
        this.canSplit = canSplit;
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
    public double getBet() {
        return this.bet;
    }

    @Override
    public void setBet(double bet) {
        this.bet = bet;
    }

    @Override
    public int getNumOfSplits() {
        return this.numOfSplits;
    }

    @Override
    public void setNumOfSplits(int numOfSplits) {
        this.numOfSplits = numOfSplits;
    }

    @Override
    public boolean canHit() {
        return canHit;
    }

    @Override
    public void setCanHit(boolean canHit) {
        this.canHit = canHit;
    }

    @Override
    public boolean tookInsurance() {
        return this.tookInsurance;
    }

    @Override
    public void setTookInsurance(boolean tookInsurance) {
        this.tookInsurance = tookInsurance;
    }

    @Override
    public boolean hasSplitAces() {
        return this.hasSplitAces;
    }

    @Override
    public void setHasSplitAces(boolean hasSplitAces) {
        this.hasSplitAces = hasSplitAces;
    }

    @Override
    public boolean isOver() {
        return this.isOver;
    }

    @Override
    public void setIsOver(boolean isOver) {
        this.isOver = isOver;
    }
}

