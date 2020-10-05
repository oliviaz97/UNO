package test.UNO;

import UNO.Cards.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import UNO.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Game game;
    private Player player;
    private ArrayList<Player> players = new ArrayList<>();
    private int numPlayers = 1;
    private Deck deck;
    private ArrayList<Card> hand;
    //private static int numOfCards; // # of cards in the hand, game over once zero is reached
    private String name;
    private boolean skipped; // if player is getting skipped
    private boolean drawTwo;
    private boolean drawFour;
    private final int DRAW_ONE_NUM = 92;
    private final int DRAW_MANY_NUM = 102;
    private static final Card.CardColor YELLOW = Card.CardColor.YELLOW;
    private static final Card.CardColor RED = Card.CardColor.RED;
    private static final Card.CardColor BLUE = Card.CardColor.BLUE;
    private static final Card.CardColor GREEN = Card.CardColor.GREEN;

    @BeforeEach
    public void setUpBeforeClass()
    {
        game = new Game();
        deck = game.getDeck();
        players.add(new Player("player1", game));
        game.getPlayerFromList(numPlayers, players); // add one player to player list
        player = game.getPlayers().get(0);
        player.assignCards();
        hand = player.getHand();
        //numOfCards = player.getNumOfCards();
        name = player.getPlayerName();
        skipped = player.willBeSkipped();
        drawTwo = player.willDrawTwo();
        drawFour = player.willDrawFour();
    }

    @Test
    public void testDrawOnePlayOne() {
        assertEquals(7, hand.size()); // start with 7 cards
        int deckSize = 101;
        // try drawing 10 cards (no replenish)
        for(int i = 0; i < 10; i++)
        {
            player.drawOne();
            deckSize -= 1;
            assertEquals(deckSize,game.getDeck().getDeckSize());
        }
        assertEquals(hand.size(), 17);
        assertEquals(hand.size(), player.getNumOfCards());
        // try overdrawing (need replenishing), draw 92 cards
        for(int i = 0; i < DRAW_ONE_NUM; i++)
        {
            player.drawOne();
            player.playOne(hand.get(1));
        }
        assertEquals(17, player.getNumOfCards());
        assertEquals(89, deck.getDeckSize());
        assertEquals(2, game.getDiscardPile().size());
    }

    /***
     * Start with 7 cards in hand and 101 cards on deck
     * Draw 102 cards to test for replenish
     */
    // keep
    @Test
    public void testDrawMany() {
        player.playOne(player.getHand().get(0));
        player.playOne(player.getHand().get(0)); // discard pile now has two cards
        player.drawMany(DRAW_MANY_NUM); // draw 102 cards to test for replenishing (overdraw by 1)
        assertEquals(1,game.getDiscardPile().size());
        assertEquals(0,game.getDeck().getDeckSize());
        assertEquals(107, player.getNumOfCards());
    }

    @Test
    public void testSwapCards()
    {
        // create p1, give 7 cards
        Player p1 = new Player("player2", game);
        p1.addCard(new NumCard(BLUE, 9));
        p1.addCard(new SkipCard(GREEN));
        p1.addCard(new ReverseCard(RED));
        p1.addCard(new DrawTwoCard(YELLOW));
        p1.addCard(new WildCard());
        p1.addCard(new WildCard());
        p1.addCard(new WildCard());
        assert(p1.getNumOfCards() == 7);

        // give p2 player 5 cards
        Player p2 = new Player("player2", game);
        p2.addCard(new NumCard(BLUE, 9));
        p2.addCard(new SkipCard(GREEN));
        p2.addCard(new ReverseCard(RED));
        p2.addCard(new DrawTwoCard(YELLOW));
        p2.addCard(new WildCard());
        assert(p2.getNumOfCards() == 5);

        p1.swapCards(p2);
        assert(p1.getNumOfCards() == 5);
        assert(p2.getNumOfCards() == 7);
    }

    @Test
    public void testHasValid()
    {
        Game gg = new Game();
        Player p = new Player("p", gg);
        p.addCard(new NumCard(Card.CardColor.RED, 8));
        p.addCard(new NumCard(Card.CardColor.YELLOW, 4));
        p.addCard(new SkipCard(Card.CardColor.GREEN));
        p.addCard(new NumCard(Card.CardColor.RED, 9));
        p.addCard(new DrawTwoCard(Card.CardColor.YELLOW));
        p.addCard(new NumCard(Card.CardColor.YELLOW, 3));

        Card cardToPlay = new WildDrawFourCard();
        p.addCard(cardToPlay);
        p.playOne(cardToPlay);

        gg.setLastColor(Card.CardColor.BLUE);

        assertEquals(false, p.hasValidCard());
    }

    @Test
    public void testIfWildFourLegal()
    {

    }

}