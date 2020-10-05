package test.UNO;

import UNO.Cards.*;
import UNO.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private static Game newGame;
    private static final int NUM_PLAYERS = 2;
    private static final Card.CardColor YELLOW = Card.CardColor.YELLOW;
    private static final Card.CardColor RED = Card.CardColor.RED;
    private static final Card.CardColor BLUE = Card.CardColor.BLUE;
    private static final Card.CardColor GREEN = Card.CardColor.GREEN;
    private static final int NUM_CARD_TYPES = 6;
    private static ArrayList<Player> players = new ArrayList<>();


    @BeforeAll
    public static void setUpBeforeClass()
    {
        newGame = new Game();
        for(int i = 0; i < NUM_PLAYERS; i++)
        {
            players.add(new Player("player" + (i + 1), newGame));
        }
    }

    @Test
    public void testGetPlayerFromList() {
        newGame.getPlayerFromList(NUM_PLAYERS, players);

        // test totalPlayer is getting set correctly
        assertEquals(newGame.getTotalPlayers(), NUM_PLAYERS);

        for(int i = 0; i < newGame.getTotalPlayers(); i++)
        {
            assertEquals(players.get(i).getPlayerName(), newGame.getPlayers().get(i).getPlayerName());
            assertEquals(players.get(i).getGame(), newGame.getPlayers().get(i).getGame());
        }
    }

    /**
     * Current card is valid if it is:
     * 1. A number card that matches the color or number of the last card
     * 2. A +2/reverse/skip card and matches with the color of the last card
     * 3. A wild card or wild +4 card is always valid
     *
     * For each card in last, compare with it a list of good and bad choices
     */
    @Test
    public void testCardIsValid() {
        // fake last card to compare with
        ArrayList<Card> last = new ArrayList<>();
        ArrayList<ArrayList<Card>> good = new ArrayList<>(); // list of lists of good matches
        ArrayList<ArrayList<Card>> bad = new ArrayList<>(); // list of lists of bad matches

        last.add(new NumCard(BLUE, 9));
        last.add(new SkipCard(GREEN));
        last.add(new ReverseCard(RED));
        last.add(new DrawTwoCard(YELLOW));
        last.add(new WildCard());
        last.add(new WildDrawFourCard());

        // construct good list
        ArrayList<Card> goodNum = new ArrayList<>();
        goodNum.add(new NumCard(BLUE, 0));
        goodNum.add(new NumCard(BLUE, 3));
        goodNum.add(new NumCard(BLUE, 5));
        goodNum.add(new NumCard(RED, 9));
        goodNum.add(new NumCard(YELLOW, 9));
        goodNum.add(new NumCard(GREEN, 9));
        goodNum.add(new NumCard(BLUE, 9));
        goodNum.add(new SkipCard(BLUE));
        goodNum.add(new ReverseCard(BLUE));
        goodNum.add(new DrawTwoCard(BLUE));
        goodNum.add(new WildCard());
        goodNum.add(new WildDrawFourCard());

        ArrayList<Card> goodSkip = new ArrayList<>();
        goodSkip.add(new SkipCard(GREEN));
        goodSkip.add(new ReverseCard(GREEN));
        goodSkip.add(new DrawTwoCard(GREEN));
        goodSkip.add(new WildDrawFourCard());
        goodSkip.add(new WildCard());

        ArrayList<Card> goodReverse = new ArrayList<>();
        goodReverse.add(new SkipCard(RED));
        goodReverse.add(new ReverseCard(RED));
        goodReverse.add(new DrawTwoCard(RED));
        goodReverse.add(new WildDrawFourCard());
        goodReverse.add(new WildCard());

        ArrayList<Card> goodDrawTwo = new ArrayList<>();
        goodDrawTwo.add(new SkipCard(YELLOW));
        goodDrawTwo.add(new ReverseCard(YELLOW));
        goodDrawTwo.add(new DrawTwoCard(YELLOW));
        goodDrawTwo.add(new WildDrawFourCard());
        goodDrawTwo.add(new WildCard());

        ArrayList<Card> goodWild = new ArrayList<>();
        goodWild.add(new NumCard(YELLOW, 5));
        goodWild.add(new NumCard(YELLOW, 1));
        goodWild.add(new NumCard(YELLOW, 3));
        goodWild.add(new SkipCard(YELLOW));
        goodWild.add(new ReverseCard(YELLOW));
        goodWild.add(new DrawTwoCard(YELLOW));
        goodWild.add(new WildCard());
        goodWild.add(new WildDrawFourCard());

        ArrayList<Card> goodWildDrawFour = new ArrayList<>();
        goodWildDrawFour.add(new NumCard(GREEN, 5));
        goodWildDrawFour.add(new NumCard(GREEN, 1));
        goodWildDrawFour.add(new NumCard(GREEN, 3));
        goodWildDrawFour.add(new SkipCard(GREEN));
        goodWildDrawFour.add(new ReverseCard(GREEN));
        goodWildDrawFour.add(new DrawTwoCard(GREEN));
        goodWildDrawFour.add(new WildCard());
        goodWildDrawFour.add(new WildDrawFourCard());

        good.add(goodNum);
        good.add(goodSkip);
        good.add(goodReverse);
        good.add(goodDrawTwo);
        good.add(goodWild);
        good.add(goodWildDrawFour);

        // construct bad list
        ArrayList<Card> badNum = new ArrayList<>();
        ArrayList<Card> badSkip = new ArrayList<>();
        ArrayList<Card> badReverse = new ArrayList<>();
        ArrayList<Card> badDrawTwo = new ArrayList<>();
        ArrayList<Card> badWild = new ArrayList<>();
        ArrayList<Card> badWildDrawFour = new ArrayList<>();

        // test good matches
        for(int i = 0; i < NUM_CARD_TYPES; i++)
        {
            int len = good.get(i).size();
            newGame.addToDiscardPile(last.get(i)); // sets lastCard
            if(i == 4)
                newGame.setLastColor(YELLOW);
            if(i == 5)
                newGame.setLastColor(GREEN);
            for(int j = 0; j < len; j++)
            {
                Card card = good.get(i).get(j);
                assertEquals(true, newGame.cardIsValid(card));
            }
        }

    }

    @Test
    public void testDrawOrNotDraw()
    {
        assert(newGame.set_wishToDraw() == 0 || newGame.set_wishToDraw() == 1);
    }
    
    /**
     * Sets up a game of two and tests if game is set up correctly
     * Tests startGame, assignCards, setInitialState
     */
    @Test
    public void testGameSetUp()
    {
        Game new_game = new Game();
        ArrayList<Player> new_players = new ArrayList<>();
        for(int i = 0; i < NUM_PLAYERS; i++)
        {
            new_players.add(new Player("player" + i, new_game));
        }
        new_game.startGame(NUM_PLAYERS, new_players);
        assertEquals(NUM_PLAYERS,new_game.getTotalPlayers());
        assertEquals(7, new_game.getPlayers().get(0).getNumOfCards()); // player 1 has 7 cards
        assertEquals(7, new_game.getPlayers().get(1).getNumOfCards()); // player 2 has 7 cards
        assertEquals(1, new_game.getDiscardPile().size()); // discard pile has one card
        assert(new_game.getDiscardPile().get(new_game.getDiscardPile().size() - 1) instanceof NumCard); // top of discard is a number card
    }
    
    /**
     * Sets up a game of two players and tests if special effects are in place
     * Don't care about if playing the right card
     * Also tests for turns and reverse
     */
    @Test
    public void testCardTakesEffect()
    {
        newGame.startGame(NUM_PLAYERS, players);
        Player p1 = newGame.getPlayers().get(0);
        Player p2 = newGame.getPlayers().get(1);
        assertEquals(p1, newGame.getCurrPlayer());

        // test skipped
        Card currCard = new SkipCard(BLUE);
        p1.addCard(currCard);
        testSkipped(newGame, p1, p2, currCard);

        // test drawTwo
        currCard = new DrawTwoCard(BLUE);
        p1.addCard(currCard);
        testDrawTwo(newGame, p1, p2, currCard);

        // test drawFour
        currCard = new WildDrawFourCard();
        p1.addCard(currCard);
        testDrawFour(newGame, p1, p2, currCard);

        // test swap
        currCard = new NumCard(BLUE, 7);
        p1.addCard(currCard);
        testSwap(newGame, p1, p2, currCard);
    }

    public void testDrawTwo(Game game, Player p1, Player p2, Card currCard)
    {
        game.updateCurrentPlayer(); // curr is now p1

        game.getCurrPlayer().playOne(currCard);
        game.cardTakesEffect(currCard);
        game.updateCurrentPlayer(); // curr is now p2
        assertEquals(p2, game.getCurrPlayer());
        assertEquals(true, game.getCurrPlayer().willDrawTwo());

        int numBefore = game.getCurrPlayer().getNumOfCards(); // preserve
        game.playerMove();
        assertEquals(numBefore + 2, game.getCurrPlayer().getNumOfCards());
        testReset(game, p2);
    }

    public void testSkipped(Game game, Player p1, Player p2, Card currCard)
    {
        game.getCurrPlayer().playOne(currCard);
        game.cardTakesEffect(currCard);
        game.updateCurrentPlayer(); // curr is now p2
        assertEquals(p2, game.getCurrPlayer());
        assertEquals(true, game.getCurrPlayer().willBeSkipped());

        game.playerMove();
        testReset(game, p2);
    }

    public void testDrawFour(Game game, Player p1, Player p2, Card currCard)
    {
        assert(game.getPlayers().get(0).willBeSkipped() == false);
        assert(game.getPlayers().get(0).willDrawFour() == false);
        assert(game.getPlayers().get(0).willDrawTwo() == false);
        assert(game.getPlayers().get(1).willBeSkipped() == false);
        assert(game.getPlayers().get(1).willDrawFour() == false);
        assert(game.getPlayers().get(1).willDrawTwo() == false);

        game.updateCurrentPlayer(); // curr is now p1
        game.getCurrPlayer().playOne(currCard);
        game.cardTakesEffect(currCard);
        game.updateCurrentPlayer(); // curr is now p2
        assertEquals(p2, game.getCurrPlayer());
        assert(game.getCurrPlayer().willDrawFour() == true);
        assertEquals(true, game.getCurrPlayer().willDrawFour());

        int numBefore2 = game.getCurrPlayer().getNumOfCards(); // preserve
        game.playerMove();
        assertEquals(numBefore2 + 4, game.getCurrPlayer().getNumOfCards());
        testReset(game, p2);
    }

    public void testSwap(Game game, Player p1, Player p2, Card currCard)
    {
        game.updateCurrentPlayer(); // curr is now p1

        game.getCurrPlayer().playOne(currCard);
        game.updateCurrentPlayer(); // curr is now p2
        assertEquals(p2, game.getCurrPlayer());
        assertEquals(7, p1.getNumOfCards());
        assertEquals(13, p2.getNumOfCards());

        game.cardTakesEffect(currCard);
        assertEquals(13, p1.getNumOfCards());
        assertEquals(7, p2.getNumOfCards());

        testReset(game, p2);
    }

    public void testReset(Game game, Player p2)
    {
        assertEquals(false, p2.willBeSkipped());
        assertEquals(false, p2.willDrawTwo());
        assertEquals(false, p2.willDrawFour());
    }

    @Test
    public void testChallengeFail()
    {
        Game g = new Game();
        Player p1 = new Player("player1", g);
        Player p2 = new Player("player2", g);

        Card wild4 = new WildDrawFourCard();
        // each start with 3 cards
        p1.addCard(wild4);
        p1.addCard(new NumCard(GREEN, 4));
        p1.addCard(new NumCard(YELLOW, 4));
        p2.addCard(new ReverseCard(BLUE));
        p2.addCard(new ReverseCard(BLUE));
        p2.addCard(new ReverseCard(BLUE));

        g.setLastCard(new NumCard(RED, 1));
        g.getDiscardPile().add(new NumCard(RED, 1));
        // p1 plays wildDrawFour legally
        p1.playOne(wild4);
        assert(p1.ifWildFourLegal() == true);
        //game.setLastCard(wild4);
        g.setCurrPlayer(p2); // p2 is now playing
        assert(g.challenge() == false); // challenge should fail
        assertEquals(2,p1.getNumOfCards());
        assertEquals(9, p2.getNumOfCards());
    }
}