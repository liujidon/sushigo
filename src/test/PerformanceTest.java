package test;

import game.Board;
import game.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cards.Card;
import cards.Chopsticks;
import cards.Dumpling;
import cards.Maki;
import cards.Nigiri;
import cards.Pudding;
import cards.Sashimi;
import cards.Tempura;
import cards.Wasabi;

public class PerformanceTest {
	public static void main(String[] args) {
		Map<Integer, Integer> finishCount = new HashMap<Integer, Integer>();
		
		List<Card> mode = new ArrayList<Card>();
		mode.add(new Nigiri());
		mode.add(new Maki());
		mode.add(new Tempura());
		mode.add(new Sashimi());
		mode.add(new Wasabi());
		mode.add(new Chopsticks());
		mode.add(new Dumpling());
		mode.add(new Pudding());
		int gameNumber = 1000;
		System.out.println( "Simulating " + gameNumber + " games..." );

		for(int i = 0; i < gameNumber; i++) {
			Board board = new Board(5, mode);
			
			Player myPlayer = board.selectProbEnginePlayer(0);
			
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
			
			int rank = board.getRank(myPlayer.number);
			int count = finishCount.containsKey(rank) ? finishCount.get(rank) : 0;
			finishCount.put(rank, count + 1);
		}
		
		int firstPlaceCount = finishCount.containsKey(1) ? finishCount.get(1) : 0; 
		System.out.println(String.format("First Place: %d/%d (%.2f)", firstPlaceCount, gameNumber, firstPlaceCount/(double)gameNumber ));
		
		int secondPlaceCount = finishCount.containsKey(2) ? finishCount.get(2) : 0; 
		System.out.println(String.format("Second Place: %d/%d (%.2f)", secondPlaceCount, gameNumber, secondPlaceCount/(double)gameNumber ));
		
		int thirdPlaceCount = finishCount.containsKey(3) ? finishCount.get(3) : 0; 
		System.out.println(String.format("Third Place: %d/%d (%.2f)", thirdPlaceCount, gameNumber, thirdPlaceCount/(double)gameNumber ));
	}
}
