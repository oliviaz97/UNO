package GUI;

import UNO.Game;

import javax.swing.*;
import java.awt.*;

/**
 * A EndOfGame window is displayed when a winner is found
 */
public class EndOfGame extends GameWindow{
    Game game;

    public EndOfGame(Game game)
    {
        super();
        this.game = game;
        displayWinMessage();
        addEndPadding();
    }

    public void displayWinMessage()
    {
        String winner = game.getWinner().getPlayerName();
        JLabel colorList = createLabel(winner + " won! ", Component.CENTER_ALIGNMENT);
        padding(1);
        JLabel endMsg = createLabel("Thanks for playing UNO.", Component.CENTER_ALIGNMENT);
    }
}
