import java.util.*;
public interface IPlayerHand extends IHand {
    /**
     *
     * @return value of canSurrender
     */
    public boolean canSurrender();

    /**
     * set whether this hand can surrender
     * @param canSurrender whether the hand can surrender
     */
    public void setCanSurrender(boolean canSurrender);

    /**
     *
     * @return value of canDouble
     */
    public boolean canDouble();

    /**
     * set whether this hand can double
     * @param canDouble whether the hand can double
     */
    public void setCanDouble(boolean canDouble);

    /**
     *
     * @return value of canSplit
     */
    public boolean canSplit();

    /**
     * set whether this hand can split
     * @param canSplit whether the hand can split
     */
    public void setCanSplit(boolean canSplit);

    /**
     *
     * @return value of bet
     */
    public double getBet();

    /**
     * set value of bet such as if a double takes place
     * @param bet the total value of the bet on the hand
     */
    public void setBet(double bet);

    /**
     *
     * @return num of splits
     */
    public int getNumOfSplits();

    /**
     * set number of splits
     * @param numOfSplits the total value of the bet on the hand
     */
    public void setNumOfSplits(int numOfSplits);

    /**
     *
     * @return if the hand can hit
     */
    public boolean canHit();

    /**
     * set number of splits
     * @param canHit the total value of the bet on the hand
     */
    public void setCanHit(boolean canHit);

    /**
     *
     * @return if the player took insurance
     */
    public boolean tookInsurance();

    /**
     * @param tookInsurance set if the player took insurance
     */
    public void setTookInsurance(boolean tookInsurance);

    /**
     * @return true if hand has split aces
     */
    public boolean hasSplitAces();

    /**
     * @param hasSplitAces if the hand split aces
     */
    public void setHasSplitAces(boolean hasSplitAces);
}
