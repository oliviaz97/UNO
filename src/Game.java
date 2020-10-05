package UNO;
import UNO.Cards.*;

import java.util.*;

import static UNO.Cards.Card.CardColor.*;

/**
 * An Uno Game implements the UNO game logic, which includes player turn orders,
 * assigning cards to players, determining the start and end of the game,
 * updating the draw pile, updating cards etc.
 */
public class Game
{
    // fields, all private for security purposes
    private Deck deck; // deck of cards to draw from
    private ArrayList<Player> players;
    private Player currPlayer; // it's currPlayer's turn
    private int currPlayerIndex; // current player's index in the arraylist
    private Player lastPlayer;
    private int totalPlayers; // total number of players
    private Direction direction; // direction of turns, changes when reverse is played
    private ArrayList<Card> discardPile;
    private Scanner userInput; // initialize in constructor
    private ArrayList<Card.CardColor> colorChoices; // all color choices for wild cards
    private boolean gameOver; // indicates end of game
    private Card lastCard; // last card played, same as top of discard pile
    private Card.CardColor lastColor; // the color set by the last wild card player
    private Player winner;

    /**
     * Alternative constructor that takes no input
     */
    public Game()
    {
        deck = new Deck(this);
        players = new ArrayList<>();
        currPlayer = null;
        lastPlayer = null;
        winner = null;
        currPlayerIndex = 0;
        totalPlayers = 0;
        direction = Direction.CLOCKWISE; // clockwise by default
        discardPile = new ArrayList<>();
        userInput = new Scanner(System.in);
        colorChoices = new ArrayList<>(Arrays.asList(YELLOW, RED, BLUE, GREEN));
        gameOver = false;
        lastCard = null;
        lastColor = null;
    }

    /**
     * Gets initial user&game info and sets the start state of the game
     * @param totalPlayers number of players for this game
     * @param players list of players joining this game
     */
    public void startNewGame(int totalPlayers, ArrayList<Player> players)
    {
        this.players = players;
        this.totalPlayers = totalPlayers;
        currPlayerIndex = 0; // reset index to first player in list
        currPlayer = players.get(currPlayerIndex);
        currPlayer.assignCards(); // start assigning from first player
        setInitialState(); // Top of discard pile should now be a number card
    }

    /**
     * Gets initial user&game info and sets the start state of the game
     * @param newPlayerNum number of players for this game
     * @param newPlayers list of players joining this game
     */
    public void startGame(int newPlayerNum, ArrayList<Player> newPlayers)
    {
        getPlayerFromList(newPlayerNum, newPlayers); // sets player name and number from param
        currPlayerIndex = 0; // reset index to first player in list
        currPlayer = players.get(currPlayerIndex);
        currPlayer.assignCards(); // start assigning from first player
        setInitialState(); // Top of discard pile should now be a number card
    }


    /**
     * Set the initial state of the game
     * Reshuffle deck until number card is on top
     * Use current player to move the top card to discard pile to start the game
     */
    public void setInitialState()
    {
        Card topOfDeck = deck.peekTopOfDeck();
        while(!(topOfDeck instanceof NumCard))
        {
            deck.shuffleDeck();
            topOfDeck = deck.peekTopOfDeck();
        }
        currPlayer.drawOne();
        currPlayer.playOne(topOfDeck);
    }



    /**
     * Get players and player number from input
     * For test purposes
     * @param totalPlayers total number of players of the game
     * @param players list of players joining the game
     */
    public void getPlayerFromList(int totalPlayers, ArrayList<Player> players)
    {
        this.totalPlayers = totalPlayers;
        for(int i = 0; i < totalPlayers; i++)
        {
            this.players.add(players.get(i));
        }
    }

