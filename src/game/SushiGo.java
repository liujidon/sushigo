package game;

import cards.*;

import java.util.ArrayList;
import java.util.List;

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
		
		Board board = new Board(5, mode);
		
		board.selectSetEnginePlayer(1);
		board.selectRiskyEnginePlayer(3);

		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.computeScores();
		board.printPlayerEaten();
		board.newRound();

		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.computeScores();
		board.printPlayerEaten();
		board.newRound();

		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.takeTurn();
		board.printPlayerHand();
		board.computeScores();
		board.printPlayerEaten();

		//board.printPlayers();
	}
}
