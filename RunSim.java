import java.io.File;
import java.io.IOException;
import java.util.*;

public class RunSim {


    //the actual runner main method

/*
JACOB's BLACKJACK RULES:
1. If gambler flat bet = false, then gamblerBet = -1
2. Penetration (# decks being dealt) < num of decks
 */

    public static void main(String[] args) {

        // initialize analyzer and the storage
        AnalyzeSims mainAnalyzer = new AnalyzeSims();
        SimStorageManager mainStore = mainAnalyzer.getSimStore();
        String filepathForStore = "finalSimRuns.txt";

        File storeFile = new File(filepathForStore);
        Scanner scr = new Scanner(System.in);

        // Load the storage from a file
        if (storeFile.exists()) {
            mainStore.loadFromFile(filepathForStore);
        }


        System.out.print("Do you want to look up by specific parameter: (True/False) ");
        boolean lookUp = scr.nextBoolean();
        
        if (!lookUp) {
            System.out.print("Enter number of rounds to simulate: ");
            double numberOfRounds = scr.nextDouble();
            System.out.print("Enter number of decks: ");
            int numDecks = scr.nextInt();
            System.out.print("Enter penetration: ");
            double penetration = scr.nextDouble();
            System.out.print("Enter number of gamblers: ");
            int numOfGamblers = scr.nextInt();
            System.out.print("Does gambler flat bet? (true/false): ");
            boolean gamblerFlatBets = scr.nextBoolean();
            System.out.print("Track gambler? (true/false): ");
            boolean trackGambler = scr.nextBoolean();
            System.out.print("Enter gambler's bet: ");
            double gamblerBet = scr.nextDouble();
            double[][] betSpread = {{25, 25, 25, 25, -1, -1, -1, -1, -1, -1, -1}, {0, 0, 0, 0, 25, 50, 100, 200, 250, 300, 300}};
            System.out.print("Enter number of counters: ");
            int numOfCounters = scr.nextInt();
            System.out.print("Enter tracked player position: ");
            int trackedPlayerPosition = scr.nextInt();
            System.out.print("Enter bankroll: ");
            double bankroll = scr.nextDouble();
            System.out.print("Enter rules row: ");
            int rulesRow = scr.nextInt();

            String countSystemFile = "countSystemHiLo.txt";

            System.out.print("Enter deck estimation: ");
            double deckEstimation = scr.nextDouble();


            // for constructor
            String[] chartsToInput = {"CounterH17BSChart.txt", "CounterS17BSChart.txt", "GamblerNormBSChart.txt",
                    "GamblerCrazyBSChart.txt", "H17Dealer.txt", "H17Dealer.txt", "S17Dealer.txt"};
            String ruleFile = "rulesMap.txt";

            // create sim
            Simulation sim1;
            sim1 = new Simulation(chartsToInput, numberOfRounds, mainStore, ruleFile, numDecks, penetration, numOfGamblers,
                    gamblerFlatBets, trackGambler, gamblerBet,
                    betSpread, numOfCounters, trackedPlayerPosition,
                    bankroll, rulesRow, countSystemFile, deckEstimation);

            SimulationKey sim1Key = sim1.getSimKey();

            // check if the sim already has been run (simKey was created in sim constructor)
            if (!mainStore.getPrimaryStorage().containsKey(sim1Key)) {
                // run sim
                System.out.println("Running a new sim...\n" + sim1Key.toString());
                sim1.runSim();

                try {
                    mainStore.saveToFile(filepathForStore, sim1Key, mainStore.getSingleSimResult(sim1Key));
                    System.out.println("Simulation results appended to the file.");
                } catch (IOException e) {
                    System.err.println("Error writing simulation results to the file: " + e.getMessage());
                }
            } else {
                System.out.println("Analyzing sim results...\n" + sim1Key.toString());
//            System.out.println(mainStore.getPrimaryStorage().get(sim1Key));
                System.out.println("Sim params: " + sim1Key);
                mainAnalyzer.runAnalyse((HashMap<String, Double>) mainStore.getPrimaryStorage().get(sim1Key), sim1Key.getNumberOfRounds(), sim1Key.getBankroll());
            }
        } else {

            // ask user for a parameter to lookup
            System.out.println("Enter the parameter you want to see. Please enter the number, and here are all the options: ");
            System.out.println("1.BANKROLL, 2.NUM_DECKS, 3.NUM_ROUNDS, 4.PENETRATION, 5.NUM_OF_COUNTERS, 6.NORM_GAMBLER, 7.S17, 8.RSA, 9.SURRENDER");
            int index = scr.nextInt();

            List<SimulationKey> simsList = null;


                if (index == 9) {
                    System.out.println("Surrender True / False?");
                    boolean boolChosen = scr.nextBoolean();
                     simsList =  mainStore.getSimsByBoolean(boolChosen, SimStorageManager.SimParameter.SURRENDER);
                }
                if (index == 6) {
                    System.out.println("NORM GAMBLER True / False?");
                    boolean boolChosen = scr.nextBoolean();
                    simsList =  mainStore.getSimsByBoolean(boolChosen, SimStorageManager.SimParameter.NORM_GAMBLER);
                }
                if (index == 7) {
                    System.out.println("S17 GAMBLER True / False?");
                    boolean boolChosen = scr.nextBoolean();
                    simsList =  mainStore.getSimsByBoolean(boolChosen, SimStorageManager.SimParameter.S17);
                }
                if (index == 8) {
                    System.out.println("RSA True / False?");
                    boolean boolChosen = scr.nextBoolean();
                    simsList =  mainStore.getSimsByBoolean(boolChosen, SimStorageManager.SimParameter.RSA);
                }

                if (index ==1) {
                    System.out.println("Enter the min");
                    int min = scr.nextInt();
                    System.out.println("Enter the max");
                    int max = scr.nextInt();
                    simsList = mainStore.getAllSimsInRange(min, max, SimStorageManager.SimParameter.BANKROLL);
                }

            if (index ==2) {
                System.out.println("Enter the min");
                int min = scr.nextInt();
                System.out.println("Enter the max");
                int max = scr.nextInt();
                simsList = mainStore.getAllSimsInRange(min, max, SimStorageManager.SimParameter.NUM_DECKS);
            }

            if (index ==3) {
                System.out.println("Enter the min");
                int min = scr.nextInt();
                System.out.println("Enter the max");
                int max = scr.nextInt();
                simsList = mainStore.getAllSimsInRange(min, max, SimStorageManager.SimParameter.NUM_ROUNDS);
            }

            if (index ==4) {
                System.out.println("Enter the min");
                int min = scr.nextInt();
                System.out.println("Enter the max");
                int max = scr.nextInt();
                simsList = mainStore.getAllSimsInRange(min, max, SimStorageManager.SimParameter.PENETRATION);
            }

            if (index ==5) {
                System.out.println("Enter the min");
                int min = scr.nextInt();
                System.out.println("Enter the max");
                int max = scr.nextInt();
                simsList = mainStore.getAllSimsInRange(min, max, SimStorageManager.SimParameter.NUM_OF_COUNTERS);
            }


            if (simsList == null){
                System.out.println("sim list not created... check for error");
            }
            else if (simsList.isEmpty()){
                System.out.println("Sorry! No sims with these filters...");
            }

                for (SimulationKey each: simsList){
                    System.out.println("Sim parameters: " + each);
                    mainAnalyzer.runAnalyse((HashMap<String, Double>) mainStore.getPrimaryStorage().get(each), each.getNumberOfRounds(), each.getBankroll());
                }


    }

}

}
