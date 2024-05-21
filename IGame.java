import java.util.HashMap;
import java.util.List;

public interface IGame {

    /**
     * plays one shoe and then brings back game to initial state
     * skips players if their hand list is empty
     *
     */
    public void playShoe();

    /**
     * creates count system map from file
     *
     * @param countSystemFile count system file
     *
     */
    public HashMap<String, Integer> createCountSystemMap(String countSystemFile);

    /**
     * splits current hand and bets into two hands of one
     * card each, inserting the new hand right after the
     * current hand in the hands list. Make sure to account for later
     * implementation i.e...The class looping through the hands
     * (Game class) must be stopped and the current index must be
     * saved, then the loop must be modified and then restarted from
     * the saved index within the split method. if the hand is aces make
     * canhit is turned off.
     *
     *
     * @param indexOfPlayer index of player who is splitting in players list
     * @param handsList the hands list with the hand splitting
     * @param indexOfHand index of the player's hand
     *                    in the hand list
     */
    public void split(int indexOfPlayer, List<PlayerHand> handsList, int indexOfHand);

    /**
     * calls placeBets() from player and then deals 2 cards to all players
     * that have hands in their hand list, and also deals to the dealer,
     * deciding that the first card dealt to the dealer is their upcard,
     * and the other is their hole card. Subtracts dealer holecard from
     * count and readjusts count
     *
     */
    public void roundInit();

    /**
     * checks if dealer has T upcard - if so, check if dealer has blackjack.
     * If dealer has blackjack then go through all players and if they don't
     * have blackjack, discard their hand. If they do have blackjack, pay
     * them their original bet back and discard their hand. Then set round to
     * over.
     *
     */
    public void checkDealerT();

    /**
     * after all cards are dealt, check if dealer has an Ace up, and then
     * if so will ask player hands for insurance.
     *
     * If a player takes insurance
     * and the dealer has blackjack, then the hand will push and the round will
     * be set to over. If the player takes insurance and has blackjack and the dealer
     * has blackjack the hand will be paid even money. IF the player does not take insurance
     * and the dealer has blackjack then they will not recieve any money and the round will be
     * over. If the player does not take insurance and has blackjack and the dealer has blackjack
     * then they will push and the round will be over. Make sure to discard all hands in the
     * case of a dealer blackjack.
     *
     * If a player takes insurance and the dealer does not have blackjack, then their bet will
     * be subtracted from their bankroll and the round will continue. If a player takes insurance
     * and has blackjack and the dealer does not have blackjack then they will be paid even money
     * and their hand will be set to isOver and discarded.IF a player does not take insurance and
     * the dealer does not have blackjack then nothing will happen and the round will continue.
     * If a player does not take insurance and has blackjack and the dealer does not have blackjack
     * then their hand will be set to has blackjack and the round will continue
     *
     */
    public void runInsurance();

    /**
     * check all hands for blackjack and if one does, set hand to is over,
     * discard it and pay the player 3:2
     *
     */
    public void checkForBlackJacks();

    /**
     * check if hand has busted. If so return true
     *
     * @param hand the hand to check
     *
     * @return true if hand is > 21
     */
    public boolean isBustedPlayer(PlayerHand hand);

    /**
     * check if hand has busted. If so return true
     *
     * @param hand the hand to check
     *
     * @return true if hand is > 21
     */
    public boolean isBustedDealer(DealerHand hand);

    /**
     * adds the dealer's hole card to the running count and
     * updates the true count
     */
    public void holeCardUpdate();

    /**
     * when the dealer has either made a hand (stood) or busted,
     * this method will be run either compare all
     * waiting hands to the dealer, and make payouts, or if the dealer
     * busted, then all waiting hands will win.
     */
    public void compareHands();

    /**
     * deals card using deck method and updates the running count
     * and then updates the true count by calling updateCount
     * Also, adds card to discards
     *
     * @return the dealt card
     *
     */
    public Card dealCard();

    /**
     * updates true count using running count, discard size, and deck estimation
     *
     *
     */
    public void updateCount();

    /**
     * calculates the exact true count for taking insurance
     *
     * @return the exact true count
     */
    public double insuranceCount();

