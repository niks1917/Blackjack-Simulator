import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CounterPlayer implements IPlayer {

    private boolean isTracked = false;

    //all hand id's * every count between -2 and 7, inclusive
    //mapped to a decision
    private HashMap<String, String> decisionMap;

    //2D array of size 11 with bets correlated to the true count
    //(TC = index - 3)...bet = -1 if it should look at the
    //next array
    private double[][] betSpread;

    //position at table (1 is dealt to and plays first, then 2, etc.)
    private int position;
    private List<PlayerHand> handsList = null;

    //how much money the player has and
    //changes based on bets, wins, and loses
    private double bankroll;
    private boolean h17;
    private boolean surrender;
    private boolean rsa;

    //constructor
    public CounterPlayer(int position, double bankroll, double[][] betSpread,
                         boolean isTracked, HashMap<String, String> decisionMap,
                         boolean h17, boolean surrender, boolean rsa) {
        this.betSpread = betSpread;
        this.position = position;
        this.bankroll = bankroll;
        this.isTracked = isTracked;
        this.decisionMap = decisionMap;
        this.h17 = h17;
        this.surrender = surrender;
        this.rsa = rsa;
    }

    public CounterPlayer(){}

    @Override
    public void hit(Card dealtCard, int indexOfHand) {
        this.handsList.get(indexOfHand).addCard(dealtCard);
        this.handsList.get(indexOfHand).setCanDouble(false);
        this.handsList.get(indexOfHand).setCanSplit(false);
        this.handsList.get(indexOfHand).setCanSurrender(false);

    }

    @Override
    public void stand(int indexOfHand) {
        this.handsList.get(indexOfHand).setIsWaiting(true);
    }

    @Override
    public void doubleDown(Card dealtCard, int indexOfHand) {
        this.handsList.get(indexOfHand).addCard(dealtCard);
        double bet = this.handsList.get(indexOfHand).getBet();
        this.handsList.get(indexOfHand).setBet(bet * 2);
        this.bankroll = this.bankroll - bet;
        this.handsList.get(indexOfHand).setIsWaiting(true);
    }

    @Override
    public void surrender(int indexOfHand) {
        double bet = this.handsList.get(indexOfHand).getBet();
        this.bankroll = this.bankroll + (bet / 2);
        this.handsList.get(indexOfHand).setIsOver(true);
    }


    @Override
    public boolean isTracked() {
        return this.isTracked;
    }

    @Override
    public HashMap<String, String> getDecisionMap() {
        return this.decisionMap;
    }

    @Override
    public double[][] getBetSpread() {
        return this.betSpread;
    }

    @Override
    public void placeBets(int trueCount) {

        this.handsList = new ArrayList<>();

        int trueCountSquashed;
        if (trueCount > 7) {
            trueCountSquashed = 7;
        } else if (trueCount < -3) {
            trueCountSquashed = -3;
        } else {
            trueCountSquashed = trueCount;
        }

        int numOfHands = 0;
        double bet;
        if (this.betSpread[0][trueCountSquashed + 3] == 0) {
            bet = 0;
        } else if (this.betSpread[0][trueCountSquashed + 3] > 0) {
            bet = this.betSpread[0][trueCountSquashed + 3];
            numOfHands = 1;
        } else if (this.betSpread[0][trueCountSquashed + 3] == -1) {
            bet = this.betSpread[1][trueCountSquashed + 3];
            numOfHands = 2;
        } else {
            throw new IllegalArgumentException("invalid betspread");
        }

        this.bankroll = this.bankroll - (numOfHands * bet);

        for (int i = 0; i < numOfHands; i++) {
            PlayerHand hand = new PlayerHand(bet, this.surrender);
            this.handsList.add(hand);
        }


    }

    @Override
    public List<PlayerHand> getHandsList() {
        return this.handsList;
    }

    @Override
    public PlayerHand getHand(int index) {

        return this.handsList.get(index);
    }

    @Override
    public String makeHandDecision(PlayerHand playerHand, DealerHand dealerHand, int trueCount, int runningCount) {

        String playerHandID = playerHand.getId();
        String dealerHandID = dealerHand.getUpCard().getID();

        boolean pA8 = playerHandID.equals("A8");
        boolean pA7 = playerHandID.equals("A7");
        boolean pA2 = playerHandID.equals("A2");
        boolean p16 = playerHandID.equals("16");
        boolean p12 = playerHandID.equals("12");

        boolean d6 = dealerHandID.equals("6");
        boolean d2 = dealerHandID.equals("2");
        boolean d5 = dealerHandID.equals("5");
        boolean dT = dealerHandID.equals("T");
        boolean d4 = dealerHandID.equals("4");

        boolean pA4 = playerHandID.equals("A4");

        int trueCountSquashed;
        if (trueCount > 7) {
            trueCountSquashed = 7;
        } else if (trueCount < -3) {
            trueCountSquashed = -3;
        } else {
            trueCountSquashed = trueCount;
        }

        int runningCountSquashed;
        if (runningCount > 7) {
            runningCountSquashed = 7;
        } else if (runningCount < -3) {
            runningCountSquashed = -3;
        } else {
            runningCountSquashed = runningCount;
        }

        String strategyResult = "";

        if (this.h17 && ((pA8 && d6) || (pA7 && d2) || (pA2 && d5) || (p16 && dT) || (p12 && d4))) {
            strategyResult = this.decisionMap.get(playerHandID + dealerHandID + runningCountSquashed);
        } else if (!this.h17 && ((pA7 && d2) || (pA4 && d4) || (pA2 && d5) || (p16 && dT) || (p12 && d4))) {
            strategyResult = this.decisionMap.get(playerHandID + dealerHandID + runningCountSquashed);
        } else {
            strategyResult = this.decisionMap.get(playerHandID + dealerHandID + trueCountSquashed);
        }

        if (strategyResult == null) {
            throw new IllegalArgumentException("Unable to find strategy for player hand: "
                    + playerHandID + " and dealer hand: " + dealerHandID + " and true count: "
                    + trueCount + " and running count: " + runningCount);
        }

        if (strategyResult.equals("PU") && dealerHandID.equals("T")) {
            if (playerHand.canSurrender()) {
                strategyResult = this.decisionMap.get(playerHandID + "U" + dealerHandID + trueCountSquashed);
            } else {
                strategyResult = "P";
            }
        }

        if (strategyResult.equals("PU") && dealerHandID.equals("A")) {
            if (playerHand.canSurrender()) {
                strategyResult = this.decisionMap.get(playerHandID + "U" + dealerHandID + trueCountSquashed);
            } else {
                strategyResult = "P";
            }
        }

        if (strategyResult.equals("P")) {
            if (playerHand.canSplit() && playerHand.getNumOfSplits() < 3) {
                strategyResult = "P";
            } else {
                if ((playerHandID.equals("66") && dealerHandID.equals("4")) || (playerHandID.equals("88") && dealerHandID.equals("T"))) {
                    strategyResult = this.decisionMap.get(playerHand.getValue() + dealerHandID + runningCountSquashed);
                } else {
                    strategyResult = this.decisionMap.get(playerHand.getValue() + dealerHandID + trueCountSquashed);
                }
            }
        }

        if (strategyResult.equals("A")) {
            strategyResult = this.decisionMap.get(playerHand.getValue() + dealerHandID + trueCountSquashed);
        }


        //after pairs are dealt with
        String finalDecision = "";

        if (strategyResult.length() == 1) {
            finalDecision = strategyResult;
        } else if (strategyResult.equals("DS")) {
            
            if (playerHand.canDouble()) {
                finalDecision = "D";
            } else {
                finalDecision = "S";
            }
        } else if (strategyResult.equals("DH")) {
            
            if (playerHand.canDouble()) {
                finalDecision = "D";
            } else {
                finalDecision = "H";
            }
        } else if (strategyResult.equals("SU")) {

            if (playerHand.canSurrender()) {
                if (playerHandID.equals("15") && dealerHandID.equals("T")) {
                    finalDecision = this.decisionMap.get(playerHandID + "U" + dealerHandID + runningCountSquashed);
                } else {
                    finalDecision = this.decisionMap.get(playerHand.getValue() + "U" + dealerHandID + trueCountSquashed);
                }
            } else {
                finalDecision = "S";
            }
        } else if (strategyResult.equals("HU")) {
            if (playerHand.canSurrender()) {
                if (playerHandID.equals("15") && dealerHandID.equals("T")) {
                    finalDecision = this.decisionMap.get(playerHandID + "U" + dealerHandID + runningCountSquashed);
                } else {
                    finalDecision = this.decisionMap.get(playerHand.getValue() + "U" + dealerHandID + trueCountSquashed);
                }
            } else {
                finalDecision = "H";
            }
        } else {
            throw new IllegalArgumentException("Unable to find strategy for player hand: "
                    + playerHandID + " and dealer hand: " + dealerHandID + " and true count: "
                    + trueCount + " and running count: " + runningCount);
        }

        if (playerHand.hasSplitAces()) {
            if (!playerHandID.equals("AA")) {
                finalDecision = "S";
            } else if (this.rsa) {
                if (playerHand.canSplit() && playerHand.getNumOfSplits() < 3) {
                    finalDecision = "P";
                } else {
                    finalDecision = "S";
                }
            } else {
                finalDecision = "S";
            }
        }

        if (!finalDecision.equals("H") && !finalDecision.equals("S") &&
                !finalDecision.equals("D") && !finalDecision.equals("P") &&
                !finalDecision.equals("U")) {
            throw new IllegalArgumentException("Unable to find strategy for player hand: "
                    + playerHandID + " and dealer hand: " + dealerHandID + " and true count: "
                    + trueCount + " and running count: " + runningCount);
        }


        return finalDecision;
    }

    @Override
    public int getPlayerPosition() {
        return this.position;
    }

    @Override
    public double getBankroll() {
        return this.bankroll;
    }

    @Override
    public void setBankroll(double bankroll) {
        this.bankroll = bankroll;
    }

    @Override
    public void removeHands() {
        this.handsList = new ArrayList<>();
    }

    @Override
    public boolean isCounter() {
        return true;
    }

}
