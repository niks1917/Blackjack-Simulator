import java.util.HashMap;

public class Dealer implements IDealer {

    private final boolean isTracked = false;
    private HashMap<String, String> decisionMap;
    private int position;
    private DealerHand hand;

    public Dealer(int position, HashMap<String, String> decisionMap) {
        this.position = position;
        this.decisionMap = decisionMap;
    }

    @Override
    public void initHand() {
        this.hand = new DealerHand();
    }

    @Override
    public void hit(Card dealtCard) {
        this.hand.addCard(dealtCard);
    }

    @Override
    public void stand() {
        this.hand.setIsWaiting(true);
    }

    @Override
    public DealerHand getHand() {
        return this.hand;
    }

    @Override
    public String makeHandDecision(DealerHand dealerHand) {
        return this.decisionMap.get(dealerHand.getId());
    }

    @Override
    public boolean isTracked() {
        return this.isTracked;
    }

    @Override
    public HashMap<String, String> getDecisionMap() {
        return this.decisionMap;
    }
}