    /**
     * pay the player a specified amount based on the
     * current bet associated with the hand
     *
     *
     * @param bet the current bet of the hand
     * @param player the player to be paid
     *
     */
    public void payPlayer(IPlayer player, double bet);

    /**
     * compares the player's hand and the dealer's hand
     *
     * @param playerhand the player's hand
     *
     * @return -1 if dealer wins, 0 if push, or 1 if player wins
     */
    public int compareWithDealer(PlayerHand playerhand);

    /**
     * puts the rest of the deck in the discards
     * calls the second deck constructor using the discard tray
     * to reshuffle the cards and sets it as the deck.
     * Clears the discards
     * resets discards counter
     * resets running and true count
     * sets round is over to false
     * sets discard counter to 0
     */
    public void clearShoe();

    /**
     *
     * @return the deck
     *
     */
    public Deck getDeck();

    /**
     *
     * @return the dealer
     *
     */
    public Dealer getDealer();

    /**
     *
     * @return the countSystem Map
     *
     */
    public HashMap<String, Integer> getCountSystem();

    /**
     *
     * @return the running count
     *
     */
    public int getRunningCount();

    /**
     *
     * @return the true count
     *
     */
    public int getTrueCount();

    /**
     *
     * determine if deck is finished using discard size
     * and num cards to deal
     *
     * @return if the deck is finished
     *
     */
    public boolean deckIsFinished();

    /**
     *
     *@return if round is over
     */
    public boolean roundIsOver();

    /**
     *
     *@param roundIsOver sets if round is over
     */
    public void setRoundIsOver(boolean roundIsOver);

    /**
     *
     *@return the number of rounds played so far
     */
    public int getRoundsPlayed();

    /**
     *
     *@param roundsPlayed set the number of rounds played
     */
    public void setRoundsPlayed(int roundsPlayed);

    /**
     *
     *@return the deck penetration
     */
    public double getPenetration();

    /**
     *
     *@return the number of decks
     */
    public int getNumDecks();

    /**
     *
     *@return if this game has surrender
     */
    public boolean getSurrender();

    /**
     *
     *@return if this game has RSA
     */
    public boolean getRSA();

    /**
     *
     *@return if this game is H17
     */
    public boolean getH17();

    /**
     *
     *@return the tracked players total winnings (negative if loss)
     */
    public double getTotalGain();

    /**
     *
     *@param totalGain set total gain
     */
    public void setTotalGain(double totalGain);

    /**
     *
     *@return the running variance number (result of each hand squared and added to total)
     */
    public double getRunningVariance();

    /**
     *
     *@param runningVariance set running variance number
     */
    public void setRunningVariance(double runningVariance);

    /**
     *
     *@return the amount of rounds won
     */
    public int getWonRounds();

    /**
     *
     *@param wonRounds set the amount of rounds won
     */
    public void setWonRounds(int wonRounds);

    /**
     *
     *@return the amount of rounds lost
     */
    public int getLostRounds();

    /**
     *
     *@param lostRounds set the amount of rounds lost
     */
    public void setLostRounds(int lostRounds);

    /**
     *
     *@return the deck estimation number
     */
    public double getDeckEstimation();

    /**
     *
     *@return true if gamblers are crazy
     */
    public boolean isGamblerCrazy();

    /**
     *
     *@return the tracked player position
     */
    public int getTrackedPlayerPosition();

    /**
     *
     *@return the total number of players (excluding dealer)
     */
    public int getNumOfPlayers();

    /**
     *
     *@return number of gamblers
     */
    public int getNumOfGamblers();

    /**
     *
     *@return the number of counters
     */
    public int getNumOfCounters();

    /**
     *
     *@return true if tracking gambler, false if tracking counter
     */
    public boolean isTrackingGambler();

    /**
     *
     *@return if gambler is flat betting
     */
    public boolean gamblerFlatBets();

    /**
     *
     *@return gambler's bet (only !=-1 when gambler is flat betting)
     */
    public double getGamblerBet();

    /**
     *
     *@return the bet spread
     */
    public double[][] getBetSpread();

    /**
     *
     *@return the bankroll
     */
    public double getBankroll();

    /**
     *
     *@return the list of players at the table
     */
    public List<IPlayer> getPlayersAtTable();
}