    /**
     * Current player makes a move (play, draw or misses turn)
     * If player is missing the turn: Applies and resets special effects
     * If not missing turn:
     * Player has NO valid card to play:
     * Has to draw one card from deck. If this card is valid, play it; if invalid again, keep card and ends turn
     * Player has valid cards to play:
     * 1. gets to choose which card to play, OR
     * 2. choose to draw a card before playing one
     * Checks if game is over and calls endGame to wrap up game
     */
    public void playerMove()
    {
        // Player is being skipped due to special effects
        if(currPlayer.willBeSkipped() || currPlayer.willDrawTwo() || currPlayer.willDrawFour())
        {
            // Apply special effects (reverse/draw 2/draw 4) and ends turn
            skipTurn();
            return;
        }

        // Has NO valid card, has to draw
        if(!currPlayer.hasValidCard())
        {
            Card newCard = currPlayer.drawOne();
            // new card is invalid again (last card of the hand), miss turn
            if(!cardIsValid(newCard))
                return;
            // new card valid, goes on to play the one legal card (let the user pick the legal card)
        }
        // Has valid card, chose to draw
        else if(currPlayer.hasValidCard())
        {
            //System.out.println("Do you wish to draw? (type in the corresponding number) 1 - YES; 2 - NO");
            int wishToDraw = set_wishToDraw();
            if(wishToDraw == 1)
                currPlayer.drawOne();
            // if no wish to draw, move on to let user pick card to play
        }

        pickAndPlayCard();

    }

    /**
     * Player is not missing turn, picks card until valid and plays card, applies card effect
     */
    public void pickAndPlayCard()
    {
        // not missing any turns (no special effect on current player)
        Card cardToPlay = currPlayer.getCardToPlay();

        // keep prompting until player plays a valid card
        while(!cardIsValid(cardToPlay))
        {
            cardToPlay = currPlayer.getCardToPlay();
        }

        // player plays card and card gets added to discard pile
        currPlayer.playOne(cardToPlay);
        // Card does special things, updates lastCard
        cardTakesEffect(cardToPlay);
    }

    /**
     * Player is being skipped, has to do one of the following:
     * Reverse, draw 2, draw 4
     * If player is supposed to draw 4, can challenge
     */
    public void skipTurn() {
        if(currPlayer.willBeSkipped())
        {
            resetSpecialEffects();
        }
        // Forced to draw two cards and misses turn
        if(currPlayer.willDrawTwo())
        {
            int numToDraw = 2;
            applyDrawCardsEffect(numToDraw);
        }
        // Forced to draw four cards and misses turn
        if(currPlayer.willDrawFour())
        {
            // Can challenge
            boolean challenge = getIfChallenge();
            if(challenge)
            {
                challenge();
                pickAndPlayCard();
            }
            else {
                int numToDraw = 4;
                applyDrawCardsEffect(numToDraw);
            }
        }
    }

    /**
     * Randomly chooses to challenge or not
     * @return
     */
    public boolean getIfChallenge()
    {
        int flag = (int)Math.random() * 2;
        if(flag == 0)
            return false;
        else
            return true;
    }

    /**
     * Current player challenges last player
     * RULE: When current player is supposed to draw four, he/she can challenge last active player to show
     * current player his/her cards.
     * If last active player indeed had no card to play - challenge fails - current player draws 6 cards instead of 4
     * If last active player had other cards to play (illegally played wild draw four) - challenge succeeds,
     * Last active player draws 4 cards, current player can play a card
     * @return
     */
    public boolean challenge()
    {
        if(!lastPlayer.ifWildFourLegal()) // illegal, penalize last player
        {
            lastPlayer.drawMany(4);
            resetSpecialEffects();
            return true; // challenge succeeds
        }
        else // legal, penalize current player
        {
            currPlayer.drawMany(6);
            resetSpecialEffects();
            return false; // challenge fails
        }
    }

    /**
     * Makes current player draw either two or four cards and resets player's special flags
     * @param numToDraw whether the player should draw two or draw four cards
     */
    public void applyDrawCardsEffect(int numToDraw) {
        currPlayer.drawMany(numToDraw);
        resetSpecialEffects();
    }

    /**
     * Resets effect from lastCard including
     * Skipped, drawTwo, drawFour
     */
    public void resetSpecialEffects()
    {
        currPlayer.setSkipped(false);
        currPlayer.setDrawTwo(false);
        currPlayer.setDrawFour(false);
    }

