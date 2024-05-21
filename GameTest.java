import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void runShoeTotalGainTest() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{0,0,0,0,-1,-1,-1,-1,-1,-1,-1}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 0, true, false, 25, betSpread,
                1, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        String[] rulesArr = sim.getRulesArr();

        Game game = new Game(6, 5, 1, true, false,
                25, betSpread, 1, 0, 50000, rulesArr, decisionMaps, countSystemFile, .5);

        IPlayer counterTracked = game.getPlayersAtTable().get(0);

        double startingBankroll = counterTracked.getBankroll();

        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();

        double endingBankroll = counterTracked.getBankroll();

        double expectedGain = endingBankroll - startingBankroll;

        assertEquals(expectedGain, game.getTotalGain(), .0);
    }

    @Test
    public void runShoeGettersTest() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{0,0,0,0,-1,-1,-1,-1,-1,-1,-1}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 1, true, false, 25, betSpread,
                1, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        String[] rulesArr = sim.getRulesArr();

        Game game = new Game(6, 5, 1, true, false,
                25, betSpread, 1, 0, 50000, rulesArr, decisionMaps, countSystemFile, .5);

        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();
        game.playShoe();

        assertEquals(2, game.getPlayersAtTable().size());
        assertEquals(6, game.getNumDecks());
        assertEquals(1, game.getNumOfCounters());
        assertEquals(1, game.getNumOfGamblers());
        assertEquals(0, game.getTrackedPlayerPosition());
        assertTrue(game.getPlayersAtTable().get(0).isTracked());
        assertFalse(game.getPlayersAtTable().get(1).isTracked());
        assertEquals(5, (int)game.getPenetration());
        assertFalse(game.getRSA());
        assertFalse(game.getH17());
        assertFalse(game.isGamblerCrazy());
        assertTrue(game.getSurrender());
        assertEquals(50000, (int)game.getBankroll());
    }

    @Test
    public void splitTest() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{0,0,0,0,-1,-1,-1,-1,-1,-1,-1}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 1, true, false, 25, betSpread,
                1, 0, 500, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        String[] rulesArr = sim.getRulesArr();

        Game game = new Game(6, 5, 1, true, false,
                25, betSpread, 1, 0, 500, rulesArr, decisionMaps, countSystemFile, .5);

        Card hand1Card1 = new Card("6");
        Card hand1Card2 = new Card("6");

        Card hand2Card1 = new Card(("A"));
        Card hand2Card2 = new Card("A");

        PlayerHand hand1 = new PlayerHand(25, true);
        hand1.addCard(hand1Card1);
        hand1.addCard(hand1Card2);
        PlayerHand hand2 = new PlayerHand(25, true);
        hand2.addCard(hand2Card1);
        hand2.addCard(hand2Card2);


        game.getPlayersAtTable().get(0).placeBets(1);

        assertEquals(2, game.getPlayersAtTable().get(0).getHandsList().size());

        game.getPlayersAtTable().get(0).getHandsList().set(0, hand1);
        game.getPlayersAtTable().get(0).getHandsList().set(1, hand2);

        //split 1
        game.split(0, game.getPlayersAtTable().get(0).getHandsList(), 0);

        assertEquals(3, game.getPlayersAtTable().get(0).getHandsList().size());

        assertEquals(425, (int) game.getPlayersAtTable().get(0).getBankroll());

        assertEquals("AA", game.getPlayersAtTable().get(0).getHand(2).getId());
        assertEquals(0, game.getPlayersAtTable().get(0).getHand(2).getNumOfSplits());


        assertEquals(1, game.getPlayersAtTable().get(0).getHand(1).getCardsList().size());
        assertEquals("6", game.getPlayersAtTable().get(0).getHand(1).getCardsList().get(0).getID());
        assertEquals(1, game.getPlayersAtTable().get(0).getHand(1).getNumOfSplits());
        assertFalse(game.getPlayersAtTable().get(0).getHand(1).canSurrender());

        assertEquals(1, game.getPlayersAtTable().get(0).getHand(0).getCardsList().size());
        assertEquals("6", game.getPlayersAtTable().get(0).getHand(0).getCardsList().get(0).getID());
        assertEquals(1, game.getPlayersAtTable().get(0).getHand(0).getNumOfSplits());
        assertFalse(game.getPlayersAtTable().get(0).getHand(1).canSurrender());

        //split 2
        game.getPlayersAtTable().get(0).getHand(0).addCard(new Card("6"));

        game.split(0, game.getPlayersAtTable().get(0).getHandsList(), 0);

        assertEquals(4, game.getPlayersAtTable().get(0).getHandsList().size());

        assertEquals(400, (int) game.getPlayersAtTable().get(0).getBankroll());

        assertEquals("AA", game.getPlayersAtTable().get(0).getHand(3).getId());
        assertEquals(0, game.getPlayersAtTable().get(0).getHand(3).getNumOfSplits());

        assertEquals(1, game.getPlayersAtTable().get(0).getHand(2).getCardsList().size());
        assertEquals("6", game.getPlayersAtTable().get(0).getHand(2).getCardsList().get(0).getID());
        assertEquals(2, game.getPlayersAtTable().get(0).getHand(2).getNumOfSplits());
        assertFalse(game.getPlayersAtTable().get(0).getHand(2).canSurrender());

        assertEquals(1, game.getPlayersAtTable().get(0).getHand(1).getCardsList().size());
        assertEquals("6", game.getPlayersAtTable().get(0).getHand(1).getCardsList().get(0).getID());
        assertEquals(2, game.getPlayersAtTable().get(0).getHand(1).getNumOfSplits());
        assertFalse(game.getPlayersAtTable().get(0).getHand(1).canSurrender());

        assertEquals(1, game.getPlayersAtTable().get(0).getHand(0).getCardsList().size());
        assertEquals("6", game.getPlayersAtTable().get(0).getHand(0).getCardsList().get(0).getID());
        assertEquals(2, game.getPlayersAtTable().get(0).getHand(0).getNumOfSplits());
        assertFalse(game.getPlayersAtTable().get(0).getHand(0).canSurrender());

    }


}