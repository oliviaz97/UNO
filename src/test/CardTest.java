package test.UNO;

import UNO.Cards.*;
import UNO.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    private static Game game;
    private static Deck deck;

    @BeforeAll
    public static void setUpBeforeClass()
    {
        game = new Game();
        deck = new Deck(game);
    }

    /**
     * Resets deck to a new deck
     */
    @AfterEach
    public void resetDeck()
    {
        deck = new Deck(game);
    }

    /**
     * Check every card in the deck for color and type of card
     */
    @Test
    public void testGetColorAndNumber() {
        Card card;
        //deck = new Deck(game);
        for(int i = 0; i < 108; i++)
        {
            card = deck.popTopCard();
            // NumCard, Skip, reverse, draw2, could only be one of the four colors
            if(card instanceof NumCard || card instanceof SkipCard || card instanceof ReverseCard || card instanceof DrawTwoCard)
            {
                assert(card.getColor() == Card.CardColor.BLUE || card.getColor() == Card.CardColor.GREEN
                        || card.getColor() == Card.CardColor.RED || card.getColor() == Card.CardColor.YELLOW);
                if(card instanceof NumCard)
                    assert(Arrays.asList(0,1,2,3,4,5,6,7,8,9).contains(((NumCard)card).getNumber()));
            }
            // Wild cards
            else
            {
                assert(card.getColor() == Card.CardColor.ALL);
            }
        }
    }

    @Test
    public void testGetCardStr() {
        ArrayList<Card> cardsToPrint = new ArrayList<>();
        cardsToPrint.add(new NumCard(Card.CardColor.BLUE, 0));
        cardsToPrint.add(new NumCard(Card.CardColor.RED, 1));
        cardsToPrint.add(new NumCard(Card.CardColor.YELLOW, 2));
        cardsToPrint.add(new NumCard(Card.CardColor.GREEN, 3));
        cardsToPrint.add(new ReverseCard(Card.CardColor.BLUE));
        cardsToPrint.add(new SkipCard(Card.CardColor.GREEN));
        cardsToPrint.add(new DrawTwoCard(Card.CardColor.RED));
        cardsToPrint.add(new WildDrawFourCard());
        cardsToPrint.add(new WildCard());

        String[] expected = {
                "BLUE 0",
                "RED 1",
                "YELLOW 2",
                "GREEN 3",
                "BLUE REVERSE",
                "GREEN SKIP",
                "RED DRAW TWO",
                "WILD DRAW FOUR",
                "WILD CARD"
        };

        for(int i = 0; i < 9; i++)
        {
            String str = cardsToPrint.get(i).getCardStr();
            assertEquals(expected[i], str);
        }

    }
}