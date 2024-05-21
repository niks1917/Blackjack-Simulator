import java.util.HashMap;

public interface ISimulation {

    /**
     * generates a 2D array where each row is a rule variation
     *
     * @param ruleFile the file containing rule variations (ruleMap.txt)
     *
     * @return rule file as 2D array
     */
    public void createRuleFile2DArr(String ruleFile);

    /**
     * calls each decision map creator method (for each strategy chart)
     * and then maps an integer to every decision map (the integer will be passed
     * into the gameRunner so the player can decide their decisions). The
     * integer value is based off the rules2DArr. The gambler will pick
     * which integer to choose based on the row of the rules2Darr fed to them.
     *
     * takes in each chart (in order of appearance in hash map)
     *
     * @return the master decision map of all decision maps
     */
    public void mapDecisionMaps(
            String CounterH17BSChart, String CounterS17BSChart,
            String GamblerNormBSChart, String GamblerCrazyBSChart,
            String H17Dealer, String S17Dealer);

    /**
     * creates a decision map for a counter
     *
     * @param strategyFile the file containing the strategy chart
     *
     * @return the decision map
     */
    public HashMap<String, String> createCounterDecisionMap(
            String strategyFile);

    /**
     * creates the decision map for a gambler
     *
     * @param strategyFile the file containing the strategy chart
     *
     * @return the decision map
     */
    public HashMap<String, String> createGamblerDecisionMap(
            String strategyFile);

    /**
     * creates the decision map for a dealer
     *
     * @param strategyFile the file containing the strategy chart
     *
     * @return the decision map
     */
    public HashMap<String, String> createDealerDecisionMap(
            String strategyFile);


    /**
     * run a sim with custom parameters. Will run for until number
     * of rounds is reached + the time it takes to finish the current
     * game.
     * update the sim results map
     */

    public void runSim();

    /**
     * create a unique string key for the sim - to be used while storing the sim in hashmap
     */

    public void createSimKey();

    /**
     * @return the unique sim key of this sim
     */
    public SimulationKey getSimKey();

    /**
     * updates the Hashmap and tree maps with the current sim being run
     */
    public void storeSimResult();

    /**
     * @return the sim resultsMap
     */
    public HashMap<String, Double> getSimResults();

    /**
     * @return number of rounds to play
     */

    public double getNumRoundsToPlayMillions();

    /**
     *
     *
     * @return get the number of rounds played so far
     */
    public double getNumRoundsSoFar();

    /**
     *
     * @param numRoundsSoFar number of rounds played so far
     *
     */
    public void setNumRoundsSoFar(double numRoundsSoFar);

    public double getTotalGain();
    public double getVarianceNumerator();
    public int getWinCount();

    public double getLossCount();
}
