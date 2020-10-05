package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome extends GameWindow{
    JTextArea totalPlayers; // text area that gets total num of players
    protected int numPlayers;
    JButton okay;

    public Welcome()
    {
        super();
        String welcomeText = "Welcome to UNO!";
        JLabel welcomeLabel = createLabel(welcomeText, Component.CENTER_ALIGNMENT);

        // get number of players from input
        String getNum = "Enter total number of players:";
        totalPlayers = getInput(getNum);

        padding(1);
        okay = createButton("Okay");
        okay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numPlayers = Integer.parseInt(totalPlayers.getText());
                hideWindow();
                StartGame startGame = new StartGame(numPlayers);
            }
        });
        addEndPadding();
    }
    public static void main(String[] args){
        Welcome welcome = new Welcome();
    }

}
