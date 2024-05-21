import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

public class SimulationsTest {


    private Simulation sim;
   private SimStorageManager testStorage;

   private SimulationKey expectedKey;

   @Before
   public void setUp(){


       AnalyzeSims testAnalyser = new AnalyzeSims(); // initializes the testStorage
       testStorage = testAnalyser.getSimStore();

       int numberOfRounds = 0; // adjusted temp
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
       String countSystemFile = "countSystemHiLo.txt";
       double deckEstimation = 0.5; // quarter decks

       // for constructor
       String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
               "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};
       String ruleFile = "rulesMap.txt";

       sim = new Simulation(chartsToInput,numberOfRounds,testStorage,ruleFile,numDecks,  penetration,  numOfGamblers,
               gamblerFlatBets,  trackGambler,  gamblerBet,
               betSpread,  numOfCounters,  trackedPlayerPosition,
               bankroll, rulesRow, countSystemFile,  deckEstimation);

       expectedKey = new SimulationKey(
        numberOfRounds,  numDecks,  penetration,  numOfGamblers,
        gamblerFlatBets,  trackGambler,  gamblerBet,
        betSpread, numOfCounters , trackedPlayerPosition ,
        bankroll, sim.getRulesArr(),
                countSystemFile,  deckEstimation
       );

   }

   @Test
    public void testSimKeyCreation(){

       // key should have been created with just the iniatialization
       SimulationKey actualKey = sim.getSimKey();
       assertEquals(expectedKey,actualKey);

   }

   @Test
   public void testSimKeyCompareTo(){

       // for constructor
       String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
               "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};
       String ruleFile = "rulesMap.txt";

       int numberOfRounds = 5; // adjusted temp
       int numDecks = 20;
       double penetration = 5;
       int numOfGamblers = 4;
       boolean gamblerFlatBets = false;
       boolean trackGambler = true;
       double gamblerBet = -1;
       double[][] betSpread = {{0,0,0,0,-1,-1,-1,-1,-1,-1,-1},{0,0,0,0,25,50,100,200,250,300,300}};
       int numOfCounters = 1;
       int trackedPlayerPosition = 2;
       double bankroll = 30000;
       HashMap<Integer, HashMap<String, String>> decisionMaps = new HashMap<>();
       String countSystemFile = "countSystemHiLo.txt";
       double deckEstimation = 0.5; // quarter decks

       SimulationKey altKey = new SimulationKey(
               numberOfRounds,  numDecks,  penetration,  numOfGamblers,
               gamblerFlatBets,  trackGambler,  gamblerBet,
               betSpread, numOfCounters , trackedPlayerPosition ,
               bankroll, sim.getRulesArr(),
               countSystemFile,  deckEstimation
       );

       assertTrue(altKey.compareTo(expectedKey)>0);

   }

   @Test
   public void testBooleans(){

       assertTrue(expectedKey.isNormGambler());
       assertFalse(expectedKey.isSurrender());
       assertTrue(expectedKey.isRSA());
       assertFalse(expectedKey.isS17());

   }

   @Test
   public void testToString(){
       String expected = "SimulationKey { numberOfRounds=0: numDecks=6: penetration=5.0: numOfGamblers=4: gamblerFlatBets=false: trackGambler=true: gamblerBet=-1.0: betSpread=[[0.0, 0.0, 0.0, 0.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0], [0.0, 0.0, 0.0, 0.0, 25.0, 50.0, 100.0, 200.0, 250.0, 300.0, 300.0]]: numOfCounters=1: trackedPlayerPosition=2: bankroll=30000.0: rulesArr=[3, H17, NoSurr, RSA, NormGambler, 1, 3, 5]: countSystemFile='countSystemHiLo.txt': deckEstimation=0.5 }";
       assertEquals(expected,expectedKey.toString());

   }


   @Test
   public void testReverseIndex(){
       testStorage.getSingleSimResult(expectedKey);
       String filepathForStore = "testFinalFile.txt";

       File storeFile = new File(filepathForStore);

       // Load the storage from a file
       if (storeFile.exists()) {
           testStorage.loadFromFile(filepathForStore);
       }

       HashMap<Boolean, List<SimulationKey>> bySurrender = bySurrender = testStorage.getSimBySurrender();
       assertNotNull(bySurrender);
       TreeMap<Double, List<SimulationKey>> byBankroll = testStorage.getSimsByBankroll();
       assertNotNull(byBankroll);

       List<SimulationKey> simsWIthSurrFalse = testStorage.getSimsByBoolean(false, SimStorageManager.SimParameter.SURRENDER);
       assertFalse(simsWIthSurrFalse.isEmpty());

       List<SimulationKey> simsWithDeckRange = testStorage.getAllSimsInRange(7,9 ,SimStorageManager.SimParameter.NUM_DECKS);
       assertTrue(simsWithDeckRange.isEmpty());

   }

   @Test
    public void testGetSimBySpecifics(){

       testStorage.getSingleSimResult(expectedKey);
       String filepathForStore = "testFinalFile.txt";

       File storeFile = new File(filepathForStore);

       // Load the storage from a file
       if (storeFile.exists()) {
           testStorage.loadFromFile(filepathForStore);
       }

       assertTrue(testStorage.getSimsByBankroll().get(30000.0).contains(expectedKey));
       assertTrue(testStorage.getSimsByNumDecks().get(6).contains(expectedKey));
       assertTrue(testStorage.getSimByPenetration().get(5.0).contains(expectedKey));
       assertTrue(testStorage.getSimBySurrender().get(false).contains(expectedKey));
       assertTrue(testStorage.getSimByRSA().get(true).contains(expectedKey));
       assertTrue(testStorage.getSimByNormGambler().get(true).contains(expectedKey));
       assertTrue(testStorage.getSimbyS17().get(false).contains(expectedKey));
       assertTrue(testStorage.getSimsByNumOfCounters().get(1).contains(expectedKey));
       assertTrue(testStorage.getSimsByNumRounds().get(0).contains(expectedKey));

   }

}
