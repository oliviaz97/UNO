package UNO.Cards;

/**
 * A DrawTwoCard forces the next player to draw two cards in a row
 * And prevents this player to play any card in this round
 */
public class DrawTwoCard extends Card
{

    public DrawTwoCard(CardColor color)
    {
        super(color); // sets color
    }

    public String getCardStr()
    {
        String colorStr = getColorString();
        String cardStr = colorStr + " DRAW TWO";
        return cardStr;
    }

}