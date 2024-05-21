import com.sun.source.tree.Tree;

import java.io.*;
import java.util.*;

public class SimStorageManager {

    public enum SimParameter {
        BANKROLL, NUM_DECKS, NUM_ROUNDS, PENETRATION, NUM_OF_COUNTERS, SURRENDER, RSA, S17, NORM_GAMBLER
    }

    // each sim object is the key,
    // the value map has the variance, win count, bet sum (calculating during the sim run)
    private final HashMap<SimulationKey, Map<String,Double>> primaryStorage;

    /*
     each key is the 'parameter value', and the value is a list of all the sims corresponding to that value
     using treemap because will use submap() for range queries on the keys
     */

    private final TreeMap<Double, List<SimulationKey>> simsByBankroll;
    private final TreeMap<Integer, List<SimulationKey>> simsByNumRounds;
    private final TreeMap<Integer, List<SimulationKey>> simsByNumDecks;
    private final TreeMap<Double, List<SimulationKey>> simByPenetration;
    private final TreeMap<Integer, List<SimulationKey>> simsByNumOfCounters;

    /*
    using hashmaps for the boolean ones - don't need submap and headmap for range queries on keys here
     */
    private final HashMap<Boolean, List<SimulationKey>> simBySurrender;
    private final HashMap<Boolean, List<SimulationKey>> simbyS17;
    private final HashMap<Boolean, List<SimulationKey>> simByNormGambler;
    private final HashMap<Boolean, List<SimulationKey>> simByRSA;


    // default constructor
    public SimStorageManager(){
        this.primaryStorage = new HashMap<>();
        this.simsByBankroll = new TreeMap<>();
        this.simsByNumRounds = new TreeMap<>();
        this.simsByNumDecks = new TreeMap<>();
        this.simByPenetration = new TreeMap<>();
        this.simsByNumOfCounters = new TreeMap<>();
        this.simBySurrender = new HashMap<>();
        this.simbyS17 = new HashMap<>();
        this.simByNormGambler = new HashMap<>();
        this.simByRSA = new HashMap<>();
    }

    public void addSimToStorage(SimulationKey simKey,Map<String,Double> simResults){

        primaryStorage.put(simKey,simResults);

        // BANKROLL
        List<SimulationKey> bankrollSimList = simsByBankroll.computeIfAbsent(simKey.getBankroll(), k -> new ArrayList<SimulationKey>());
        bankrollSimList.add(simKey);

        // NUM ROUNDS
        List<SimulationKey> numRoundsSimList = simsByNumRounds.computeIfAbsent(simKey.getNumberOfRounds(), k -> new ArrayList<SimulationKey>());
        numRoundsSimList.add(simKey);

        // NUM DECKS
        List<SimulationKey> numDecksSimList = simsByNumDecks.computeIfAbsent(simKey.getNumDecks(), k -> new ArrayList<SimulationKey>());
        numDecksSimList.add(simKey);

        // % DEALT
        //TODO check later
        List<SimulationKey> perDealtList = simByPenetration.computeIfAbsent(simKey.getPenetration(), k -> new ArrayList<SimulationKey>());
        perDealtList.add(simKey);

        // NUM COUNTERS
        List<SimulationKey> numCountersList = simsByNumOfCounters.computeIfAbsent(simKey.getNumOfCounters(), k -> new ArrayList<SimulationKey>());
        numCountersList.add(simKey);

        List<SimulationKey> surrennderList = simBySurrender.computeIfAbsent(simKey.isSurrender(), k -> new ArrayList<SimulationKey>());
        surrennderList.add(simKey);

        List<SimulationKey> RSAList = simByRSA.computeIfAbsent(simKey.isRSA(), k -> new ArrayList<SimulationKey>());
        RSAList.add(simKey);

        List<SimulationKey> S17List = simbyS17.computeIfAbsent(simKey.isS17(), k -> new ArrayList<SimulationKey>());
        S17List.add(simKey);

        List<SimulationKey> NormGambler = simByNormGambler.computeIfAbsent(simKey.isNormGambler(), k -> new ArrayList<SimulationKey>());
        NormGambler.add(simKey);


    }



    public Map<String,Double> getSingleSimResult(SimulationKey simKey){

        return this.primaryStorage.get(simKey);
    }

