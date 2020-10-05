package test.UNO;

import UNO.AI;
import UNO.Cards.Card;
import UNO.Game;
import UNO.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AITest {

    @Test
    void pickColor() {
        Game game = new Game();
        AI ai = new AI("AI", game);
        Card.CardColor color = ai.pickColor();
        assert(color == Card.CardColor.BLUE
                || color == Card.CardColor.GREEN
                || color == Card.CardColor.YELLOW
                || color == Card.CardColor.RED);
    }

    @Test
    void getSwapIndex() {
        Game game = new Game();
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("p1", game));
        players.add(new Player("p2", game));
        players.add(new Player("p3", game));
        players.add(new Player("p4", game));

        game.startNewGame(4, players);
        assert(game.getTotalPlayers() == 4);
        AI ai = new AI("AI", game);
        int idx = ai.getSwapIndex();
        assert(idx == 0 || idx == 1 || idx == 2 || idx == 3);
    }

    @Test
    void getChallenge() {
        Game game = new Game();
        AI ai = new AI("AI", game);
        boolean challenge = ai.getChallenge();
        assert(challenge == true || challenge == false);
    }
}