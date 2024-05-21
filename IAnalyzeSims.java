import java.util.Collection;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;

public interface IAnalyzeSims {

    // analyze sim will first extract sim outputs and then calculate the stats from the file path output
    // (1) check if sim exists,
    // (2) if sim exists, pull result,
    // (3) if doesn't ask for user input on whether to run the sim

    public void runAnalyse(HashMap<String,Double> simResultsMap, double roundsPlayed, double bankroll);

    /**
     * get rounds per hr from getNumOfHandsPerHour() method
     * get average gain per round from calculateExpectation()
     * EV per hour = (Total gain / num of hours)
     *             = Total gain / (total num of rounds / rounds per hour)
     * @param simResultsMap being run
     * @return total gain/loss per hour
     */
    public double calculateExpectationPerHour(HashMap<String,Double> simResultsMap, double roundsPlayed);

    /**
     * get mean from calculateExpectationPerHour method
     * calculate variance hourly -from running variance total from sim
     * calculate SD - sqrt(variance)
     * @param simResultsMap - being run
     * @return SD value
     */
    public double calculateSDHourly(HashMap<String,Double> simResultsMap, double roundsPlayed);


    /**
     * RoR = e^((-2*B*EV)/variance)
     * SILEOS approach - http://www12.plala.or.jp/doubledown/poker/sileo.pdf
     * where w is hourly expectation, sd is hourly sd, b is player bankroll
     * @param SDHourly,EVHourly,bankroll
     * @return ror (probability percentage) value
     */
   public double calculateRiskOfRuin(double SDHourly, double EVHourly, double bankroll);


    /**
     * N0 = number of bets needed for exepcted outcome to become apparent in the variability
     * N0 = (SDHourly/EVHourly)^2
     * @param SDHourly,EVHourly
     * @return num of hours of play required
     */
   public double calculateTimeToConquer(double SDHourly, double EVHourly);

    /**
     * win rate = win / win + loss
     * @param simResultsMap
     * @return
     */
    public double calculateWinRate(HashMap<String,Double> simResultsMap);

   // -------------------------------------------------------------------------------


}