    public List<SimulationKey> getAllSimsInRange(double min, double max, SimParameter simParameter){

        // TODO
        ArrayList<SimulationKey> finalSims = new ArrayList<>();

        // making the tree map variable based on the param to be used
        switch (simParameter) {
            case BANKROLL -> {
                SortedMap<Double, List<SimulationKey>> subMap = simsByBankroll.subMap(min, max);
                // adding to finalSims
                for (List<SimulationKey> eachList: subMap.values()){
                    finalSims.addAll(eachList);
                }
            }
            case NUM_DECKS -> {
                SortedMap<Integer, List<SimulationKey>> subMap = simsByNumDecks.subMap((int) min, (int) max);
                // adding to finalSims
                for (List<SimulationKey> eachList: subMap.values()){
                    finalSims.addAll(eachList);
                }
            }
            case NUM_ROUNDS -> {
                SortedMap<Integer, List<SimulationKey>> subMap = simsByNumRounds.subMap((int) min, (int) max);
                // adding to finalSims
                for (List<SimulationKey> eachList: subMap.values()){
                    finalSims.addAll(eachList);
                }
            }

            case NUM_OF_COUNTERS -> {
                SortedMap<Integer, List<SimulationKey>> subMap = simsByNumOfCounters.subMap((int) min, (int) max);
                // adding to finalSims
                for (List<SimulationKey> eachList: subMap.values()){
                    finalSims.addAll(eachList);
                }
            }
            case PENETRATION -> {
                SortedMap<Double, List<SimulationKey>> subMap = simByPenetration.subMap(min, max);
                // adding to finalSims
                for (List<SimulationKey> eachList: subMap.values()){
                    finalSims.addAll(eachList);
                }
            }
        };

        return finalSims;
    }

    List<SimulationKey> getSimsByBoolean(boolean bool, SimParameter simParameter){

        ArrayList<SimulationKey> finalSimsByBoolean = new ArrayList<>();

        switch (simParameter) {
            case SURRENDER -> {
                if (bool){
                    finalSimsByBoolean = (ArrayList<SimulationKey>) simBySurrender.get(true);
                }
                else {
                    finalSimsByBoolean = (ArrayList<SimulationKey>) simBySurrender.get(false);
                }
                }

            case RSA -> {
                if (bool){
                    finalSimsByBoolean = (ArrayList<SimulationKey>) simByRSA.get(true);
                }
                else {
                    finalSimsByBoolean = (ArrayList<SimulationKey>) simByRSA.get(false);
                }
            }

            case S17 -> {
                if (bool){
                    finalSimsByBoolean = (ArrayList<SimulationKey>) simbyS17.get(true);
                }
                else {
                    finalSimsByBoolean = (ArrayList<SimulationKey>) simbyS17.get(false);
                }
            }

            case NORM_GAMBLER -> {
                if (bool) {
                    finalSimsByBoolean = (ArrayList<SimulationKey>) simByNormGambler.get(true);
                } else {
                    finalSimsByBoolean = (ArrayList<SimulationKey>) simByNormGambler.get(false);
                }
            }

            }


        return finalSimsByBoolean;
    }

    public HashMap<SimulationKey, Map<String, Double>> getPrimaryStorage() {
        return primaryStorage;
    }

    public TreeMap<Double, List<SimulationKey>> getSimsByBankroll() {
        return simsByBankroll;
    }


    public TreeMap<Integer, List<SimulationKey>> getSimsByNumRounds() {
        return simsByNumRounds;
    }

    public TreeMap<Integer, List<SimulationKey>> getSimsByNumDecks() {
        return simsByNumDecks;
    }

    public TreeMap<Double, List<SimulationKey>> getSimByPenetration() {
        return simByPenetration;
    }

    public TreeMap<Integer, List<SimulationKey>> getSimsByNumOfCounters() {
        return simsByNumOfCounters;
    }

    public HashMap<Boolean, List<SimulationKey>> getSimBySurrender(){return simBySurrender;}

    public HashMap<Boolean, List<SimulationKey>> getSimbyS17() {
        return simbyS17;
    }

    public HashMap<Boolean, List<SimulationKey>> getSimByNormGambler() {
        return simByNormGambler;
    }

