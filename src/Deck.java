package UNO;
import UNO.Cards.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A Deck is a deck of cards to draw from
 * A new Deck of cards include 108 cards:
 * Four "wild" cards
 * Four "wild draw four" cards
 * For each of the four colors: red, yellow, green, blue:
 * One "0" card
 * Two sets of "1"-"9" cards
 * Two "Skip" cards
 * Two "reverse" cards
 * Two "Draw two" cards
 */
public class Deck
{
    // fields
    private Game game;
    private ArrayList<Card> deck;
    private int deckSize;

    /**
     * Constructor that creates a new deck of shuffled cards
     */
    public Deck(Game game)
    {
        this.game = game;
        deck = new ArrayList<>();
        Card.CardColor[] colorChoices = {Card.CardColor.YELLOW, Card.CardColor.RED, Card.CardColor.BLUE, Card.CardColor.GREEN};
        /**
         * 4 wild cards, 4 wild draw four cards, 4 zero cards one of each color
         * 2 skip cards, 2 reverse cards, 2 draw two cards of each color
         */
        for(int i = 0; i < 4; i++)
        {
            deck.add(new WildCard());
            deck.add(new WildDrawFourCard());
            deck.add(new NumCard(colorChoices[i], 0));
            deck.add(new SkipCard(colorChoices[i]));
            deck.add(new SkipCard(colorChoices[i]));
            deck.add(new ReverseCard(colorChoices[i]));
            deck.add(new ReverseCard(colorChoices[i]));
            deck.add(new DrawTwoCard(colorChoices[i]));
            deck.add(new DrawTwoCard(colorChoices[i]));
        }
        // Two sets of 1-9 cards of each color
        // j: loops through four colors
        // i: loops through 9 numbers from 1 - 9
        for(int j = 0; j < 4; j++)
        {
            for(int i = 1; i < 10; i++)
            {
                deck.add(new NumCard(colorChoices[j], i));
                deck.add(new NumCard(colorChoices[j], i));
            }
        }

        // shuffles cards in the deck
        Collections.shuffle(deck);

        deckSize = deck.size(); // test to see if it's 108
    }

    /**
     * Draws top card from the deck, top is first in list
     * Removes from head
     */
    public Card popTopCard()
    {
        Card topCard = deck.get(0);
        deck.remove(0);
        deckSize--;
        return topCard;
    }

    // Adds one card to deck
    public void addToDeck(Card card)
    {
        deck.add(card);
        deckSize++;
    }

    public void shuffleDeck()
    {
        Collections.shuffle(deck);
    }

    // ------------- Getters -------------------

    public int getDeckSize()
    {
        return deckSize;
    }

    /**
     * @return top card of the deck
     */
    public Card peekTopOfDeck()
    {
        return deck.get(0);
    }

}