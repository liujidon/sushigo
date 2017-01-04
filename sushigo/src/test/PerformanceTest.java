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
		Map<Integer, Integer> finishCountP2 = new HashMap<Integer, Integer>();

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
			
			Player p1 = board.selectProbEnginePlayer(0);
			Player p2 = board.selectRiskyEnginePlayer(2);
			//Player p2 = p1;
			
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
			
			int rank = board.getRank(p1.number);
			int count = finishCount.containsKey(rank) ? finishCount.get(rank) : 0;
			finishCount.put(rank, count + 1);
			
			rank = board.getRank(p2.number);
			count = finishCountP2.containsKey(rank) ? finishCountP2.get(rank) : 0;
			finishCountP2.put(rank, count+1);
		}
		
		System.out.println("First Player Score"); 
		int firstPlaceCount = finishCount.containsKey(1) ? finishCount.get(1) : 0; 
		System.out.println(String.format("First Place: %d/%d (%.2f)", firstPlaceCount, gameNumber, firstPlaceCount/(double)gameNumber ));
		
		int secondPlaceCount = finishCount.containsKey(2) ? finishCount.get(2) : 0; 
		System.out.println(String.format("Second Place: %d/%d (%.2f)", secondPlaceCount, gameNumber, secondPlaceCount/(double)gameNumber ));
		
		int thirdPlaceCount = finishCount.containsKey(3) ? finishCount.get(3) : 0; 
		System.out.println(String.format("Third Place: %d/%d (%.2f)", thirdPlaceCount, gameNumber, thirdPlaceCount/(double)gameNumber ));
		
		
		System.out.println("Second Player Score");
		int firstPlaceCountP2 = finishCountP2.containsKey(1) ? finishCountP2.get(1) : 0; 
		System.out.println(String.format("First Place: %d/%d (%.2f)", firstPlaceCountP2, gameNumber, firstPlaceCountP2/(double)gameNumber ));
		
		int secondPlaceCountP2 = finishCountP2.containsKey(2) ? finishCountP2.get(2) : 0; 
		System.out.println(String.format("Second Place: %d/%d (%.2f)", secondPlaceCountP2, gameNumber, secondPlaceCountP2/(double)gameNumber ));
		
		int thirdPlaceCountP2 = finishCountP2.containsKey(3) ? finishCountP2.get(3) : 0; 
		System.out.println(String.format("Third Place: %d/%d (%.2f)", thirdPlaceCountP2, gameNumber, thirdPlaceCountP2/(double)gameNumber ));
	}
}
