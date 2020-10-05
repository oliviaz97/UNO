package UNO;

import UNO.Cards.Card;

/**
 * AI player doesn't require any user input
 * Automatically fills up empty player spots
 * Random choices
 */
public class AI extends Player{
    //String[] aiNames = {"AI1", "AI2", "AI3", "AI4"};
    Card.CardColor[] colorChoices = {Card.CardColor.YELLOW, Card.CardColor.RED, Card.CardColor.BLUE, Card.CardColor.GREEN};

    public AI(String name, Game game) {
        super(name, game);
    }

    /**
     * Returns random color as wild card color choice
     * @return Random color chosen for the wild cards
     */
    public Card.CardColor pickColor()
    {
        int colorIdx = (int)(Math.random() * 4); // random int in [0,4)
        return colorChoices[colorIdx];
    }

    /**
     * Randomly chooses the index of player to swap hands with
     * @return Randomly chosen index of player to swap hands with
     */
    public int getSwapIndex()
    {
        int num = game.getTotalPlayers();
        int idx = (int)(Math.random() * num); // random int in [0, num)
        return idx;
    }

    /**
     * Randomly chooses to challenge or not(draw four)
     * @return Challenge or draw four
     */
    public boolean getChallenge()
    {
        return game.getIfChallenge();
    }
}
