package test.UNO;

import static org.junit.jupiter.api.Assertions.*;

import UNO.Cards.Card;
import UNO.Cards.WildCard;
import org.junit.*;
import UNO.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

public class DeckTest {
    private static Game game;
    private static Deck deck;

    @BeforeClass
    public static void setUpBeforeClass()
    {
        game = new Game();
        deck = new Deck(game);
    }

    /**
     * Resets deck to a new deck
     */
    @After
    public void resetDeck()
    {
        deck = new Deck(game);
    }

    @Test
    public void testDeckConstructor()
    {
        Deck new_deck = new Deck(game);
        assertEquals(new_deck.getDeckSize(), 108);
    }

    /**
     * Try popping off entire deck and check if number of cards is correct
     */
    @Test
    public void testPopTopCard() {
        for(int i = 0; i < 108; i++)
        {
            deck.popTopCard();
        }
        assertEquals(deck.getDeckSize(), 0);
    }

    /**
     * Try adding 50 cards to a full deck
     */
    @Test
    public void testAddToDeck() {
        for(int i = 0; i < 50; i++)
        {
            deck.addToDeck(new WildCard());
        }
        assertEquals(deck.getDeckSize(), 158);
    }
}