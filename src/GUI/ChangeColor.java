package GUI;

import UNO.AI;
import UNO.Cards.Card;
import UNO.Cards.WildDrawFourCard;
import UNO.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A ChangeColor window is displayed when the player played a wild card or wild draw four card
 * The player will then be prompted to pick a color for his/her wild card
 * TODO: Change text entry to buttons
 */
public class ChangeColor extends GameWindow{
    Game game;
    Card.CardColor[] colorChoices = {Card.CardColor.YELLOW, Card.CardColor.RED, Card.CardColor.BLUE, Card.CardColor.GREEN};

    public ChangeColor(Game game)
    {
        super();
        this.game = game;
        if(game.getCurrPlayer() instanceof AI)
            aiPicksColor();
        else
            promptToPickColor();
    }

    /**
     * Prompts and lets the human user to pick a color for his/her wild/wild drawFour card
     */
    public void promptToPickColor()
    {
        JLabel colorList = createLabel("<html><font color='orange'>1 - YELLOW</font>  <font color='red'>2 - RED</font>  <font color='blue'>3 - BLUE</font>  <font color='green'>4 - GREEN</font></html>", Component.CENTER_ALIGNMENT);
        padding(1);
        JLabel pickColor = createLabel("Enter the index of the color you wish to choose: ", Component.CENTER_ALIGNMENT);
        padding(1);
        JTextArea color = createTextArea();
        padding(1);

        JButton chooseColor = createButton("Choose color");
        chooseColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int colorIdx = Integer.parseInt(color.getText()) - 1; // convert to 0 indexed
                Card.CardColor colorChosen = colorChoices[colorIdx];
                game.setLastColor(colorChosen);
                game.updateCurrentPlayer(); // updates current player
                // handle wild draw four
                if(game.getLastCard() instanceof WildDrawFourCard)
                    game.getCurrPlayer().setDrawFour(true);
                hideWindow();
                InGame nextState = new InGame(game); // moves on to the next game state
            }
        });
        addEndPadding();
    }

    /**
     * AI picks color and sets drawFour if needed
     */
    public void aiPicksColor()
    {
        Card.CardColor colorChosen = ((AI)game.getCurrPlayer()).pickColor();
        game.setLastColor(colorChosen);
        // set drawFour
        game.updateCurrentPlayer(); // updates current player
        if(game.getLastCard() instanceof WildDrawFourCard)
            game.getCurrPlayer().setDrawFour(true);
        hideWindow();
        InGame nextState = new InGame(game); // moves on to the next game state
    }

}