    /**
     * Number card: if a 7 card, player gets to swap hands with one of the other players
     * Reverse card: changes direction, effective immediately
     * Skip card: marks next player as skipped
     * Draw two card: marks next player as draw two
     * Wild card: user gets to pick color
     * Wild draw four card: user gets to pick color and marks next player as draw four
     *
     */
    public void cardTakesEffect(Card card)
    {
        lastCard = getLastCard();
        int nextIndex = getNextPlayerIndex();
        Player nextPlayer = players.get(nextIndex);
        if(card instanceof NumCard)
        {
            if(((NumCard) card).getNumber() == 7)
            {
                Player swapPlayer = getSwapPlayer();
                currPlayer.swapCards(swapPlayer);
            }
        }
        else if(card instanceof ReverseCard)
            reverseDirection();
        else if(card instanceof SkipCard)
            nextPlayer.setSkipped(true);
        else if(card instanceof DrawTwoCard)
            nextPlayer.setDrawTwo(true);
        else if(card instanceof WildDrawFourCard)
        {
            nextPlayer.setDrawFour(true);
            lastColor = pickRandomColor();
        }
        else if(card instanceof WildCard)
            lastColor = getColorFromPlayer();
    }


    /**
     * Gets the player to swap cards with currPlayer
     * Randomly generated
     * @return Player to swap cards with
     */
    public Player getSwapPlayer()
    {
        int swapIndex = (int)Math.random() * totalPlayers;
        return players.get(swapIndex);
    }

    /**
     * Determines if card played by the current player is valid or not
     * Current card is valid if it is:
     * 1. A number card that matches the color or number of the last card
     * 2. A +2/reverse/skip card and matches with the color of the last card
     * 3. A wild card or wild +4 card is always valid
     * @currCard: card played by the current player
     * @lastCard: top of discard pile
     * @return: true - if card is valid, in which case move on to next player
     *          false - if card is invalid, in which case current player is prompted to paly again
     */
    public boolean cardIsValid(Card currCard)
    {
        lastCard = getLastCard();
        assert(lastCard != null);
        return cardValidHelper(currCard, lastCard, lastColor);
    }

    /**
     * Compares current card with the second last card played and second last color
     * @return
     */
    public boolean cardIsValidDrawFour(Card card)
    {
        int discardSize = discardPile.size();
        Card secondLastCard = discardPile.get(discardSize - 2); // second last card in the discard pile
        Card.CardColor secondLastColor = secondLastCard.getColor();
        return cardValidHelper(card, secondLastCard, secondLastColor);
    }

    public boolean cardValidHelper(Card currCard, Card last, Card.CardColor lastColor)
    {
        Card.CardColor currColor = currCard.getColor();
        if(currCard instanceof NumCard)
        {
            // last card is number, compare color or number
            if(last instanceof NumCard)
                return currColor == last.getColor()
                        || ((NumCard) currCard).getNumber() == ((NumCard) last).getNumber();
                // last card isn't number, compare only color
            else
                return currColor == last.getColor()
                        || currColor == lastColor;
        }
        // reverse, drawtwo, drawfour, can match with either color or card type
        else if(currCard instanceof DrawTwoCard)
        {
            return currColor == last.getColor() || currColor == lastColor || (last instanceof DrawTwoCard);
        }
        else if(currCard instanceof ReverseCard)
        {
            return currColor == last.getColor() || currColor == lastColor || (last instanceof ReverseCard);
        }
        else if(currCard instanceof SkipCard)
        {
            return currColor == last.getColor() || currColor == lastColor || (last instanceof SkipCard);
        }
        // either types of wild cards, always match
        else
            return true;
    }

    /**
     * Gets and converts player's chosen color for his/her wild card/ wild draw four card
     * into CardColor type
     */
    public Card.CardColor getColorFromPlayer()
    {
        System.out.println("Pick one of the following colors (by typing the corresponding number): 1 - YELLOW; 2 - RED; 3 - BLUE; 4 - GREEN");
        int chosenColorIndex = Integer.parseInt(userInput.nextLine()) - 1;
        return colorChoices.get(chosenColorIndex);
    }