    public HashMap<Boolean, List<SimulationKey>> getSimByRSA() {
        return simByRSA;
    }

    public void saveToFile(String filePath, SimulationKey simKey, Map<String, Double> simResults) throws IOException {
        // using file writer true for appending vs overwriting data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            // using csv formatting for this bit
                String line = String.format("%s,%f,%f,%f,%f",
                        simKey.toString(),
                        simResults.getOrDefault("GAIN", 1.0),
                        simResults.getOrDefault("VARIANCE_NUM", 1.0),
                        simResults.getOrDefault("WON", 1.0),
                        simResults.getOrDefault("LOST", 1.0)
                );
                writer.write(line);
                writer.newLine();
            }

    }

    public void loadFromFile(String filePath){

        System.out.println("sim results loading from file...");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
//                System.out.println("line" + line);
                String[] initialFields = line.split("},");
//                System.out.println("initialFields0: " + initialFields[0]);
//                System.out.println("initialFields1: " + initialFields[1]);
                String[] resultFields = initialFields[1].split(",");
//                System.out.println("fields0: " + resultFields[0]);
//                System.out.println("fields1: " + resultFields[1]);
//                System.out.println("fields2: " + resultFields[2]);
//                System.out.println("fields3: " + resultFields[3]);
                if (resultFields.length != 4) {
                    System.out.println("skipping malformed");
                    continue; // Skip malformed lines
                }

                SimulationKey key = recreateFromString(initialFields[0]);

                Map<String, Double> results = new HashMap<>();
                results.put("GAIN", Double.parseDouble(resultFields[0]));
                results.put("VARIANCE_NUM", Double.parseDouble(resultFields[1]));
                results.put("WON", Double.parseDouble(resultFields[2]));
                results.put("LOST", Double.parseDouble(resultFields[3]));

                addSimToStorage(key,results);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public SimulationKey recreateFromString(String key) {

        String inner = key.replaceFirst("^SimulationKey \\{ ", "").replaceFirst(" \\}$", "");
        String[] pairs = inner.split(": ");
//
//        for (int i = 0; i < pairs.length; i++){
//            System.out.println(pairs[i]);
//        }

        // Extract each field value from the key-value pairs
        int numberOfRounds = Integer.parseInt(pairs[0].split("=")[1]);
        int numDecks = Integer.parseInt(pairs[1].split("=")[1]);

        double penetration = Double.parseDouble(pairs[2].split("=")[1]);
        int numOfGamblers = Integer.parseInt(pairs[3].split("=")[1]);
        boolean gamblerFlatBets = Boolean.parseBoolean(pairs[4].split("=")[1]);
        boolean trackGambler = Boolean.parseBoolean(pairs[5].split("=")[1]);
        double gamblerBet = Double.parseDouble(pairs[6].split("=")[1]);

        // Parse 2D array `betSpread`
        String betSpreadString = pairs[7].split("=")[1];
        double[][] betSpread = parse2DArray(betSpreadString);
        int numOfCounters = Integer.parseInt(pairs[8].split("=")[1]);
        int trackedPlayerPosition = Integer.parseInt(pairs[9].split("=")[1]);
        double bankroll = Double.parseDouble(pairs[10].split("=")[1]);

        // Parse 1D array `rulesArr`
        String rulesArrString = pairs[11].split("=")[1];
        String[] rulesArr = parseStringArray(rulesArrString);
        String countSystemFile = pairs[12].split("=")[1].replace("'", "");
        double deckEstimation = Double.parseDouble(pairs[13].split("=")[1]);

        return new SimulationKey(numberOfRounds, numDecks, penetration, numOfGamblers,
                gamblerFlatBets, trackGambler, gamblerBet, betSpread, numOfCounters,
                trackedPlayerPosition, bankroll, rulesArr,
                countSystemFile, deckEstimation);
    }

    private static double[][] parse2DArray(String input) {
        // Remove the surrounding square brackets
        input = input.replace("[[", "").replace("]]", "");
        String[] rows = input.split("], \\[");
        double[][] result = new double[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] values = rows[i].split(", ");
            result[i] = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
        }
        return result;
    }

    private static String[] parseStringArray(String input) {
        // Remove the surrounding square brackets and split by ", "
        input = input.replace("[", "").replace("]", "");
        return input.split(", ");
    }

}
