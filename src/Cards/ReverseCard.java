package UNO.Cards;

import UNO.Cards.Card;

//**
 /** A DrawTwoCard forces the next player to draw two cards in a row
 /** And prevents this player to play any card in this round
 /**/
public class ReverseCard extends Card
{
    public ReverseCard(CardColor color)
    {
        super(color); // sets color
    }

    @Override
    public String getCardStr() {
        String colorStr = getColorString();
        String cardStr = colorStr + " REVERSE";
        return cardStr;
    }

}