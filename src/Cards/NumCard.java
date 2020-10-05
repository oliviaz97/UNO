package UNO.Cards;

/**
* A UNO.Cards.NumberCard could be of the following colors:
* "yellow", "red", "green", "blue"
* with number "0" - "9" on the card
*/
public class NumCard extends Card
{
    // fields
    int number;

    public NumCard(CardColor color, int number)
    {
        super(color); // sets color
        this.number = number;
    }

    // gets number on the card
    public int getNumber()
    {
        return this.number;
    }

    public String getCardStr()
    {
        String colorStr = getColorString();
        String numStr = Integer.toString(number);
        String cardStr = colorStr + " " + numStr;
        return cardStr;
    }

}