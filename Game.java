import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Game implements IGame {

    //initialized in constructor and reshuffled every shoe
    private Deck deck;
    private int numDecks;
    private double penetration;
    private Card[] discards;
    private int discardsCounter = 0;
    private List<IPlayer> playersAtTable;
    private Dealer dealer;
    private HashMap<Integer, HashMap<String, String>> decisionMaps;

    //map of card id to integer
    private HashMap<String, Integer> countSystem;
    private int runningCount;
    private int trueCount;

    //either .25, .5 or 1
    private double deckEstimation;
    private boolean roundIsOver = false;
    private boolean surrender;
    private boolean rsa;
    private boolean h17;
    private int roundsPlayed = 0;
    private double totalGain = 0;
    private double runningVariance = 0;
    private int wonRounds = 0;
    private int lostRounds = 0;
    private boolean gamblerIsCrazy;
    private int trackedPlayerPosition;
    private int numOfPlayers;
    private int numOfGamblers;
    private int numOfCounters;
    private boolean trackingGambler;
    private boolean gamblerFlatBets;

    //-1 if they are using a bet spread
    private double gamblerBet;
    private double[][] betSpread;
    private double bankroll;


    public Game() {}
    //trackedPlayerPosition value starts at 0 and goes as high as
    //numOfGamblers + numOfPlayers - 1
    //gambler bet is -1 if gamblerFlatBets (meaning they bet the same
    //amount no matter the count) is false (remember when doing permutations
    //in sim that gambler doesn't need to iterate through bet values
    //if they are flat betting)
    public Game(int numDecks, double penetration, int numOfGamblers,
                boolean gamblerFlatBets, boolean trackGambler, double gamblerBet,
                double[][] betSpread, int numOfCounters, int trackedPlayerPosition,
                double bankroll, String[] rulesArr,
                HashMap<Integer, HashMap<String, String>> decisionMaps,
                String countSystemFile, double deckEstimation) {

        if (penetration > numDecks) {
            throw new IllegalArgumentException("penetration is larger than num of decks");
        }

        if (numDecks <= 0) {
            throw new IllegalArgumentException("Number of decks can not be less than 1");
        }

        this.numDecks = numDecks;
        this.penetration = penetration;
        this.deck = new Deck(numDecks);
        this.discards = new Card[numDecks * 52];
        this.deckEstimation = deckEstimation;

        if (deckEstimation != .25 && deckEstimation != .5 && deckEstimation != 1) {
            throw new IllegalArgumentException("Deck estimation is not .25, .5, or 1");
        }

        if (!gamblerFlatBets && gamblerBet != -1) {
            throw new IllegalArgumentException("Gambler is spreading bets, but also has a single-bet value");
        }

        double[][] gamblerBetSpread = new double[11][11];
        if (gamblerFlatBets) {
            for (int i = 0; i < 11; i++) {
                for (int j = 0; j < 11; j++) {
                    gamblerBetSpread[i][j] = gamblerBet;
                }
            }
        } else {
            gamblerBetSpread = betSpread;
        }

        this.gamblerFlatBets = gamblerFlatBets;

        this.gamblerBet = gamblerBet;

        this.betSpread = betSpread;

        this.bankroll = bankroll;

        this.countSystem = createCountSystemMap(countSystemFile);

        int counterDecisionMapKey = Integer.parseInt(rulesArr[5]);
        int gamblerDecisionMapKey = Integer.parseInt(rulesArr[6]);
        int dealerDecisionMapKey = Integer.parseInt(rulesArr[7]);

        this.h17 = rulesArr[1].equals("H17");
        this.surrender = rulesArr[2].equals("Surr");
        this.rsa = rulesArr[3].equals("RSA");

        this.gamblerIsCrazy = rulesArr[4].equals("CrazyGambler");

        this.trackedPlayerPosition = trackedPlayerPosition;

        if (trackGambler && numOfGamblers == 0) {
            throw new IllegalArgumentException("set gambler to be tracked, but there are no gamblers");
        }

        if (!trackGambler && numOfCounters == 0) {
            throw new IllegalArgumentException("set counter to be tracked, but there are no counters");
        }

        this.numOfCounters = numOfCounters;
        this.numOfGamblers = numOfGamblers;

        int numOfPlayers = numOfGamblers + numOfCounters;
        this.numOfPlayers = numOfPlayers;
        if ((trackedPlayerPosition > numOfPlayers - 1) || trackedPlayerPosition < 0) {
            throw new IllegalArgumentException("invalid tracked player position");
        }

        if ((numOfPlayers == 1 && numOfCounters == 1) || (numOfPlayers == 1 && numOfGamblers == 1 && !gamblerFlatBets)) {
            for (int i = 0; i < this.betSpread[0].length; i++) {
                if (this.betSpread[0][i] == 0) {
                    throw new IllegalArgumentException("When playing alone, you can't sit out hands");
                }
            }
        }

        for (int i = 0; i < this.betSpread[0].length; i++) {
            if (this.betSpread[0][i] == -1 && this.betSpread[1][i] <= 0) {
                throw new IllegalArgumentException("Can't bet $0 on two hands");
            }
        }

        List<Integer> initQ = new ArrayList<>();
        for (int i = 0; i < numOfPlayers; i++) {
            if (i != trackedPlayerPosition) {
                initQ.add(i);
            }
        }
        if (!initQ.isEmpty()) {
            Collections.shuffle(initQ);
        }

        Queue<Integer> q = new LinkedList<>(initQ);

        int currPos = 0;
        if (!q.isEmpty()) {
            currPos = q.poll();
        }

        this.trackingGambler = trackGambler;

        this.playersAtTable = new ArrayList<>();

        for (int i = 0; i < numOfPlayers; i++) {
            IPlayer dummy = new CounterPlayer();
            this.playersAtTable.add(dummy);
        }

        if (trackGambler) {
            for (int i = 0; i < numOfGamblers; i++) {
                if (i == 0) {
                    GamblerPlayer trackedPlayer = new GamblerPlayer(trackedPlayerPosition,
                            bankroll, gamblerBetSpread, true,
                            decisionMaps.get(gamblerDecisionMapKey), this.surrender, this.rsa);
                    this.playersAtTable.set(trackedPlayerPosition, trackedPlayer);
                } else {
                    GamblerPlayer gambler = new GamblerPlayer(currPos,
                            bankroll, gamblerBetSpread, false,
                            decisionMaps.get(gamblerDecisionMapKey), this.surrender, this.rsa);
                    this.playersAtTable.set(currPos, gambler);
                    if (!q.isEmpty()) {
                        currPos = q.poll();
                    }

                }
            }
        } else {
            for (int i = 0; i < numOfGamblers; i++) {
                GamblerPlayer gambler = new GamblerPlayer(currPos,
                        bankroll, gamblerBetSpread, false,
                        decisionMaps.get(gamblerDecisionMapKey), this.surrender, this.rsa);
                this.playersAtTable.set(currPos, gambler);
                if (!q.isEmpty()) {
                    currPos = q.poll();
                }
            }
        }

        if (!trackGambler) {
            for (int i = 0; i < numOfCounters; i++) {
                if (i == 0) {
                    CounterPlayer trackedPlayer = new CounterPlayer(trackedPlayerPosition,
                            bankroll, betSpread, true,
                            decisionMaps.get(counterDecisionMapKey), this.h17, this.surrender, this.rsa);
                    this.playersAtTable.set(trackedPlayerPosition, trackedPlayer);
                } else {
                    CounterPlayer counter = new CounterPlayer(currPos,
                            bankroll, betSpread, false,
                            decisionMaps.get(counterDecisionMapKey), this.h17, this.surrender, this.rsa);
                    this.playersAtTable.set(currPos, counter);
                    if (!q.isEmpty()) {
                        currPos = q.poll();
                    }
                }
            }
        } else {
            for (int i = 0; i < numOfCounters; i++) {
                CounterPlayer counter = new CounterPlayer(currPos,
                        bankroll, betSpread, false,
                        decisionMaps.get(counterDecisionMapKey), this.h17, this.surrender, this.rsa);
                this.playersAtTable.set(currPos, counter);
                if (!q.isEmpty()) {
                    currPos = q.poll();
                }
            }
        }

        List<IPlayer> sortedPlayers = new ArrayList<>();
        for (int i = 0; i < numOfPlayers; i++) {
            IPlayer dummy = new CounterPlayer();
            sortedPlayers.add(dummy);
        }

        for (int i = 0; i < this.playersAtTable.size(); i++) {
            int properPlayerPosition = this.playersAtTable.get(i).getPlayerPosition();
            sortedPlayers.set(properPlayerPosition, this.playersAtTable.get(i));
        }
        this.playersAtTable = sortedPlayers;

        this.dealer = new Dealer(numOfPlayers, decisionMaps.get(dealerDecisionMapKey));
    }


    @Override
    public void playShoe() {

        //burn card
        dealCard();
        //if 1 or 2 deck game, player does not know the card
        if (this.deck.getNumOfDecks() < 3) {
            this.runningCount = 0;
            this.trueCount = 0;
        }

        while (!deckIsFinished()) {

            double startingTrackedBankroll = 0;
            for (IPlayer iPlayer : this.playersAtTable) {
                if (iPlayer.isTracked()) {
                    startingTrackedBankroll = iPlayer.getBankroll();
                    break;
                }
            }

            roundInit();
            checkDealerT();
            if (!this.roundIsOver) {
                runInsurance();
            }
            if (!this.roundIsOver) {
                checkForBlackJacks();
            }

            if (!this.roundIsOver) {
                int playerIndex = 0;
                for (IPlayer iPlayer : this.playersAtTable) {
                    if (!iPlayer.getHandsList().isEmpty()) {
                        int handIndex = 0;
                        while (handIndex < iPlayer.getHandsList().size()) {
                            while (!iPlayer.getHand(handIndex).isOver() &&
                                    !iPlayer.getHand(handIndex).isWaiting()) {

                                if (iPlayer.getHand(handIndex).getCardsList().size() == 1) {
                                    Card dealtCard = dealCard();
                                    iPlayer.getHand(handIndex).addCard(dealtCard);
                                }

                                String decision = iPlayer.makeHandDecision(iPlayer.getHand(handIndex),
                                        this.dealer.getHand(), this.trueCount, this.runningCount);

                                if (decision.equals("H")) {
                                    Card dealtCard = dealCard();
                                    iPlayer.hit(dealtCard, handIndex);
                                    if (isBustedPlayer(iPlayer.getHand(handIndex))) {
                                        iPlayer.getHand(handIndex).setIsOver(true);
                                    }
                                } else if (decision.equals("D")) {
                                    Card dealtCard = dealCard();
                                    iPlayer.doubleDown(dealtCard, handIndex);
                                } else if (decision.equals("U")) {
                                    iPlayer.surrender(handIndex);
                                } else if (decision.equals("P")) {
                                    split(playerIndex, iPlayer.getHandsList(), handIndex);
                                } else if (decision.equals("S")) {
                                    iPlayer.stand(handIndex);
                                }

                            }
                            handIndex++;
                        }
                    }
                    playerIndex++;
                }

                holeCardUpdate();

                while (!this.dealer.getHand().isOver() && !this.dealer.getHand().isWaiting()) {

                    String decision = this.dealer.makeHandDecision(this.dealer.getHand());

                    if (decision.equals("H")) {
                        Card dealtCard = dealCard();
                        this.dealer.hit(dealtCard);
                        if (isBustedDealer(this.dealer.getHand())) {
                            this.dealer.getHand().setIsOver(true);
                        }
                    } else if (decision.equals("S")) {
                        this.dealer.stand();
                    }
                }

                compareHands();
            }

            double endingTrackedBankroll = 0;
            for (IPlayer iPlayer : this.playersAtTable) {
                if (iPlayer.isTracked()) {
                    endingTrackedBankroll = iPlayer.getBankroll();
                    break;
                }
            }

            double roundGain = endingTrackedBankroll - startingTrackedBankroll;
            this.totalGain = this.totalGain + roundGain;

            double roundVariance = roundGain * roundGain;
            this.runningVariance = this.runningVariance + roundVariance;

            if (roundGain > 0) {
                this.wonRounds = this.wonRounds + 1;
            } else if (roundGain < 0) {
                this.lostRounds = this.lostRounds + 1;
            }

            this.roundsPlayed = this.roundsPlayed + 1;

            this.roundIsOver = false;
        }

        clearShoe();
    }

    @Override
    public HashMap<String, Integer> createCountSystemMap(String countSystemFile) {

        HashMap<String, Integer> countSystem = new HashMap<>();

        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(countSystemFile));
            String line;
            while ((line = in.readLine()) != null) {
                String[] lineArr = line.split(" ");
                countSystem.put(lineArr[0], Integer.parseInt(lineArr[1]));
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return countSystem;
    }

    @Override
    public void split(int indexOfPlayer, List<PlayerHand> handsList, int indexOfHand) {
        Card splitCard = handsList.get(indexOfHand).getCardsList().get(0);
        double bet = handsList.get(indexOfHand).getBet();
        int numSplits = handsList.get(indexOfHand).getNumOfSplits();

        PlayerHand splitHandReplace = new PlayerHand(bet, this.surrender);
        splitHandReplace.addCard(splitCard);
        splitHandReplace.setNumOfSplits(numSplits + 1);
        splitHandReplace.setCanSurrender(false);

        PlayerHand splitHandAdd = new PlayerHand(bet, this.surrender);
        splitHandAdd.addCard(splitCard);
        splitHandAdd.setNumOfSplits(numSplits + 1);
        splitHandAdd.setCanSurrender(false);

        if (splitCard.getID().equals("A")) {
            splitHandReplace.setHasSplitAces(true);
            splitHandAdd.setHasSplitAces(true);
        }

        handsList.set(indexOfHand, splitHandReplace);

        handsList.add(indexOfHand + 1, splitHandAdd);
        double bankroll = this.playersAtTable.get(indexOfPlayer).getBankroll();
        this.playersAtTable.get(indexOfPlayer).setBankroll(bankroll - bet);

        for (int i = indexOfHand + 2; i < handsList.size(); i++) {
            if (handsList.get(i).getNumOfSplits() > 0) {
                handsList.get(i).setNumOfSplits(handsList.get(i).getNumOfSplits() + 1);
            }
        }

    }

    @Override
    public void updateCount() {
        
        int trueCount = 0;
        int cardsRemaining = (this.deck.getNumOfDecks() * 52) - this.discardsCounter;

        if (this.runningCount != 0 && cardsRemaining > 0) {
            double decksRemaining = ((double)cardsRemaining) / 52.0;

            if (this.deckEstimation == 1.0) {
                decksRemaining = Math.ceil(decksRemaining);
            } else if (this.deckEstimation == 0.5) {
                double floored = Math.floor(decksRemaining);
                double flooredPlusHalf = floored + 0.5;
                double ceiling = Math.ceil(decksRemaining);

                if (decksRemaining > flooredPlusHalf) {
                    decksRemaining = ceiling;
                } else if (decksRemaining > floored) {
                    decksRemaining = flooredPlusHalf;
                }
            } else if (this.deckEstimation == 0.25) {
                double floored = Math.floor(decksRemaining);
                double flooredPlusQuarter = floored + 0.25;
                double flooredPlusHalf = floored + 0.5;
                double flooredPlus3Quarters = floored + 0.75;
                double ceiling = Math.ceil(decksRemaining);

                if (decksRemaining > flooredPlus3Quarters) {
                    decksRemaining = ceiling;
                } else if (decksRemaining > flooredPlusHalf) {
                    decksRemaining = flooredPlus3Quarters;
                } else if (decksRemaining > flooredPlusQuarter) {
                    decksRemaining = flooredPlusHalf;
                } else if (decksRemaining > floored) {
                    decksRemaining = flooredPlusQuarter;
                }
            }

            double trueCountInit = ((double)this.runningCount) / decksRemaining;
            double trueCountInitFloored = Math.floor(trueCountInit);
            double trueCountInitCeil = Math.ceil(trueCountInit);
            
            if (trueCountInit > 0) {
                trueCount = (int) trueCountInitFloored;
            } else if (trueCountInit < 0) {
                trueCount = (int) trueCountInitCeil;
            }
        }
        
        this.trueCount = trueCount;
    }

    @Override
    public double insuranceCount() {

        double trueCount = 0;
        int cardsRemaining = (this.deck.getNumOfDecks() * 52) - this.discardsCounter;

        if (this.runningCount != 0 && cardsRemaining > 0) {
            double decksRemaining = ((double)cardsRemaining) / 52.0;

            if (this.deckEstimation == 1.0) {
                decksRemaining = Math.ceil(decksRemaining);
            } else if (this.deckEstimation == 0.5) {
                double floored = Math.floor(decksRemaining);
                double flooredPlusHalf = floored + 0.5;
                double ceiling = Math.ceil(decksRemaining);

                if (decksRemaining > flooredPlusHalf) {
                    decksRemaining = ceiling;
                } else if (decksRemaining > floored) {
                    decksRemaining = flooredPlusHalf;
                }
            } else if (this.deckEstimation == 0.25) {
                double floored = Math.floor(decksRemaining);
                double flooredPlusQuarter = floored + 0.25;
                double flooredPlusHalf = floored + 0.5;
                double flooredPlus3Quarters = floored + 0.75;
                double ceiling = Math.ceil(decksRemaining);

                if (decksRemaining > flooredPlus3Quarters) {
                    decksRemaining = ceiling;
                } else if (decksRemaining > flooredPlusHalf) {
                    decksRemaining = flooredPlus3Quarters;
                } else if (decksRemaining > flooredPlusQuarter) {
                    decksRemaining = flooredPlusHalf;
                } else if (decksRemaining > floored) {
                    decksRemaining = flooredPlusQuarter;
                }
            }

            trueCount = ((double)this.runningCount) / decksRemaining;
        }

        return trueCount;
    }

    @Override
    public Card dealCard() {

        if (this.discardsCounter < (this.deck.getNumOfDecks() * 52)) {
            Card dealtCard = this.deck.dealCard();
            int value = this.countSystem.get(dealtCard.getID());
            this.runningCount = this.runningCount + value;
            this.discards[discardsCounter] = dealtCard;
            this.discardsCounter++;

            updateCount();

            return dealtCard;

        } else if (this.discardsCounter == (this.deck.getNumOfDecks() * 52)) {
            String[] idArr = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
            Random rand = new Random();
            int index = rand.nextInt(13);
            String id = idArr[index];

            Card dealtCard = new Card(id);

            this.runningCount = 0;
            this.trueCount = 0;

            return dealtCard;

        } else {
            throw new RuntimeException("discard counter exceeded number of cards in deck");
        }
    }

    @Override
    public void roundInit() {

        for (int i = 0; i < this.playersAtTable.size(); i++) {
            this.playersAtTable.get(i).placeBets(this.trueCount);
        }
        this.dealer.initHand();

        for (int i = 0; i < this.playersAtTable.size(); i++) {
            if (!this.playersAtTable.get(i).getHandsList().isEmpty()) {
                for (int j = 0; j < this.playersAtTable.get(i).getHandsList().size(); j++) {
                    Card dealtCard = dealCard();
                    this.playersAtTable.get(i).getHandsList().get(j).addCard(dealtCard);
                }
            }
        }

        Card upCard = dealCard();
        this.dealer.getHand().addCard(upCard);
        this.dealer.getHand().setUpCard(upCard);

        for (int i = 0; i < this.playersAtTable.size(); i++) {
            if (!this.playersAtTable.get(i).getHandsList().isEmpty()) {
                for (int j = 0; j < this.playersAtTable.get(i).getHandsList().size(); j++) {
                    Card dealtCard = dealCard();
                    this.playersAtTable.get(i).getHandsList().get(j).addCard(dealtCard);
                }
            }
        }

        Card holeCard = dealCard();
        this.dealer.getHand().addCard(holeCard);
        this.dealer.getHand().setHoleCard(holeCard);

        this.runningCount = this.runningCount - this.countSystem.get(holeCard.getID());
        updateCount();
    }

    @Override
    public void checkDealerT() {
        if (this.dealer.getHand().getUpCard().getID().equals("T") &&
                this.dealer.getHand().getHoleCard().getID().equals("A")) {

            for (int i = 0; i < this.playersAtTable.size(); i++) {
                if (!this.playersAtTable.get(i).getHandsList().isEmpty()) {
                    for (int j = 0; j < this.playersAtTable.get(i).getHandsList().size(); j++) {
                        if (this.playersAtTable.get(i).getHandsList().get(j).getId().equals("AT")) {
                            double bet = this.playersAtTable.get(i).getHandsList().get(j).getBet();
                            payPlayer(this.playersAtTable.get(i), bet);
                        }
                    }
                }
            }

            setRoundIsOver(true);
        }
    }

    @Override
    public void runInsurance() {
        if (this.dealer.getHand().getUpCard().getID().equals("A")) {
            for (int i = 0; i < this.playersAtTable.size(); i++) {
                if (!this.playersAtTable.get(i).getHandsList().isEmpty()) {
                    if (this.playersAtTable.get(i).isCounter()) {
                        for (int j = 0; j < this.playersAtTable.get(i).getHandsList().size(); j++) {
                            int numDecks = this.deck.getNumOfDecks();
                            double trueCount = insuranceCount();
                            if ((numDecks >= 3 && trueCount >= 3) || (numDecks == 2 && trueCount >= 2.5) ||
                                    (numDecks == 1 && trueCount >= 1.5)) {
                                if (!this.playersAtTable.get(i).getHandsList().get(j).getId().equals("AT")) {
                                    this.playersAtTable.get(i).getHandsList().get(j).setTookInsurance(true);
                                    double bet = this.playersAtTable.get(i).getHandsList().get(j).getBet();
                                    this.playersAtTable.get(i).getHandsList().get(j).setBet(bet * 1.5);
                                    this.playersAtTable.get(i).setBankroll(this.playersAtTable.get(i).getBankroll() - (bet / 2));
                                } else {
                                    payPlayer(this.playersAtTable.get(i), (this.playersAtTable.get(i).getHandsList().get(j).getBet() * 2));
                                    this.playersAtTable.get(i).getHandsList().get(j).setIsOver(true);
                                }

                            }
                        }
                    }

                }
            }

            if (!this.dealer.getHand().getHoleCard().getID().equals("T")) {
                for (int i = 0; i < this.playersAtTable.size(); i++) {
                    if (!this.playersAtTable.get(i).getHandsList().isEmpty()) {
                        for (int j = 0; j < this.playersAtTable.get(i).getHandsList().size(); j++) {
                            if (!this.playersAtTable.get(i).getHandsList().get(j).isOver()) {
                                if (this.playersAtTable.get(i).getHandsList().get(j).tookInsurance()) {
                                    this.playersAtTable.get(i).getHandsList().get(j).setBet(this.playersAtTable.get(i).getHandsList().get(j).getBet() / 1.5);
                                }

                            }
                        }
                    }
                }
            } else {
                setRoundIsOver(true);
                for (int i = 0; i < this.playersAtTable.size(); i++) {
                    if (!this.playersAtTable.get(i).getHandsList().isEmpty()) {
                        for (int j = 0; j < this.playersAtTable.get(i).getHandsList().size(); j++) {
                            if (!this.playersAtTable.get(i).getHandsList().get(j).isOver()) {
                                if (!this.playersAtTable.get(i).getHandsList().get(j).tookInsurance()) {
                                    if (this.playersAtTable.get(i).getHandsList().get(j).getId().equals("AT")) {
                                        payPlayer(this.playersAtTable.get(i), this.playersAtTable.get(i).getHandsList().get(j).getBet());
                                    }
                                } else {
                                    payPlayer(this.playersAtTable.get(i), this.playersAtTable.get(i).getHandsList().get(j).getBet());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void checkForBlackJacks() {

        for (int i = 0; i < this.playersAtTable.size(); i++) {
            if (!this.playersAtTable.get(i).getHandsList().isEmpty()) {
                for (int j = 0; j < this.playersAtTable.get(i).getHandsList().size(); j++) {
                    if (!this.playersAtTable.get(i).getHandsList().get(j).isOver()) {
                        if (this.playersAtTable.get(i).getHandsList().get(j).getId().equals("AT")) {
                            payPlayer(this.playersAtTable.get(i), (this.playersAtTable.get(i).getHandsList().get(j).getBet() * 2.5));
                            this.playersAtTable.get(i).getHandsList().get(j).setIsOver(true);
                        }
                    }
                }
            }
        }

    }

    @Override
    public boolean isBustedPlayer(PlayerHand hand) {

        return Integer.parseInt(hand.getValue()) > 21;

    }

    @Override
    public boolean isBustedDealer(DealerHand hand) {

        return Integer.parseInt(hand.getValue()) > 21;
    }

    @Override
    public void holeCardUpdate() {
        Card holeCard = this.dealer.getHand().getHoleCard();
        this.runningCount = this.runningCount + this.countSystem.get(holeCard.getID());
        updateCount();
    }

    @Override
    public void compareHands() {
        if (this.dealer.getHand().isOver()) {
            for (int i = 0; i < this.playersAtTable.size(); i++) {
                if (!this.playersAtTable.get(i).getHandsList().isEmpty()) {
                    for (int j = 0; j < this.playersAtTable.get(i).getHandsList().size(); j++) {
                        if (!this.playersAtTable.get(i).getHandsList().get(j).isOver()) {
                            payPlayer(this.playersAtTable.get(i), (this.playersAtTable.get(i).getHandsList().get(j).getBet() * 2));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < this.playersAtTable.size(); i++) {
                if (!this.playersAtTable.get(i).getHandsList().isEmpty()) {
                    for (int j = 0; j < this.playersAtTable.get(i).getHandsList().size(); j++) {
                        if (!this.playersAtTable.get(i).getHandsList().get(j).isOver()) {
                            int comparison = compareWithDealer(this.playersAtTable.get(i).getHandsList().get(j));
                            if (comparison == 1) {
                                payPlayer(this.playersAtTable.get(i), (this.playersAtTable.get(i).getHandsList().get(j).getBet() * 2));
                            } else if (comparison == 0) {
                                payPlayer(this.playersAtTable.get(i), this.playersAtTable.get(i).getHandsList().get(j).getBet());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void payPlayer(IPlayer player, double bet) {
        player.setBankroll(player.getBankroll() + bet);
    }

    @Override
    public int compareWithDealer(PlayerHand playerhand) {

        int playerHandVal = Integer.parseInt(playerhand.getValue());
        int dealerHandVal = Integer.parseInt(this.dealer.getHand().getValue());

        if (playerHandVal > dealerHandVal) {
            return 1;
        } else if (dealerHandVal > playerHandVal) {
            return -1;
        } else {
            return 0;
        }

    }

    @Override
    public void clearShoe() {

        while (this.discardsCounter < this.discards.length) {
            dealCard();
        }

        this.deck = new Deck(this.discards);
        this.discardsCounter = 0;
        this.runningCount = 0;
        this.trueCount = 0;
        setRoundIsOver(false);
    }

    @Override
    public Deck getDeck() {
        return this.deck;
    }

    @Override
    public Dealer getDealer() {
        return this.dealer;
    }

    @Override
    public HashMap<String, Integer> getCountSystem() {
        return this.countSystem;
    }

    @Override
    public int getRunningCount() {
        return this.runningCount;
    }

    @Override
    public int getTrueCount() {
        return this.trueCount;
    }

    @Override
    public boolean deckIsFinished() {
        int numCardsDealt = (int)Math.round(this.penetration * 52);
        return this.discardsCounter >= numCardsDealt;
    }

    @Override
    public boolean roundIsOver() {
        return this.roundIsOver;
    }

    @Override
    public void setRoundIsOver(boolean roundIsOver) {
        this.roundIsOver = roundIsOver;
    }

    @Override
    public int getRoundsPlayed() {
        return this.roundsPlayed;
    }

    @Override
    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    @Override
    public double getPenetration() {
        return this.penetration;
    }

    @Override
    public int getNumDecks() {
        return this.numDecks;
    }

    @Override
    public boolean getSurrender() {
        return this.surrender;
    }

    @Override
    public boolean getRSA() {
        return this.rsa;
    }

    @Override
    public boolean getH17() {
        return this.h17;
    }

    @Override
    public double getTotalGain() {
        return this.totalGain;
    }

    @Override
    public void setTotalGain(double totalGain) {
        this.totalGain = totalGain;
    }

    @Override
    public double getRunningVariance() {
        return this.runningVariance;
    }

    @Override
    public void setRunningVariance(double runningVariance) {
        this.runningVariance = runningVariance;
    }

    @Override
    public int getWonRounds() {
        return this.wonRounds;
    }

    @Override
    public void setWonRounds(int wonRounds) {
        this.wonRounds = wonRounds;
    }

    @Override
    public int getLostRounds() {
        return this.lostRounds;
    }

    @Override
    public void setLostRounds(int lostRounds) {
        this.lostRounds = lostRounds;
    }

    @Override
    public double getDeckEstimation() {
        return this.deckEstimation;
    }

    @Override
    public boolean isGamblerCrazy() {
        return this.gamblerIsCrazy;
    }

    @Override
    public int getTrackedPlayerPosition() {
        return this.trackedPlayerPosition;
    }

    @Override
    public int getNumOfPlayers() {
        return this.numOfPlayers;
    }

    @Override
    public int getNumOfGamblers() {
        return this.numOfGamblers;
    }

    @Override
    public int getNumOfCounters() {
        return this.numOfCounters;
    }

    @Override
    public boolean isTrackingGambler() {
        return this.trackingGambler;
    }

    @Override
    public boolean gamblerFlatBets() {
        return this.gamblerFlatBets;
    }

    @Override
    public double getGamblerBet() {
        return this.gamblerBet;
    }

    @Override
    public double[][] getBetSpread() {
        return this.betSpread;
    }

    @Override
    public double getBankroll() {
        return this.bankroll;
    }

    @Override
    public List<IPlayer> getPlayersAtTable() {
        return this.playersAtTable;
    }


}