    public Card.CardColor pickRandomColor()
    {
        int colorIndex = (int)Math.random() * 4; // random int from 0, 1, 2, 3
        return colorChoices.get(colorIndex);
    }

    /**
     * Determines which player goes next accounting for reverses, skips
     * Sets currPlayerIndex to the next valid player
     */
    public void updateCurrentPlayer()
    {
        currPlayerIndex = getNextPlayerIndex();
        currPlayer = players.get(currPlayerIndex);
    }

    public void setLastPlayer(Player p)
    {
        lastPlayer = p;
    }

    /**
     * Gets next player in line (or wraps around) based on direction
     * Not considering skipping
     */
    public int getNextPlayerIndex()
    {
        // increment index or wrap around, check upper bound
        if(direction == Direction.CLOCKWISE)
        {
            int newCurrPlayerIndex = currPlayerIndex + 1;
            if(newCurrPlayerIndex == totalPlayers)
                return 0; // wraps around to beginning of list
            else
                return newCurrPlayerIndex;
        }
        // decrement index or wrap around, check lower bound
        else
        {
            int newCurrPlayerIndex = currPlayerIndex - 1;
            if(newCurrPlayerIndex < 0)
                return totalPlayers - 1; // wraps around to end of list
            else
                return newCurrPlayerIndex;
        }
    }

    /**
     * Adds card being played to the discard pile
     */
    public void addToDiscardPile(Card currCard)
    {
        discardPile.add(currCard);
    }

    /**
     * @return: top card of deck
     */
    public Card drawFromDeck()
    {
        return deck.popTopCard();
    }

    // Driver methods
    public int set_wishToDraw()
    {
        return (int)Math.random()*2 + 1;
    }

    /**
     * Reverses turn direction
     */
    public void reverseDirection()
    {
        if(direction == Direction.CLOCKWISE)
            direction = Direction.COUNTER_CLOCKWISE;
        else
            direction = Direction.CLOCKWISE;
    }

    /**
     * Checks if deck is empty before replenishing
     * Deck is empty, move all cards EXCEPT for the last discarded card from discard pile to deck and shuffle
     * Be careful about when to replenish, make sure game.lastCard is updated
     */
    public void replenish()
    {
        if(deck.getDeckSize() > 0) return;
        // need replenishing
        Card topCard = discardPile.get(discardPile.size() - 1); // save top of discard pile
        // move all the cards in the discard pile BESIDES the top card to the deck
        Card card;
        for(int i = 0; i < discardPile.size() - 1; i++)
        {
            card = discardPile.get(i);
            deck.addToDeck(card);
        }
        // clear discardPile
        discardPile.clear();
        discardPile.add(topCard); // top of old discard pile remains in the discard pile
        deck.shuffleDeck();
    }

    // ------------------ Get functions ----------------------
    public ArrayList<Card> getDiscardPile()
    {
        return discardPile;
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public int getTotalPlayers()
    {
        return totalPlayers;
    }

    public Deck getDeck()
    {
        return deck;
    }

    public Card.CardColor getLastColor()
    {
        return lastColor;
    }

    public Player getCurrPlayer()
    {
        return currPlayer;
    }

    public int getCurrPlayerIndex()
    {
        return currPlayerIndex;
    }

    /**
     * Gets the card at the top of the discard pile
     */
    public Card getLastCard()
    {
        int size = discardPile.size();
        return discardPile.get(size - 1);
    }

    public Player getWinner()
    {
        return winner;
    }

    // ------------------ Set functions ----------------------

    public void setLastCard(Card card)
    {
        lastCard = card;
    }

    public void setLastColor(Card.CardColor color)
    {
        lastColor = color;
    }

    public void setCurrPlayer(Player p)
    {
        currPlayer = p;
    }

    /**
     * Sets winner of the game
     * @param player
     */
    public void setWinner(Player player)
    {
        winner = player;
    }

}