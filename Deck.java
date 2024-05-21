import java.util.*;

public class Deck implements IDeck {
    //deck of cards
    private Card[] deck;
    private int numOfDecks;

    //used to deal incrementally and to
    //calculate when deck needs to be shuffled
    private int dealtCounter;

    //constructor for making initial deck in game
    public Deck(int numOfDecks) {

        this.dealtCounter = 0;

        this.numOfDecks = numOfDecks;
        this.deck = new Card[this.numOfDecks * 52];

        String[] id = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};

        int currentIndex = 0;
        for (int i = 0; i < numOfDecks; i++) {
            for (int j = 0; j < 4; j++) {
                for (String s : id) {
                    this.deck[currentIndex] = new Card(s);
                    currentIndex++;
                }
            }

        }

        shuffleDeck();
    }

    //Constructor for reshuffling (faster than initial constructor)
    //Takes in discards from Game class instead of repopulating an array
    public Deck(Card[] discards) {

        this.deck = discards;

        this.dealtCounter = 0;

        this.numOfDecks = discards.length / 52;

        shuffleDeck();
    }

    @Override
    public Card dealCard() {

        Card dealtCard = this.deck[dealtCounter];
        this.dealtCounter++;

        return dealtCard;
    }

    //use algorithm at link: https://www.javatpoint.com/shuffle-an-array-in-java
    //for shuffling an array
    @Override
    public void shuffleDeck() {
        Random rand = new Random();
        for (int i = deck.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Card temp = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
        }
    }

    @Override
    public int getNumOfDecks() {

        return this.numOfDecks;
    }
}
