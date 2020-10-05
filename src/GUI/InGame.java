package GUI;
import UNO.*;
import UNO.Cards.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InGame extends GameWindow{

    protected Game game;

    public InGame(Game game)
    {
        super();
        this.game = game;

        displayPlayers();

        padding(2);

        displayLastCard();

        // Display current player's hand to himself/herself if human player
        // If AI, don't display hand
        if(!(game.getCurrPlayer() instanceof AI))
            disPlayHand(game.getCurrPlayer());

        // Apply special effects before playing
        if(game.getCurrPlayer().willBeSkipped() || game.getCurrPlayer().willDrawTwo() || game.getCurrPlayer().willDrawFour())
        {
            //getting skipped
            displayAndApplyEffects();
        }
        else // not getting skipped
        {
            if(!game.getCurrPlayer().hasValidCard())
                promptToDraw();
            else
            {
                if(game.getCurrPlayer() instanceof AI) // AI
                {
                    Card cardToPlay = game.getCurrPlayer().getCardToPlay();
                    playerPlayCard(cardToPlay);
                }
                else // human player
                {
                    JTextArea pickCard = promptToPick();
                    displayPlayButton(pickCard);
                }
            }
        }

        addEndPadding();
    }

    /**
     * Displays all players (1 indexed)
     * Marks current player's turn
     */
    public void displayPlayers()
    {
        for(int i = 0; i < game.getTotalPlayers(); i++)
        {
            JLabel playerLabel;
            // Manually set it to be player1's turn
            if(i == game.getCurrPlayerIndex())
            {
                playerLabel = createLabel((i+1) + ". " + game.getPlayers().get(i).getPlayerName() + "        your turn", Component.LEFT_ALIGNMENT);
            }
            else
                playerLabel = createLabel((i+1) + ". " + game.getPlayers().get(i).getPlayerName(), Component.LEFT_ALIGNMENT);
        }
    }

    /**
     * Displays last card played by the last active user
     */
    public void displayLastCard()
    {
        String color = "";
        String htmlColor = "";
        // display color chosen for wild card if last card is wild card
        if(game.getLastCard() instanceof WildDrawFourCard || game.getLastCard() instanceof WildCard)
        {
            color = colorToString(game.getLastColor());
            htmlColor = colorToHtmlColor(game.getLastColor());
        }
        else
            htmlColor = colorToHtmlColor(game.getLastCard().getColor());
        JLabel last = createLabel("<html>Last card:       <font color=" + htmlColor + ">" + game.getLastCard().getCardStr() + " " + color + "</font></html>", Component.LEFT_ALIGNMENT);
    }

    /**
     * Converts color to string color names
     * @param color
     * @return String name of color
     */
    public String colorToString(Card.CardColor color)
    {
        if(color == Card.CardColor.YELLOW)
            return "YELLOW";
        else if(color == Card.CardColor.RED)
            return "RED";
        else if(color == Card.CardColor.BLUE)
            return "BLUE";
        else if(color == Card.CardColor.GREEN)
            return "GREEN";
        else
            return "";
    }

    /**
     * Converts color to html color names
     * @param color
     * @return html color names
     */
    public String colorToHtmlColor(Card.CardColor color)
    {
        if(color == Card.CardColor.YELLOW)
            return "'orange'";
        else if(color == Card.CardColor.RED)
            return "'red'";
        else if(color == Card.CardColor.BLUE)
            return "'blue'";
        else if(color == Card.CardColor.GREEN)
            return "'green'";
        else
            return "'black'";
    }

    /**
     * Displays current player's hand
     */
    public void disPlayHand(Player player) {
        padding(2);

        // no draw prompt if has valid card to play
        JLabel yourHand = createLabel("Your hand:", Component.LEFT_ALIGNMENT);

        padding(1);
        for (int i = 0; i < player.getNumOfCards(); i++) {
            Card card = player.getHand().get(i);
            String htmlColor = colorToHtmlColor(card.getColor());
            JLabel cardLabel = createLabel("<html>" + (i+1) + ". <font color=" + htmlColor + ">" + card.getCardStr() + "</font></html>", Component.LEFT_ALIGNMENT);
        }
    }

    /**
     * Player is told to draw a card if he/she doesn't have a valid card to play
     */
    public void promptToDraw()
    {
        padding(1);
        JLabel valid = createLabel("You don't have a valid card to play, draw one.", Component.LEFT_ALIGNMENT);

        if(game.getCurrPlayer() instanceof AI)
        {
            drawAndDisplay();
            return;
        }
        // human player
        padding(1);
        JButton drawButton = createButton("Draw card");
        drawButton.addActionListener(new ActionListener() {
            @Override // current player draws a card
            public void actionPerformed(ActionEvent e) {
                drawAndDisplay();
            }
        });
    }

    /**
     * Current player draws one card and displays the card drawn
     */
    public void drawAndDisplay()
    {
        Card cardDrawn = game.getCurrPlayer().drawOne();
        displayCardDrawn(cardDrawn);
        // same player, next state
        hideWindow();
        InGame nextState = new InGame(game);
    }

    public void displayCardDrawn(Card card)
    {
        padding(1);
        String htmlColor = colorToHtmlColor(card.getColor());
        JLabel showCardDrawn = createLabel("<html>Card drawn:       <font color=" + htmlColor + ">" + card.getCardStr() + "</font></html>", Component.LEFT_ALIGNMENT);
    }

    /**
     * Displays last n cards drawn by current player
     * @param n Last n cards drawn
     */
    public void displayLastNCards(int n)
    {
        padding(1);
        int currNumCards = game.getCurrPlayer().getNumOfCards();
        for(int i = n-1; i >= 0; i--)
        {
            Card card = game.getCurrPlayer().getHand().get(currNumCards-1-i);
            String htmlColor = colorToHtmlColor(card.getColor());
            JLabel showCardDrawn = createLabel("<html>Card drawn:       <font color=" + htmlColor + ">" + card.getCardStr() + "</font></html>", Component.LEFT_ALIGNMENT);
        }
    }

    /**
     * Asks user to pick a card to play
     */
    public JTextArea promptToPick()
    {
        padding(1);
        JLabel valid = createLabel("Enter the index of the card you wish to play: ", Component.LEFT_ALIGNMENT);
        padding(1);
        JTextArea pickCard = createTextArea();
        return pickCard;
    }

    /**
     * Displays the play button and handles playing a card, moving on to next player and moving on to end of game
     * Only displays for human players
     * @param pickCard
     */
    public void displayPlayButton(JTextArea pickCard)
    {
        padding(1);
        JButton playButton = createButton("Play card");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.addActionListener(new ActionListener() {
            @Override // plays the card current player just picked
            public void actionPerformed(ActionEvent e) {
                int cardIdx = Integer.parseInt(pickCard.getText()) - 1; // convert to 0 indexed integer index
                Card cardToPlay = game.getCurrPlayer().getHand().get(cardIdx);
                int numCards = game.getCurrPlayer().getNumOfCards();

                // check if card being played is valid
                if(!game.cardIsValid(cardToPlay)) // invalid, display warning message
                {
                    JOptionPane.showMessageDialog(null, "This is not a legal card, pick another card");
                }
                else
                {
                    playerPlayCard(cardToPlay);
                }
            }
        });

    }

    /**
     * Current player plays a card, checks if game ends, if not moves on to next player
     */
    public void playerPlayCard(Card cardToPlay)
    {
        game.getCurrPlayer().playOne(cardToPlay); // play card

        // check if game ends
        if(game.getCurrPlayer().getNumOfCards() == 0) // end game
        {
            game.setWinner(game.getCurrPlayer());
            hideWindow();
            EndOfGame end = new EndOfGame(game);
        } else
        {
            cardEffectTakesPlace(cardToPlay);
        }
    }

    /**
     * Not being skipped
     * Sets special effects for next player, reverses
     * Moves on to next player if number card
     * @param cardToPlay
     */
    public void cardEffectTakesPlace(Card cardToPlay)
    {
        // let current player pick color if a wild or wild draw four is played
        if(cardToPlay instanceof WildDrawFourCard || cardToPlay instanceof WildCard)
        {
            hideWindow();
            ChangeColor changeColor = new ChangeColor(game); // changeColor will handle going to next player
        }
        else if(cardToPlay instanceof ReverseCard) // reverse
        {
            game.reverseDirection();
            moveOnToNextPlayer();
        }
        else if(cardToPlay instanceof NumCard && ((NumCard) cardToPlay).getNumber() == 7) // swap if 7 is played
        {
            swapHands();
        }
        else
        {
            game.updateCurrentPlayer(); // updates current player
            if(cardToPlay instanceof DrawTwoCard) // new current player has to draw two
            {
                game.getCurrPlayer().setDrawTwo(true);
            }
            else if(cardToPlay instanceof SkipCard) // skip next player
            {
                game.getCurrPlayer().setSkipped(true);
            }
            hideWindow();
            InGame nextState = new InGame(game); // moves on to the next game state
        }
    }

    /**
     * Prompts and swaps two players hands
     */
    public void swapHands()
    {
        if(game.getCurrPlayer() instanceof AI) // AI makes random choice to swap hands
        {
            int playerIdx = ((AI)game.getCurrPlayer()).getSwapIndex();
            Player swapPlayer = game.getPlayers().get(playerIdx);
            game.getCurrPlayer().swapCards(swapPlayer); // Done swapping, move on
            moveOnToNextPlayer();
            return;
        }
        // human player
        padding(1);
        JLabel pickSwapPlayer = createLabel("Enter the player index to swap hands with:", Component.LEFT_ALIGNMENT);
        JTextArea swapPlayerText = createTextArea();
        JButton swap = createButton("Swap");
        addEndPadding();
        swap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int playerIdx = Integer.parseInt(swapPlayerText.getText()) - 1; // convert to 0 indexed
                Player swapPlayer = game.getPlayers().get(playerIdx);
                game.getCurrPlayer().swapCards(swapPlayer);
                moveOnToNextPlayer();
            }
        });
    }

    /**
     * Displays GUI for special effects that will cause the player to skip turn
     * Displays "Okay" button to click to move on to next player
     */
    public void displayAndApplyEffects()
    {
        padding(1);

        Player currPlayer = game.getCurrPlayer();
        if(currPlayer.willBeSkipped())
        {
            JLabel skipping = createLabel("You are being skipped!", Component.LEFT_ALIGNMENT);
            game.resetSpecialEffects();
        }
        else if(currPlayer.willDrawTwo())
        {
            JLabel drawTwo = createLabel("You must draw two cards!", Component.LEFT_ALIGNMENT);
            game.applyDrawCardsEffect(2);
            displayLastNCards(2);
        }
        else if(currPlayer.willDrawFour())
        {
            // Handles challenge
            if(game.getCurrPlayer() instanceof AI) // AI
            {
                boolean challenge = ((AI)game.getCurrPlayer()).getChallenge();
                if(challenge)
                {
                    JLabel challengeLabel = createLabel("Challenge", Component.LEFT_ALIGNMENT);
                    addEndPadding();
                    displayChallengeResult();
                }
                else // draws four cards
                {
                    JLabel challengeLabel = createLabel("Draw four cards", Component.LEFT_ALIGNMENT);
                    game.applyDrawCardsEffect(4);
                    moveOnToNextPlayer();
                }
            }
            else
            {
                JLabel drawFour = createLabel("Draw four cards or challenge the last player:", Component.LEFT_ALIGNMENT);
                drawFourOrChallenge();
            }
            return;
        }
        // Button
        padding(1);
        JButton okay = createButton("Okay"); // click to move on to next player
        okay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveOnToNextPlayer();
            }
        });
    }

    public void drawFourOrChallenge()
    {
        padding(1);
        JButton drawFour = createButton("Draw four"); // click to move on to next player
        drawFour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.applyDrawCardsEffect(4);
                displayLastNCards(4); // won't show up
                moveOnToNextPlayer();
            }
        });

        padding(1);
        JButton challenge = createButton("Challenge"); // click to move on to next player
        challenge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayChallengeResult();
            }
        });
    }

    /**
     * Challenges and displays result of challenge
     * Moves on to next player or let current player play when done
     */
    public void displayChallengeResult()
    {
        boolean ifSucceed = game.challenge();
        // challenge succeeds, current player can still play a card
        if(ifSucceed)
        {
            JOptionPane.showMessageDialog(null, "Challenge succeeded, last player draw four cards");
            hideWindow();
            InGame inGame = new InGame(game);
        }
        else // challenge fails, current player draws six cards and misses turn
        {
            JOptionPane.showMessageDialog(null, "Challenge failed, draw six cards");
            moveOnToNextPlayer();
        }
    }

    /**
     * Hides current window and move on to next palyer
     */
    public void moveOnToNextPlayer()
    {
        game.updateCurrentPlayer(); // updates current player
        hideWindow();
        InGame nextState = new InGame(game); // moves on to the next game state
    }

}
