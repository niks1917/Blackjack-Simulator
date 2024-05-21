import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Simulation implements ISimulation {

    double numRoundsToPlayMillions;
    double numRoundsSoFar;
    String[][] rules2DArr;

    private boolean surrender;
    private boolean rsa;
    private boolean s17;
    private boolean normGambler;

    // __________________________________
    // ALL THE GAME PARAMETERS FOR THIS SIM
    private HashMap<Integer, HashMap<String, String>> decisionMaps;
    int numDecks;
    double penetration;
    int numOfGamblers;
    boolean gamblerFlatBets;
    boolean trackGambler;
    double gamblerBet;
    double[][] betSpread;
    int numOfCounters;
    int trackedPlayerPosition;
    double bankroll;
    String[] rulesArr;

    String countSystemFile;
    double deckEstimation;


    // __________________________________

    private HashMap<String,Double> resultsMap;

    private HashMap<String,Double> simResultsMap;
    private SimulationKey uniqueSimKey;
    private SimStorageManager allSimsStorage;

    // variables for the sim results

    private double varianceNumerator;
    private int totalGain;
    private int winCount;

    private double lossCount;


    //the boolean values are for the player to be able to make
    //decisions

    public Simulation(String[] charts, double numRoundsToPlayMillions, SimStorageManager allSimsStore, String ruleFile,int numDecks, double penetration, int numOfGamblers,
                      boolean gamblerFlatBets, boolean trackGambler, double gamblerBet,
                      double[][] betSpread, int numOfCounters, int trackedPlayerPosition,
                      double bankroll,int rulesRow,
                      String countSystemFile, double deckEstimation) {

        simResultsMap = new HashMap<>();
        this.allSimsStorage = allSimsStore;
        this.numRoundsToPlayMillions = numRoundsToPlayMillions * 1000000.0;
        this.numRoundsSoFar = 0;


        String one = charts[0];
        String two = charts[1];
        String three = charts[2];
        String four = charts[3];
        String five = charts[4];
        String six = charts[5];

        mapDecisionMaps(one, two, three, four, five, six);
        createRuleFile2DArr(ruleFile);

        // setting all the parameters of this sim to these args
        this.numDecks = numDecks;
        this.penetration = penetration;
        this.numOfGamblers = numOfGamblers;
        this.gamblerFlatBets = gamblerFlatBets;
        this.trackGambler = trackGambler;
        this.gamblerBet = gamblerBet;
        this.betSpread = betSpread;
        this.numOfCounters = numOfCounters;
        this.trackedPlayerPosition = trackedPlayerPosition;
        this.bankroll = bankroll;
        this.rulesArr = rules2DArr[rulesRow]; //TODO check later again
        this.countSystemFile = countSystemFile;
        this.deckEstimation = deckEstimation;

        this.surrender = (this.rulesArr[2].equals("Surr")); // if surr will be true
        this.rsa = (this.rulesArr[3].equals("RSA")); // if RSA will be true
        this.s17 = (this.rulesArr[1].equals("S17"));
        this.normGambler = (this.rulesArr[4].equals("NormGambler"));

        // creating sim key
        createSimKey();

    }

    @Override
    public void createRuleFile2DArr(String ruleFile) {

        String[][] rules2DArray = new String[16][8];
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(ruleFile));
            String line;
            int lineCounter = 0;
            while ((line = in.readLine()) != null) {
                String[] lineArr = line.split(" ");
                for (int i = 0; i < lineArr.length; i++) {
                    rules2DArray[lineCounter][i] = lineArr[i];
                }
                lineCounter++;
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.rules2DArr = rules2DArray;
    }

    @Override
    public void mapDecisionMaps(String CounterH17BSChart,
                                String CounterS17BSChart,
                                String GamblerNormBSChart,
                                String GamblerCrazyBSChart,
                                String H17Dealer,
                                String S17Dealer) {

        HashMap<Integer, HashMap<String, String>> decisionMaps = new HashMap<>();

        HashMap<String, String> counterH17BSChart = createCounterDecisionMap(CounterH17BSChart);
        decisionMaps.put(1, counterH17BSChart);

        HashMap<String, String> counterS17BSChart = createCounterDecisionMap(CounterS17BSChart);
        decisionMaps.put(2, counterS17BSChart);

        HashMap<String, String> gamblerNormBSChart = createGamblerDecisionMap(GamblerNormBSChart);
        decisionMaps.put(3, gamblerNormBSChart);

        HashMap<String, String> gamblerCrazyBSChart = createGamblerDecisionMap(GamblerCrazyBSChart);
        decisionMaps.put(4, gamblerCrazyBSChart);

        HashMap<String, String> h17Dealer = createDealerDecisionMap(H17Dealer);
        decisionMaps.put(5, h17Dealer);

        HashMap<String, String> s17Dealer = createDealerDecisionMap(S17Dealer);
        decisionMaps.put(6, s17Dealer);

        this.decisionMaps = decisionMaps;
    }

    @Override
    public HashMap<String, String> createCounterDecisionMap(String strategyFile) {

        HashMap<String, String> decisionMap = new HashMap<>();

        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(strategyFile));
            String line;
            while ((line = in.readLine()) != null) {
                String[] lineArr = line.split(",");
                for (int i = 0; i < 11; i++) {
                    String key = "";
                    int count = i - 3;
                    for (int j = 1; j < lineArr.length; j++) {
                        if (j < 9) {
                            key = lineArr[0] + (j + 1) + count;
                        } else if (j == 9) {
                            key = lineArr[0] + "T" + count;
                        } else {
                            key = lineArr[0] + "A" + count;
                        }

                        String decision = lineArr[j];
                        String keyValue = "";

                        if (decision.length() < 3) {
                            keyValue = decision;

                        } else {
                            //the full deviation
                            String deviation = decision.replaceAll("[^\\d-+]", "");

                            String delimiter = deviation;
                            if (delimiter.contains("+")) {
                                delimiter = delimiter.replace("+", "\\+");
                            }

                            String[] decisions = decision.split(delimiter);
                            //the basic strategy decision
                            String basicDecision = decisions[0];
                            //the deviation decision
                            String devDecision = decisions[1];

                            //the count where the deviation occurs
                            String countThresholdString = "";
                            for (int k = 0; k < deviation.length() - 1; k++) {
                                countThresholdString += deviation.charAt(k);
                            }
                            int countThreshold = Integer.parseInt(countThresholdString);

                            char qualifier = deviation.charAt(deviation.length() - 1);

                            if (countThreshold != 0) {
                                if (qualifier == '+') {
                                    if (count >= countThreshold) {
                                        keyValue = devDecision;
                                    } else {
                                        keyValue = basicDecision;
                                    }
                                }

                                if (qualifier == '-') {
                                    if (count <= countThreshold) {
                                        keyValue = devDecision;
                                    } else {
                                        keyValue = basicDecision;
                                    }
                                }

                            } else {
                                if (qualifier == '+') {
                                    if (count > countThreshold) {
                                        keyValue = devDecision;
                                    } else {
                                        keyValue = basicDecision;
                                    }
                                }

                                if (qualifier == '-') {
                                    if (count < countThreshold) {
                                        keyValue = devDecision;
                                    } else {
                                        keyValue = basicDecision;
                                    }
                                }
                            }

                        }
                        decisionMap.put(key, keyValue);
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return decisionMap;
    }

    @Override
    public HashMap<String, String> createGamblerDecisionMap(String strategyFile) {

        HashMap<String, String> decisionMap = new HashMap<>();

        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(strategyFile));
            String line;
            while ((line = in.readLine()) != null) {
                String[] lineArr = line.split(",");
                String key = "";
                for (int i = 1; i < lineArr.length; i++) {
                    if (i < 9) {
                        key = lineArr[0] + (i + 1);
                    } else if (i == 9) {
                        key = lineArr[0] + "T";
                    } else {
                        key = lineArr[0] + "A";
                    }

                    String keyValue = lineArr[i];

                    decisionMap.put(key, keyValue);
                }
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return decisionMap;
    }

    @Override
    public HashMap<String, String> createDealerDecisionMap(String strategyFile) {

        HashMap<String, String> decisionMap = new HashMap<>();

        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(strategyFile));
            String line;
            while ((line = in.readLine()) != null) {
                String[] lineArr = line.split(",");
                String key = lineArr[0];
                String keyValue = lineArr[1];
                decisionMap.put(key, keyValue);
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return decisionMap;
    }

    @Override
    public void runSim() {

        //TODO
        Game finalGame = new Game(getNumDecks(),  getPenetration(),  getNumOfGamblers(),
         isGamblerFlatBets(),  isTrackGambler(),  getGamblerBet(),
         getBetSpread(),  getNumOfCounters(),  getTrackedPlayerPosition(),
         getBankroll(), getRulesArr(), getDecisionMaps(), getCountSystemFile(),  getDeckEstimation());
//        Game finalGame = new Game();


        while (getNumRoundsSoFar() < getNumRoundsToPlayMillions()){

            finalGame.playShoe();
            // rounds so far -- finalGame.getRoundsPlayed();
            setNumRoundsSoFar(finalGame.getRoundsPlayed());
        }

        // after game has run all rounds needed
        double won = finalGame.getWonRounds();
        double lost = finalGame.getLostRounds();
        double varianceNum = finalGame.getRunningVariance();
        double totalGain = finalGame.getTotalGain();

        //DUMMY FOR TESTING
//        double won = 100;
//        double lost = 37;
//        double varianceNum = 3675;
//        double totalGain = 8567;

        this.simResultsMap.put("GAIN",totalGain);
        this.simResultsMap.put("VARIANCE_NUM",varianceNum);
        this.simResultsMap.put("WON",won);
        this.simResultsMap.put("LOST",lost);

//
        // TODO - populate simResultsMap after game is run
        // after game is run
        storeSimResult();

    }

    //TODO check
    @Override
    public void createSimKey() {

        int numberOfRounds = (int) getNumRoundsToPlayMillions();
        int numDecks = getNumDecks();
        double penetration = getPenetration();
        int numOfGamblers = getNumOfGamblers();
        boolean gamblerFlatBets = isGamblerFlatBets();
        boolean trackGambler = isTrackGambler();
        double gamblerBet = getGamblerBet();
        double[][] betSpread = getBetSpread();
        int numOfCounters = getNumOfCounters();
        int trackedPlayerPosition = getTrackedPlayerPosition();
        double bankroll = getBankroll();
        String[] rulesArr = getRulesArr();
        String countSystemFile = getCountSystemFile();
        double deckEstimation = getDeckEstimation();

        this.uniqueSimKey = new SimulationKey(numberOfRounds, numDecks,  penetration,  numOfGamblers,
         gamblerFlatBets,  trackGambler,  gamblerBet,
         betSpread,  numOfCounters,  trackedPlayerPosition,
         bankroll,rulesArr,
                 countSystemFile, deckEstimation);

    }

    @Override
    public SimulationKey getSimKey(){
        return this.uniqueSimKey;
    }


    @Override
    public void storeSimResult() {

        //TODO - add error checks here to avoid null sims getting added

        this.allSimsStorage.addSimToStorage(getSimKey(), getSimResults());

    }

    @Override
    public HashMap<String, Double> getSimResults() {
        return this.simResultsMap;
    }

    @Override
    public double getNumRoundsToPlayMillions() {
        return this.numRoundsToPlayMillions;
    }

    @Override
    public double getNumRoundsSoFar() {
        return this.numRoundsSoFar;
    }

    @Override
    public void setNumRoundsSoFar(double numRoundsSoFar) {
        this.numRoundsSoFar = numRoundsSoFar;
    }

    @Override
    public double getTotalGain() {
        return this.totalGain;
    }

    @Override
    public double getVarianceNumerator() {
        return this.varianceNumerator;
    }

    @Override
    public int getWinCount() {
        return this.winCount;
    }

    @Override
    public double getLossCount() {
        return this.lossCount;
    }

    public String[][] getRules2DArr() {
        return rules2DArr;
    }

    public HashMap<Integer, HashMap<String, String>> getDecisionMaps() {
        return decisionMaps;
    }

    public int getNumDecks() {
        return numDecks;
    }

    public double getPenetration() {
        return penetration;
    }

    public int getNumOfGamblers() {
        return numOfGamblers;
    }

    public boolean isGamblerFlatBets() {
        return gamblerFlatBets;
    }

    public boolean isTrackGambler() {
        return trackGambler;
    }

    public double getGamblerBet() {
        return gamblerBet;
    }

    public double[][] getBetSpread() {
        return betSpread;
    }

    public int getNumOfCounters() {
        return numOfCounters;
    }

    public int getTrackedPlayerPosition() {
        return trackedPlayerPosition;
    }

    public double getBankroll() {
        return bankroll;
    }

    public String[] getRulesArr() {
        return rulesArr;
    }

    public String getCountSystemFile() {
        return countSystemFile;
    }

    public double getDeckEstimation() {
        return deckEstimation;
    }
}

