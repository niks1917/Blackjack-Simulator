import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class CounterPlayerTest {

    @Test
    public void makeDecisionsTest1() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{25,25,25,25,-1,-1,-1,-1,-1,-1,-1}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 0, true, false, 25, betSpread,
                1, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        CounterPlayer cp = new CounterPlayer(0, 50000, betSpread, true,
                decisionMaps.get(2), false, true, false);

        int runningCount = 0;
        int trueCount = 20;
        PlayerHand hand = new PlayerHand(25, true);
        hand.addCard(new Card("5"));
        hand.addCard(new Card("5"));
        hand.setHasSplitAces(false);
        hand.setNumOfSplits(0);
        hand.setCanSurrender(false);
        hand.setCanHit(true);
        hand.setCanSplit(true);
        hand.setCanDouble(true);

        DealerHand dealerHand = new DealerHand();
        Card holeCard = new Card("4");
        Card upCard = new Card("A");
        dealerHand.addCard(holeCard);
        dealerHand.addCard(upCard);
        dealerHand.setUpCard(upCard);
        dealerHand.setHoleCard(holeCard);

        String expectedDecision = "D";
        String actualDecision = cp.makeHandDecision(hand, dealerHand, trueCount, runningCount);

        assertEquals(expectedDecision, actualDecision);
    }

    @Test
    public void makeDecisionsTest2() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{25,25,25,25,-1,-1,-1,-1,-1,-1,-1}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 0, true, false, 25, betSpread,
                1, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        CounterPlayer cp = new CounterPlayer(0, 50000, betSpread, true,
                decisionMaps.get(2), false, true, false);

        int runningCount = 0;
        int trueCount = 0;
        PlayerHand hand = new PlayerHand(25, true);
        hand.addCard(new Card("A"));
        hand.addCard(new Card("A"));
        hand.setHasSplitAces(true);
        hand.setNumOfSplits(1);
        hand.setCanSurrender(false);
        hand.setCanHit(true);
        hand.setCanSplit(true);
        hand.setCanDouble(true);

        DealerHand dealerHand = new DealerHand();
        Card holeCard = new Card("4");
        Card upCard = new Card("A");
        dealerHand.addCard(holeCard);
        dealerHand.addCard(upCard);
        dealerHand.setUpCard(upCard);
        dealerHand.setHoleCard(holeCard);

        String expectedDecision = "S";
        String actualDecision = cp.makeHandDecision(hand, dealerHand, trueCount, runningCount);

        assertEquals(expectedDecision, actualDecision);
    }

    @Test
    public void makeDecisionsTest3() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{25,25,25,25,-1,-1,-1,-1,-1,-1,-1}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 0, true, false, 25, betSpread,
                1, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        CounterPlayer cp = new CounterPlayer(0, 50000, betSpread, true,
                decisionMaps.get(1), true, true, false);

        int runningCount = 0;
        int trueCount = 0;
        PlayerHand hand = new PlayerHand(25, true);
        hand.addCard(new Card("T"));
        hand.addCard(new Card("7"));
        hand.setHasSplitAces(false);
        hand.setNumOfSplits(0);
        hand.setCanSurrender(true);
        hand.setCanHit(true);
        hand.setCanSplit(true);
        hand.setCanDouble(true);

        DealerHand dealerHand = new DealerHand();
        Card holeCard = new Card("4");
        Card upCard = new Card("A");
        dealerHand.addCard(holeCard);
        dealerHand.addCard(upCard);
        dealerHand.setUpCard(upCard);
        dealerHand.setHoleCard(holeCard);

        String expectedDecision = "U";
        String actualDecision = cp.makeHandDecision(hand, dealerHand, trueCount, runningCount);

        assertEquals(expectedDecision, actualDecision);
    }

    @Test
    public void makeDecisionsTest4() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{25,25,25,25,-1,-1,-1,-1,-1,-1,-1}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 0, true, false, 25, betSpread,
                1, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        CounterPlayer cp = new CounterPlayer(0, 50000, betSpread, true,
                decisionMaps.get(1), true, true, false);

        int runningCount = -1;
        int trueCount = 0;
        PlayerHand hand = new PlayerHand(25, true);
        hand.addCard(new Card("T"));
        hand.addCard(new Card("6"));
        hand.setHasSplitAces(false);
        hand.setNumOfSplits(0);
        hand.setCanSurrender(true);
        hand.setCanHit(true);
        hand.setCanSplit(true);
        hand.setCanDouble(true);

        DealerHand dealerHand = new DealerHand();
        Card holeCard = new Card("4");
        Card upCard = new Card("T");
        dealerHand.addCard(holeCard);
        dealerHand.addCard(upCard);
        dealerHand.setUpCard(upCard);
        dealerHand.setHoleCard(holeCard);

        String expectedDecision = "U";
        String actualDecision = cp.makeHandDecision(hand, dealerHand, trueCount, runningCount);

        assertEquals(expectedDecision, actualDecision);
    }

    @Test
    public void makeDecisionsTest5() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{25,25,25,25,-1,-1,-1,-1,-1,-1,-1}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 0, true, false, 25, betSpread,
                1, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        CounterPlayer cp = new CounterPlayer(0, 50000, betSpread, true,
                decisionMaps.get(1), true, true, false);

        int runningCount = 5;
        int trueCount = 1;
        PlayerHand hand = new PlayerHand(25, true);
        hand.addCard(new Card("8"));
        hand.addCard(new Card("8"));
        hand.setHasSplitAces(false);
        hand.setNumOfSplits(0);
        hand.setCanSurrender(true);
        hand.setCanHit(true);
        hand.setCanSplit(true);
        hand.setCanDouble(true);

        DealerHand dealerHand = new DealerHand();
        Card holeCard = new Card("4");
        Card upCard = new Card("T");
        dealerHand.addCard(holeCard);
        dealerHand.addCard(upCard);
        dealerHand.setUpCard(upCard);
        dealerHand.setHoleCard(holeCard);

        String expectedDecision = "U";
        String actualDecision = cp.makeHandDecision(hand, dealerHand, trueCount, runningCount);

        assertEquals(expectedDecision, actualDecision);
    }

    @Test
    public void placeBetsTest() {

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

        CounterPlayer cp = new CounterPlayer(0, 50000, betSpread, true,
                decisionMaps.get(2), false, true, false);

        int runningCount = 6;
        int trueCount = 20;

        cp.placeBets(trueCount);

        assertEquals(2, cp.getHandsList().size());
        assertEquals(300, (int)cp.getHand(0).getBet());
        assertEquals(300, (int)cp.getHand(1).getBet());

    }

    @Test
    public void hitTest() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{0,0,0,0,-1,-1,-1,-1,-1,-1,-1}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 0, true, false, 25, betSpread,
                1, 0, 500, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        CounterPlayer cp = new CounterPlayer(0, 500, betSpread, true,
                decisionMaps.get(2), false, true, false);

        int runningCount = 0;
        int trueCount = 3;

        cp.placeBets(trueCount);

        Card card1 = new Card("A");
        Card card2 = new Card("3");

        Card dealtCard1 = new Card("6");
        Card dealtCard2 = new Card("T");

        cp.getHand(0).addCard(card1);
        cp.getHand(0).addCard(card2);

        cp.getHand(1).addCard(card2);
        cp.getHand(1).addCard(card1);

        cp.hit(dealtCard1,0);
        cp.hit(dealtCard2, 1);


        assertEquals("A9", cp.getHand(0).getId());
        assertFalse(cp.getHand(0).canDouble());
        assertFalse(cp.getHand(0).canSurrender());

        assertEquals("14", cp.getHand(1).getId());
        assertFalse(cp.getHand(1).canDouble());
        assertFalse(cp.getHand(1).canSurrender());

        assertEquals(300, (int)cp.getBankroll());
    }

    @Test
    public void doubleTest() {

        String[] chartsToInput = {"CounterH17BSChart.txt", "CounterS17BSChart.txt", "GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt", "H17Dealer.txt", "H17Dealer.txt", "S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1}, {0, 0, 0, 0, 25, 50, 100, 200, 250, 300, 300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 0, true, false, 25, betSpread,
                1, 0, 500, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        CounterPlayer cp = new CounterPlayer(0, 500, betSpread, true,
                decisionMaps.get(2), false, true, false);

        int runningCount = 0;
        int trueCount = 1;

        cp.placeBets(trueCount);

        Card card1 = new Card("A");
        Card card2 = new Card("3");

        Card dealtCard1 = new Card("6");
        Card dealtCard2 = new Card("T");

        cp.getHand(0).addCard(card1);
        cp.getHand(0).addCard(card2);

        cp.getHand(1).addCard(card2);
        cp.getHand(1).addCard(card1);

        cp.doubleDown(dealtCard1, 0);
        cp.doubleDown(dealtCard2, 1);


        assertEquals("A9", cp.getHand(0).getId());
        assertTrue(cp.getHand(0).isWaiting());

        assertEquals("14", cp.getHand(1).getId());
        assertTrue(cp.getHand(1).isWaiting());

        assertEquals(50, (int) cp.getHand(0).getBet());
        assertEquals(50, (int) cp.getHand(1).getBet());

        assertEquals(400, (int) cp.getBankroll());

    }

    @Test
    public void surrenderTest() {

        String[] chartsToInput = {"CounterH17BSChart.txt", "CounterS17BSChart.txt", "GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt", "H17Dealer.txt", "H17Dealer.txt", "S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1}, {0, 0, 0, 0, 25, 50, 100, 200, 250, 300, 300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 0, true, false, 25, betSpread,
                1, 0, 500, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        CounterPlayer cp = new CounterPlayer(0, 500, betSpread, true,
                decisionMaps.get(2), false, true, false);

        int runningCount = 0;
        int trueCount = 2;

        cp.placeBets(trueCount);

        Card card1 = new Card("A");
        Card card2 = new Card("3");

        Card dealtCard1 = new Card("6");
        Card dealtCard2 = new Card("T");

        cp.getHand(0).addCard(card1);
        cp.getHand(0).addCard(card2);

        cp.getHand(1).addCard(card2);
        cp.getHand(1).addCard(card1);

        cp.surrender(0);
        cp.surrender(1);


        assertEquals("A3", cp.getHand(0).getId());
        assertTrue(cp.getHand(0).isOver());

        assertEquals("A3", cp.getHand(1).getId());
        assertTrue(cp.getHand(1).isOver());

        assertEquals(50, (int) cp.getHand(0).getBet());
        assertEquals(50, (int) cp.getHand(1).getBet());

        assertEquals(450, (int) cp.getBankroll());

    }
}