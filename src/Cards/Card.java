package UNO.Cards;
/**
 * Generic Card class
 */
public abstract class Card
{
    /**
     * A CardColor enum has five possible values: YELLOW, RED, GREEN, BLUE, ALL
     * WildCard types have the color of ALL
     */
    public enum CardColor
    {
        YELLOW, RED, GREEN, BLUE, ALL
    }

    // fields
    CardColor color;

    /**
     * constructor that sets color of the card
     */
    public Card(CardColor color)
    {
        this.color = color;
    }
    /**
     * Gets color of card
     * @return: color of card
     */
    public CardColor getColor()
    {
        return this.color;
    }

    /**
     * Get a string denoting the color of the card
     * Helper to the printCard method
     * @return: string denoting the card color
     */
    public String getColorString()
    {
        String colorStr = "";
        if(color == CardColor.YELLOW)
            colorStr = "YELLOW";
        else if(color == CardColor.RED)
            colorStr = "RED";
        else if(color == CardColor.BLUE)
            colorStr = "BLUE";
        else if(color == CardColor.GREEN)
            colorStr = "GREEN";
        else
            colorStr = "WILD";
        return colorStr;
    }
    
    /**
     * Abstract method that prints card to terminal
     */
    public abstract String getCardStr();


}