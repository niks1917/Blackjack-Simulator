public class Card implements ICard {

    private String id;
    private String value;

    //is this the dealer's hole card
    private boolean isHoleCard = false;

    //Constructor
    public Card(String id) {
        this.id = id;

        if (!id.equals("T") && !id.equals("J") && !id.equals("Q") &&
                !id.equals("K") && !id.equals("A")) {
            this.value = id;
        } else if (id.equals("A")) {
            this.value = "11";
        } else {
            this.value = "10";
            this.id = "T";
        }
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = Integer.toString(value);
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public boolean isHoleCard() {
        return isHoleCard;
    }

    @Override
    public void setHoleCard(boolean isHoleCard) {
        this.isHoleCard = isHoleCard;
    }
}