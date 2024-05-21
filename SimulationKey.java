import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

    public class SimulationKey implements Comparable<SimulationKey> {

        private final int numberOfRounds;
        private final int numDecks;
        private final double penetration;
        private final int numOfGamblers;
        private final boolean gamblerFlatBets;
        private final boolean trackGambler;
        private final double gamblerBet;
        private final double[][] betSpread;
        private final int numOfCounters;
        private final int trackedPlayerPosition;
        private final double bankroll;
        private final String[] rulesArr;
        private final String countSystemFile;
        private final double deckEstimation;


        public SimulationKey(int numberOfRounds, int numDecks, double penetration, int numOfGamblers,
                             boolean gamblerFlatBets, boolean trackGambler, double gamblerBet,
                             double[][] betSpread, int numOfCounters, int trackedPlayerPosition,
                             double bankroll, String[] rulesArr,
                             String countSystemFile, double deckEstimation) {

            this.numberOfRounds = numberOfRounds;
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
            this.rulesArr = rulesArr;
            this.countSystemFile = countSystemFile;
            this.deckEstimation = deckEstimation;
        }



        // Implementing Comparable to define a consistent ordering
        @Override
        public int compareTo(SimulationKey other) {

            int cmp = Integer.compare(this.numberOfRounds, other.numberOfRounds);
            if (cmp == 0) cmp = Integer.compare(this.numDecks, other.numDecks);
            if (cmp == 0) cmp = Double.compare(this.penetration, other.penetration);
            if (cmp == 0) cmp = Integer.compare(this.numOfGamblers, other.numOfGamblers);
            if (cmp == 0) cmp = Boolean.compare(this.gamblerFlatBets, other.gamblerFlatBets);
            if (cmp == 0) cmp = Boolean.compare(this.trackGambler, other.trackGambler);
            if (cmp == 0) cmp = Double.compare(this.gamblerBet, other.gamblerBet);
            if (cmp == 0) cmp = Arrays.deepHashCode(this.betSpread) - Arrays.deepHashCode(other.betSpread);
            if (cmp == 0) cmp = Integer.compare(this.numOfCounters, other.numOfCounters);
            if (cmp == 0) cmp = Integer.compare(this.trackedPlayerPosition, other.trackedPlayerPosition);
            if (cmp == 0) cmp = Double.compare(this.bankroll, other.bankroll);
            return cmp;
        }


        public int getNumberOfRounds() {
            return numberOfRounds;
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

        public boolean isSurrender(){

            if (rulesArr[2].equals("Surr")){
                return true;
            }
            return false;
        }

        public boolean isS17(){
            if(rulesArr[1].equals("S17")){
                return true;
            }
            return false;
        }

        public boolean isRSA(){
            if (rulesArr[3].equals("RSA")){
                return true;
            }
            return false;
        }

        public boolean isNormGambler(){
            if (rulesArr[4].equals("NormGambler")){
             return true;
            }
            return false;
        }


        @Override
        public String toString() {
            // Using StringBuilder for efficient string concatenation

            // Return the constructed string
            return "SimulationKey { " +
                    "numberOfRounds=" + numberOfRounds + ": " +
                    "numDecks=" + numDecks + ": " +
                    "penetration=" + penetration + ": " +
                    "numOfGamblers=" + numOfGamblers + ": " +
                    "gamblerFlatBets=" + gamblerFlatBets + ": " +
                    "trackGambler=" + trackGambler + ": " +
                    "gamblerBet=" + gamblerBet + ": " +
                    "betSpread=" + Arrays.deepToString(betSpread) + ": " +
                    "numOfCounters=" + numOfCounters + ": " +
                    "trackedPlayerPosition=" + trackedPlayerPosition + ": " +
                    "bankroll=" + bankroll + ": " +
                    "rulesArr=" + Arrays.toString(rulesArr) + ": " +
                    "countSystemFile='" + countSystemFile + "': " +
                    "deckEstimation=" + deckEstimation +
                    " }";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            SimulationKey other = (SimulationKey) obj;

            // Compare all relevant fields
            return numberOfRounds == other.numberOfRounds &&
                    numDecks == other.numDecks &&
                    Double.compare(penetration, other.penetration) == 0 &&
                    numOfGamblers == other.numOfGamblers &&
                    gamblerFlatBets == other.gamblerFlatBets &&
                    trackGambler == other.trackGambler &&
                    Double.compare(gamblerBet, other.gamblerBet) == 0 &&
                    Arrays.deepEquals(betSpread, other.betSpread) &&
                    numOfCounters == other.numOfCounters &&
                    trackedPlayerPosition == other.trackedPlayerPosition &&
                    Double.compare(bankroll, other.bankroll) == 0 &&
                    Arrays.equals(rulesArr, other.rulesArr) &&
                    Objects.equals(countSystemFile, other.countSystemFile) &&
                    Double.compare(deckEstimation, other.deckEstimation) == 0;
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(numberOfRounds, numDecks, penetration, numOfGamblers, gamblerFlatBets,
                    trackGambler, gamblerBet, numOfCounters, trackedPlayerPosition, bankroll, countSystemFile, deckEstimation);
            result = 31 * result + Arrays.deepHashCode(betSpread);
            result = 31 * result + Arrays.hashCode(rulesArr);
            return result;
        }

        
    }

