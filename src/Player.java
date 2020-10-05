package UNO;

import java.util.ArrayList;
import UNO.Cards.Card;
import UNO.Cards.WildDrawFourCard;

/***
 * A Player has a hand of cards
 * When it's the player's turn, he/she can either"
 * 1. Play a card that matches with either the color or number of the top
 *    card of the discard pile
 * 2. Take cards from the deck until he/she has a valid card to play
 * 3. Gets skipped
 * 4. Plays an invalid card, then the card will get rejected and he/she
 *    will be prompted again
 */
public class Player
{
    // fields
    protected ArrayList<Card> hand;
    protected int numOfCards; // # of cards in the hand, game over once zero is reached
    protected String name;
    protected Game game; // current game the player is in
    protected boolean skipped; // if player is getting skipped
    protected boolean drawTwo;
    protected boolean drawFour;

    // constructor for Player, start with 7 cards
    public Player(String name, Game game)
    {
        hand = new ArrayList<Card>();
        numOfCards = 0;
        this.name = name;
        this.game = game;
        skipped = false; // default
        drawTwo = false;
        drawFour = false;
    }

    /**
     * Assign each player 7 cards at the beginning of game
     * Deck is full, no need to worry about running out of cards
     * i: loops through 7 cards
     * currPlayerIndex: loops through all players
     */
    public void assignCards()
    {
        Player currPlayer = this;
        for(int i = 0; i < 7; i++)
        {
            for(int currPlayerIndex = 0; currPlayerIndex < game.getTotalPlayers(); currPlayerIndex++)
            {
                currPlayer = game.getPlayers().get(currPlayerIndex);
                currPlayer.getHand().add(game.getDeck().popTopCard()); // takes one card from top of deck
                currPlayer.numOfCards++;
            }
        }
    }

    /***
     * Try to replenish before drawing one card from deck
     * Card drawn moves from deck to player's hand
     * TODO: need ifDeckEmpty check after and replenish if needed
     * @return: Card drawn
     */
    public Card drawOne()
    {
        game.replenish();
        Card card = game.drawFromDeck(); // remove top card from deck
        hand.add(card);
        numOfCards++;
        return card;
    }

    /***
     * Draw a certain number of cards in a row
     * @num: number of cards to be drawn from deck in a row
     */
    public void drawMany(int num)
    {
        for(int i = 0; i < num; i++)
        {
            drawOne();
        }
    }

    /*** Player plays one card from hand
     * Card played moves from player's hand to the discard pile
     * @card: card being played
     * TODO: need validity check before call
     * TODO: need ifHandEmpty check after call
     * TODO: remove argument
     */
    public void playOne(Card card)
    {
        hand.remove(card);
        numOfCards--;
        game.addToDiscardPile(card);
        game.setLastPlayer(this);
        game.setLastColor(card.getColor());
    }

    /***
     * Checks if player has a valid card to play
     * @return: true - player has valid card to play
     *          false - player doesn't have valid card to play and has to draw after the call
     */
    public boolean hasValidCard()
    {
        for (Card card : hand)
        {
            if (game.cardIsValid(card))
                return true;
        }
        return false;
    }

    /**
     * If the player has valid card other than the wild draw four, it's legal
     * @return If it's legal to play wild draw four
     */
    public boolean ifWildFourLegal()
    {
        for (Card card : hand)
        {
            boolean b1 = game.cardIsValidDrawFour(card);
            boolean b2 = !(card instanceof WildDrawFourCard);
            if(b1 && b2)
                return false;
        }
        return true; // no valid card to play besides wild draw four
    }

    /**
     * Picks a valid card for the player to play
     * Should never be null, since we only get card to play when play has valid card
     * @return first valid card the player can play
     */
    public Card getCardToPlay()
    {
        Card cardToPlay = null;
        for(int i = 0; i < numOfCards; i++)
        {
            cardToPlay = hand.get(i);
            if(game.cardIsValid(cardToPlay))
                return cardToPlay;
        }
        assert(cardToPlay != null);
        return null;
    }

    /***********Set and get special effect flags***************/

    /***
     * Checks if this player is being skipped
     * @return: true - player is being skipped
     *          false - player is NOT being skipped
     */
    public boolean willBeSkipped()
    {
        return skipped;
    }


    public void setSkipped(boolean val)
    {
        skipped = val;
    }

    /***
     * @return: if player needs to draw two cards
     */
    public boolean willDrawTwo()
    {
        return drawTwo;
    }

    /***
     * Player needs to draw two next round
     */
    public void setDrawTwo(boolean val)
    {
        drawTwo = val;
    }

    /***
     * @return: if player needs to draw four cards next round
     */
    public boolean willDrawFour()
    {
        return drawFour;
    }

    /***
     * Player needs to draw four next round
     */
    public void setDrawFour(boolean val)
    {
        drawFour = val;
    }

    /***
     * User says "UNO" when playing out second to last card
     */
    public void sayUno()
    {
        System.out.println("UNO!");
    }

    /**
     * Manually give this player a new card
     */
    public void addCard(Card card)
    {
        this.hand.add(card);
        numOfCards++;
    }

    /********************* Get functions ***********************/

    public String getPlayerName()
    {
        return name;
    }

    public int getNumOfCards()
    {
        return numOfCards;
    }

    public ArrayList<Card> getHand()
    {
        return hand;
    }

    public Game getGame()
    {
        return game;
    }

    /********************Display methods********************

    /***
     * Shows this player's cards to himself/herself
     * Every card is displayed on a new line
     */
    public String getHandStr()
    {
        String handStr = "";
        for(int i = 0; i < numOfCards; i++)
        {
            handStr = handStr + hand.get(i).getCardStr() + "\n";
        }
        return handStr;
    }

    /**
     * Rule: If current player plays a 0 card, then he/she have to swap with one of the other player's hands
     * Swap two players' hands
     * @param other Player to swap cards with
     */
    public void swapCards(Player other)
    {
        int num1 = this.getNumOfCards();
        int num2 = other.getNumOfCards();
        int minNum = Math.min(num1, num2);

        for(int i = 0; i < minNum; i++)
        {
            // keep removing cards from front of one player's hand and add to end of the other player's hand
            this.hand.add(other.hand.remove(0));
            other.hand.add(this.hand.remove(0));
        }

        int leftOverNum1 = this.getNumOfCards() - minNum;
        int leftOverNum2 = other.getNumOfCards() - minNum;

        // move left over cards
        if( leftOverNum1 > 0) // this originally had more cards
        {
            for(int i = 0; i < leftOverNum1; i++)
            {
                other.hand.add(this.hand.remove(0));
                other.numOfCards++;
                this.numOfCards--;
            }
        }
        else if( leftOverNum2 > 0) // other originally had more cards
        {
            for(int i = 0; i < leftOverNum2; i++)
            {
                this.hand.add(other.hand.remove(0));
                this.numOfCards++;
                other.numOfCards--;
            }
        }
    }


}