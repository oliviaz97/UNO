package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import UNO.*;

public class StartGame extends GameWindow
{
    private final int LABEL_X = 10;
    private final int LABEL_Y = 10;
    protected int numPlayers;
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<JTextArea> playerNameTexts = new ArrayList<>(); // list of text areas for each player name
    JButton start;

    public StartGame(int numPlayers)
    {
        super();
        this.numPlayers = numPlayers;
        getPlayerNames(); // prompts and gets the names of all players

        // Padding between last text area and buttton
        JLabel padding = createLabel("        ", Component.CENTER_ALIGNMENT);
        JLabel padding2 = createLabel("        ", Component.CENTER_ALIGNMENT);

        // clicking start game button should create a new instance
        // of a Game and start game
        String startText = "Start game";
        start = createButton(startText);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game game = new Game();
                for(int i = 0; i < numPlayers; i++)
                {
                    String name = playerNameTexts.get(i).getText();
                    Player newPlayer;
                    // no player name entered, fill up with AI
                    if(name.equals(""))
                    {
                        String aiName = "AI" + (i+1);
                        newPlayer = new AI(aiName, game);
                    }
                    else // human player
                    {
                        newPlayer = new Player(name,game);
                    }
                    players.add(newPlayer);
                }
                hideWindow();
                game.startNewGame(numPlayers, players);
                InGame inGame = new InGame(game);
            }
        });

        addEndPadding();
    }

    /**
     * Gets all the player's names one by one
     */
    public void getPlayerNames()
    {
        for(int i = 0; i < numPlayers; i++)
        {
            String getName = "Enter player" + (i + 1) + " name:";
            playerNameTexts.add(getInput(getName));
        }
    }

    public static void main(String[] args){
        StartGame gw = new StartGame(4);
    }
}
