package GUI;
import javax.sound.midi.ControllerEventListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import UNO.*;

/**
 * Generic game widow class for displaying all four game states
 * Reference: Java GUI Tutorial - Make a GUI in 13 Minutes - Alex Lee (Youtube)
 * Reference: CS 242 Assignment1.1 example "Hand written code"
 */
public abstract class GameWindow
{
    // fields
    protected JFrame frame; // main game window
    protected final int WIDTH = 400, HEIGHT = 700;
    protected final int X = 400, Y = 100;
    protected JPanel panel;
    protected final int PANEL_WIDTH = 340, PANEL_HEIGHT = 640;
    protected final int TOP = 30, LEFT = 30, BOTTOM = 30, RIGHT = 30;
    protected final int TEXT_ROW = 1, TEXT_COL = 10;
    protected final int TEXT_WIDTH = 100, TEXT_HEIGHT = 20;
    protected final int BUTTON_WIDTH = 50, BUTTON_HEIGHT = 20;

    /**
     * Constructor that creates the default window of the game
     */
    public GameWindow()
    {
        frame = new JFrame("UNO");
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocation(X, Y);

        panel = createPanel();

        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Creates and initializes a JPanel object
     * @return Panel created
     */
    protected JPanel createPanel()
    {
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(TOP, LEFT, BOTTOM, RIGHT));
        panel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    /**
     * Creates and initializes a JLabel object
     * @param text Text shown
     * @param alignment Alignment (left, right, center)
     * @return Label created
     */
    protected JLabel createLabel(String text, float alignment)
    {
        JLabel label = new JLabel();
        label.setText(text);
        label.setAlignmentX(alignment);
        panel.add(label);
        label.setVisible(true);
        return label;
    }

    /**
     * Creates and initializes a JTextArea object
     * @return TextArea created
     */
    protected JTextArea createTextArea()
    {
        JTextArea textArea = new JTextArea(TEXT_ROW, TEXT_COL);
        textArea.setMaximumSize(new Dimension(TEXT_WIDTH, TEXT_HEIGHT));
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(textArea);
        textArea.setVisible(true);
        return textArea;
    }

    /**
     * Creates and initializes a JButton object
     * @param text Text shown on button
     * @return Button created
     */
    protected JButton createButton(String text)
    {
        JButton button = new JButton();
        button.setText(text);
        button.setSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button);
        return button;
    }

    /**
     * Displays a prompt and gets user input on the next line
     * @param prompt Prompt for the user
     * @return Text area created
     */
    protected JTextArea getInput(String prompt)
    {
        JLabel label = createLabel(prompt, Component.CENTER_ALIGNMENT);
        JTextArea textArea = createTextArea();
        return textArea;
    }

    /**
     * Pad given number of lines to create gaps between components
     * @param numLines The number of lines to pad
     */
    public void padding(int numLines)
    {
        for(int i = 0; i < numLines; i++)
        {
            JLabel padding = createLabel("        ", Component.CENTER_ALIGNMENT);
        }
    }

    /**
     * Adds a line of padding at the end
     * HELPS WITH VISIBILITY ISSUES
     */
    protected void addEndPadding() {
        JLabel endPadding = createLabel("        ", Component.CENTER_ALIGNMENT);
        endPadding.setVisible(false); // this helps show everything on the window
    }

    /**
     * Hides current window
     */
    protected void hideWindow()
    {
        this.frame.setVisible(false);
        this.frame.dispose();
    }
}
