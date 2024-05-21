import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class GamblerPlayerTest {

    @Test
    public void makeDecisionsTest1() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{25,25,25,25,-1,-1,-1,-1,-1,-1,-1}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 1, true, true, 25, betSpread,
                0, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        GamblerPlayer gp = new GamblerPlayer(0, 50000, betSpread, true, decisionMaps.get(3), true, false);

        int runningCount = 0;
        int trueCount = 0;
        PlayerHand hand = new PlayerHand(25, true);
        hand.addCard(new Card("A"));
        hand.addCard(new Card("7"));
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

        String expectedDecision = "H";
        String actualDecision = gp.makeHandDecision(hand, dealerHand, trueCount, runningCount);

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
                5, 1, true, true, 25, betSpread,
                0, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        GamblerPlayer gp = new GamblerPlayer(0, 50000, betSpread, true, decisionMaps.get(3), true, false);

        int runningCount = 0;
        int trueCount = 0;
        PlayerHand hand = new PlayerHand(25, true);
        hand.addCard(new Card("A"));
        hand.addCard(new Card("7"));
        hand.setHasSplitAces(false);
        hand.setNumOfSplits(0);
        hand.setCanSurrender(true);
        hand.setCanHit(true);
        hand.setCanSplit(true);
        hand.setCanDouble(true);

        DealerHand dealerHand = new DealerHand();
        Card holeCard = new Card("4");
        Card upCard = new Card("6");
        dealerHand.addCard(holeCard);
        dealerHand.addCard(upCard);
        dealerHand.setUpCard(upCard);
        dealerHand.setHoleCard(holeCard);

        String expectedDecision = "D";
        String actualDecision = gp.makeHandDecision(hand, dealerHand, trueCount, runningCount);

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
                5, 1, true, true, 25, betSpread,
                0, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        GamblerPlayer gp = new GamblerPlayer(0, 50000, betSpread, true, decisionMaps.get(3), true, false);

        int runningCount = 0;
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
        String actualDecision = gp.makeHandDecision(hand, dealerHand, trueCount, runningCount);

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
                5, 1, true, true, 25, betSpread,
                0, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        GamblerPlayer gp = new GamblerPlayer(0, 50000, betSpread, true, decisionMaps.get(3), true, false);

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
        Card upCard = new Card("T");
        dealerHand.addCard(holeCard);
        dealerHand.addCard(upCard);
        dealerHand.setUpCard(upCard);
        dealerHand.setHoleCard(holeCard);

        String expectedDecision = "S";
        String actualDecision = gp.makeHandDecision(hand, dealerHand, trueCount, runningCount);

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
                5, 1, true, true, 25, betSpread,
                0, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        GamblerPlayer gp = new GamblerPlayer(0, 50000, betSpread, true, decisionMaps.get(3), true, true);

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
        Card upCard = new Card("T");
        dealerHand.addCard(holeCard);
        dealerHand.addCard(upCard);
        dealerHand.setUpCard(upCard);
        dealerHand.setHoleCard(holeCard);

        String expectedDecision = "P";
        String actualDecision = gp.makeHandDecision(hand, dealerHand, trueCount, runningCount);

        assertEquals(expectedDecision, actualDecision);
    }

    @Test
    public void makeDecisionsTest6() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{25,25,25,25,-1,-1,-1,-1,-1,-1,-1}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 1, true, true, 25, betSpread,
                0, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        GamblerPlayer gp = new GamblerPlayer(0, 50000, betSpread, true, decisionMaps.get(3), true, true);

        int runningCount = 0;
        int trueCount = 0;
        PlayerHand hand = new PlayerHand(25, true);
        hand.addCard(new Card("6"));
        hand.addCard(new Card("A"));
        hand.setHasSplitAces(false);
        hand.setNumOfSplits(1);
        hand.setCanSurrender(false);
        hand.setCanHit(true);
        hand.setCanSplit(true);
        hand.setCanDouble(true);

        DealerHand dealerHand = new DealerHand();
        Card holeCard = new Card("4");
        Card upCard = new Card("4");
        dealerHand.addCard(holeCard);
        dealerHand.addCard(upCard);
        dealerHand.setUpCard(upCard);
        dealerHand.setHoleCard(holeCard);

        String expectedDecision = "D";
        String actualDecision = gp.makeHandDecision(hand, dealerHand, trueCount, runningCount);

        assertEquals(expectedDecision, actualDecision);
    }

    @Test
    public void makeDecisionsTest7() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{25,25,25,25,-1,-1,-1,-1,-1,-1,-1}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 1, true, true, 25, betSpread,
                0, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        GamblerPlayer gp = new GamblerPlayer(0, 50000, betSpread, true, decisionMaps.get(3), true, true);

        int runningCount = 0;
        int trueCount = 0;
        PlayerHand hand = new PlayerHand(25, true);
        hand.addCard(new Card("6"));
        hand.addCard(new Card("A"));
        hand.setHasSplitAces(false);
        hand.setNumOfSplits(1);
        hand.setCanSurrender(false);
        hand.setCanHit(true);
        hand.setCanSplit(true);
        hand.setCanDouble(false);

        DealerHand dealerHand = new DealerHand();
        Card holeCard = new Card("4");
        Card upCard = new Card("4");
        dealerHand.addCard(holeCard);
        dealerHand.addCard(upCard);
        dealerHand.setUpCard(upCard);
        dealerHand.setHoleCard(holeCard);

        String expectedDecision = "H";
        String actualDecision = gp.makeHandDecision(hand, dealerHand, trueCount, runningCount);

        assertEquals(expectedDecision, actualDecision);
    }

    @Test
    public void placeBetsTest() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{25,25,25,25,25,25,25,25,25,25,25}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 1, true, true, 25, betSpread,
                0, 0, 50000, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        GamblerPlayer gp = new GamblerPlayer(0, 50000, betSpread, true, decisionMaps.get(3), true, false);

        int runningCount = 0;
        int trueCount = 20;

        gp.placeBets(trueCount);

        assertEquals(1, gp.getHandsList().size());
        assertEquals(25, (int)gp.getHand(0).getBet());

    }

    @Test
    public void hitTest() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{25,25,25,25,25,25,25,25,25,25,25}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 1, true, true, 25, betSpread,
                0, 0, 500, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        GamblerPlayer gp = new GamblerPlayer(0, 500, betSpread, true, decisionMaps.get(3), true, false);

        int runningCount = 0;
        int trueCount = 0;

        gp.placeBets(trueCount);

        Card card1 = new Card("A");
        Card card2 = new Card("3");

        Card dealtCard1 = new Card("6");
        Card dealtCard2 = new Card("T");

        gp.getHand(0).addCard(card1);
        gp.getHand(0).addCard(card2);

        gp.hit(dealtCard1,0);


        assertEquals("A9", gp.getHand(0).getId());
        assertFalse(gp.getHand(0).canDouble());
        assertFalse(gp.getHand(0).canSurrender());

        assertEquals(475, (int) gp.getBankroll());

    }


    @Test
    public void doubleTest() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{25,25,25,25,25,25,25,25,25,25,25}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 1, true, true, 25, betSpread,
                0, 0, 500, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        GamblerPlayer gp = new GamblerPlayer(0, 500, betSpread, true, decisionMaps.get(3), true, false);

        int runningCount = 0;
        int trueCount = 0;

        gp.placeBets(trueCount);

        Card card1 = new Card("A");
        Card card2 = new Card("3");

        Card dealtCard1 = new Card("6");

        gp.getHand(0).addCard(card1);
        gp.getHand(0).addCard(card2);

        gp.doubleDown(dealtCard1, 0);


        assertEquals("A9", gp.getHand(0).getId());
        assertTrue(gp.getHand(0).isWaiting());


        assertEquals(50, (int) gp.getHand(0).getBet());

        assertEquals(450, (int) gp.getBankroll());
    }

    @Test
    public void surrenderTest() {

        String[] chartsToInput = {"CounterH17BSChart.txt","CounterS17BSChart.txt","GamblerNormBSChart.txt",
                "GamblerCrazyBSChart.txt","H17Dealer.txt","H17Dealer.txt","S17Dealer.txt"};

        String ruleFile = "rulesMap.txt";

        String countSystemFile = "countSystemHiLo.txt";

        SimStorageManager ssm = new SimStorageManager();

        double[][] betSpread = {{25,25,25,50,25,25,25,25,25,25,25}, {0,0,0,0,25,50,100,200,250,300,300}};

        Simulation sim = new Simulation(chartsToInput, 50, ssm, ruleFile, 6,
                5, 1, false, true, -1, betSpread,
                0, 0, 500, 9, countSystemFile, .5);

        HashMap<Integer, HashMap<String, String>> decisionMaps = sim.getDecisionMaps();

        GamblerPlayer gp = new GamblerPlayer(0, 500, betSpread, true, decisionMaps.get(3), true, false);

        int runningCount = 0;
        int trueCount = 0;

        gp.placeBets(trueCount);

        Card card1 = new Card("A");
        Card card2 = new Card("3");

        gp.getHand(0).addCard(card1);
        gp.getHand(0).addCard(card2);

        gp.surrender(0);

        assertEquals("A3", gp.getHand(0).getId());
        assertTrue(gp.getHand(0).isOver());

        assertEquals(50, (int) gp.getHand(0).getBet());

        assertEquals(475, (int) gp.getBankroll());
    }

}