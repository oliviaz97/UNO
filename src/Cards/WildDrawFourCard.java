package UNO.Cards;

import UNO.Cards.Card;

//**
 /** A DrawTwoCard forces the next player to draw two cards in a row
 /** And prevents this player to play any card in this round
 /**/
public class WildDrawFourCard extends Card
{
    public WildDrawFourCard()
    {
        super(CardColor.ALL); // sets color
    }

    public String getCardStr()
    {
        String cardStr = "WILD DRAW FOUR";
        return cardStr;
    }

}