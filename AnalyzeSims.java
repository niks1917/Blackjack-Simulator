import com.sun.source.tree.BreakTree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzeSims implements IAnalyzeSims {


    public static final int ROUNDS_PER_HR = 75;
    private final SimStorageManager simStore;

    // constructor - runs all analytics for a given sim
    public AnalyzeSims(){
        // creating a storage for all the sims
        this.simStore = new SimStorageManager();
    }

    public void runAnalyse(HashMap<String,Double> simResultsMap, double roundsPlayed, double bankroll) {

        System.out.println("/-------------------/");
        System.out.println("SIMULATION RESULTS: ");
        // TODO add print of all the sim parameters here with the shorthand

        // running all analytics

        double meanHourly = calculateExpectationPerHour(simResultsMap,roundsPlayed);
        double SDHourly = calculateSDHourly(simResultsMap,roundsPlayed);
        System.out.println("Bankroll: $" + bankroll );
        System.out.println("Rounds run: " + Math.round(roundsPlayed));
        System.out.println("Gain / hour: $" + Math.round(meanHourly));
        System.out.println("Standard Deviation / hour: $" + Math.round(SDHourly));
//        System.out.println("Risk of Ruin: " + calculateRiskOfRuin(SDHourly,meanHourly,bankroll)*100 + "%");
        System.out.println("Risk of Ruin: " + String.format("%.2f%%", calculateRiskOfRuin(SDHourly, meanHourly, bankroll) * 100));
        System.out.println("N0 [#hrs to get to SD = EV]: " + Math.round(calculateTimeToConquer(SDHourly,meanHourly)));
//        System.out.println("win rate: " + calculateWinRate(simResultsMap));
        System.out.println("win rate: " + String.format("%.2f%%", calculateWinRate(simResultsMap) * 100));


    }


    @Override
    public double calculateExpectationPerHour(HashMap<String,Double> simResultsMap,double roundsPlayed) {

        // total gain calculated in run sim method
        double totalGain = simResultsMap.get("GAIN");

        double hrs = roundsPlayed/ROUNDS_PER_HR;
//        double hrs = (simResultsMap.get("WON")+simResultsMap.get("LOST"))/ROUNDS_PER_HR;

        return totalGain / hrs;

    }

    @Override
    public double calculateSDHourly(HashMap<String,Double> simResultsMap, double roundsPlayed) {

        // sum of all squared gain values
        double varianceNum = simResultsMap.get("VARIANCE_NUM");

//        double varianceInRounds = varianceNum/(simResultsMap.get("WON")+simResultsMap.get("LOST"));
                double varianceInRounds = (varianceNum/roundsPlayed);
        double varianceHrs = varianceInRounds*ROUNDS_PER_HR;

        return Math.sqrt(varianceHrs);
    }


    @Override
    public double calculateRiskOfRuin(double SDHourly, double EVHourly, double bankroll) {

//        // OLDER FORMULA APPROACH
//        double num = 1 - (EVHourly / SDHourly);
//        System.out.println("ror num: " + num);
//        double den = 1 + (EVHourly / SDHourly);
//        System.out.println("ror den: " + den);
//        double power = bankroll / SDHourly;
//        System.out.println("power " + power);
//        return Math.pow(num / den, power);

        // Using Sileo's formula - http://www12.plala.or.jp/doubledown/poker/sileo.pdf

        double num = (-2*EVHourly*bankroll);
        double den = Math.pow(SDHourly,2);

        double ror = Math.exp(num/den);
        if (ror > 1){
            return 1;
        }

        return ror;

    }

    @Override
    public double calculateTimeToConquer(double SDHourly, double EVHourly) {
        // N0 == num of hours to get SD = EV statistically
        return Math.pow(SDHourly / EVHourly, 2);
    }

    @Override
    public double calculateWinRate(HashMap<String,Double> simResultsMap) {

        return simResultsMap.get("WON")/(simResultsMap.get("WON")+simResultsMap.get("LOST"));
    }


    public SimStorageManager getSimStore(){return this.simStore;}

}
