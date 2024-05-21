import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class AnalyzeSImsTest {

    private AnalyzeSims testAnalyzer;
    private SimStorageManager testStore;
    String filepathForStore;
    Simulation testSim;
    SimulationKey testSimKey;


    @Before
    public void setUp(){
         testAnalyzer = new AnalyzeSims();
         testStore = testAnalyzer.getSimStore();
         filepathForStore = "testSimFile.txt";

        int numberOfRounds = 1; // adjusted temp
        int numDecks = 6;
        double penetration = 5;
        int numOfGamblers = 4;
        boolean gamblerFlatBets = false;
        boolean trackGambler = true;
        double gamblerBet = -1;
        double[][] betSpread = {{0,0,0,0,-1,-1,-1,-1,-1,-1,-1},{0,0,0,0,25,50,100,200,250,300,300}};
        int numOfCounters = 1;
        int trackedPlayerPosition = 2;
        double bankroll = 30000;
        int rulesRow = 2;
        HashMap<Integer, HashMap<String, String>> decisionMaps = new HashMap<>();
        String countSystemFile = "countSystemHiLo.txt";
        double deckEstimation = 0.5; // quarter decks

        // for constructor
        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};
        String ruleFile = "rulesMap.txt";

        testSim = new Simulation(chartsToInput,numberOfRounds,testStore,ruleFile,numDecks,  penetration,  numOfGamblers,
                gamblerFlatBets,  trackGambler,  gamblerBet,
                betSpread,  numOfCounters,  trackedPlayerPosition,
                bankroll, rulesRow, countSystemFile,  deckEstimation);

        testSimKey = new SimulationKey(
                numberOfRounds,  numDecks,  penetration,  numOfGamblers,
                gamblerFlatBets,  trackGambler,  gamblerBet,
                betSpread, numOfCounters , trackedPlayerPosition ,
                bankroll, testSim.getRulesArr(),
                countSystemFile,  deckEstimation
        );

    }


    @Test
    public void testExpectedValueHourly(){

        HashMap<String, Double> testSimResult = new HashMap<>();

        double roundsPlayed = testSim.getNumRoundsToPlayMillions();

        testSimResult.put("GAIN",10000.0);
        testSimResult.put("WON",200.0);
        testSimResult.put("LOST",39.0);
        testSimResult.put("VARIANCE_",1453.0);

        int roundsPerHr = AnalyzeSims.ROUNDS_PER_HR;
        double hrs = roundsPlayed/(double) roundsPerHr;

        double expectedEV = (10000/hrs);

        double actualEV = testAnalyzer.calculateExpectationPerHour(testSimResult, roundsPlayed);

        assertEquals(expectedEV,actualEV,0.01);

    }

    @Test
    public void testWinRate(){

        HashMap<String, Double> testSimResult = new HashMap<>();

        testSimResult.put("GAIN",10000.0);
        testSimResult.put("WON",200.0);
        testSimResult.put("LOST",39.0);
        testSimResult.put("VARIANCE_NUM",1453.0);

        double expected = ((double) 200 /(200+39));
        double actual = testAnalyzer.calculateWinRate(testSimResult);
        assertEquals(expected,actual,0.01);
    }

    @Test
    public void testSDHourly(){

        HashMap<String, Double> testSimResult = new HashMap<>();

        double roundsPlayed = testSim.getNumRoundsToPlayMillions();

        testSimResult.put("GAIN",10000.0);
        testSimResult.put("WON",200.0);
        testSimResult.put("LOST",39.0);
        testSimResult.put("VARIANCE_NUM",1453.0);

        int roundsPerHr = AnalyzeSims.ROUNDS_PER_HR;
        double hrs = roundsPlayed/(double) roundsPerHr;

        double varianceInRounds = 1453/roundsPlayed;
        double expectedVarianceHrs = varianceInRounds*roundsPerHr;

        double expectedSD = Math.sqrt(expectedVarianceHrs);

        double actualSD = testAnalyzer.calculateSDHourly(testSimResult, roundsPlayed);

        assertEquals(expectedSD,actualSD,0.01);

    }

    @Test
    public void testRiskOfRuin(){

        HashMap<String, Double> testSimResult = new HashMap<>();

        double roundsPlayed = testSim.getNumRoundsToPlayMillions();

        testSimResult.put("GAIN",10000.0);
        testSimResult.put("WON",200.0);
        testSimResult.put("LOST",39.0);
        testSimResult.put("VARIANCE_NUM",1453.0);

        int roundsPerHr = AnalyzeSims.ROUNDS_PER_HR;
        double hrs = roundsPlayed/(double) roundsPerHr;

        double varianceInRounds = 1453/roundsPlayed;
        double expectedVarianceHrs = varianceInRounds*roundsPerHr;
        double expectedSD = Math.sqrt(expectedVarianceHrs);
        double expectedEV = (10000/hrs);

        double actualEV = testAnalyzer.calculateExpectationPerHour(testSimResult, roundsPlayed);
        double actualSD = testAnalyzer.calculateSDHourly(testSimResult, roundsPlayed);
        double bankroll = testSim.getBankroll();

        double expectedROR = Math.exp(-2*expectedEV*bankroll/Math.pow(expectedSD,2));
        double actualROR = testAnalyzer.calculateRiskOfRuin(actualSD,actualEV, bankroll);

        assertEquals(expectedROR,actualROR,0.01);

    }

    @Test
    public void testN0(){

        HashMap<String, Double> testSimResult = new HashMap<>();

        double roundsPlayed = testSim.getNumRoundsToPlayMillions();

        testSimResult.put("GAIN",10000.0);
        testSimResult.put("WON",200.0);
        testSimResult.put("LOST",39.0);
        testSimResult.put("VARIANCE_NUM",1453.0);

        int roundsPerHr = AnalyzeSims.ROUNDS_PER_HR;
        double hrs = roundsPlayed/(double) roundsPerHr;

        double varianceInRounds = 1453/roundsPlayed;
        double expectedVarianceHrs = varianceInRounds*roundsPerHr;
        double expectedSD = Math.sqrt(expectedVarianceHrs);
        double expectedEV = (10000/hrs);

        double actualEV = testAnalyzer.calculateExpectationPerHour(testSimResult, roundsPlayed);
        double actualSD = testAnalyzer.calculateSDHourly(testSimResult, roundsPlayed);

        double expectedN0 = Math.pow(expectedSD / expectedEV, 2);
        double actualN0 = testAnalyzer.calculateTimeToConquer(actualSD,actualEV);

        assertEquals(expectedN0,actualN0,0.01);

    }

}
