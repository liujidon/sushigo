package game;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import cards.Chopsticks;
import cards.Dumpling;
import cards.Maki;
import cards.Nigiri;
import cards.Pudding;
import cards.Sashimi;
import cards.Tempura;
import cards.Wasabi;

public class SushiGo {

	public static void main(String[] args) {
		List<Card> mode = new ArrayList<Card>();
		mode.add(new Nigiri());
		mode.add(new Maki());
		mode.add(new Tempura());
		mode.add(new Sashimi());
		mode.add(new Wasabi());
		mode.add(new Chopsticks());
		mode.add(new Dumpling());
		mode.add(new Pudding());
		
		Board board = new Board(3, mode);
		
		board.selectHumanPlayer(0);
		board.selectProbEnginePlayer(2);
		
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.computeScores();
		board.printPlayerEaten();
		board.newRound();
		
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.computeScores();
		board.printPlayerEaten();
		board.newRound();
		
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.takeTurn();
		board.computeScores();
		board.printPlayerEaten();

		//board.printPlayers();
	}
}
