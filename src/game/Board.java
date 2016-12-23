package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cards.Card;
import cards.Chopsticks;
import cards.Dumpling;
import cards.Maki;
import cards.Nigiri;
import cards.Pudding;
import cards.Sashimi;
import cards.Tempura;
import cards.Wasabi;

public class Board {

	List<Card> deck = new ArrayList<Card>();
	List<Card> round1cards = new ArrayList<Card>();
	List<Card> round2cards = new ArrayList<Card>();
	List<Card> round3cards = new ArrayList<Card>();
	
	List<Player> players = new ArrayList<Player>();
	private static final int NIGIRI_COUNT = 12;
	private static final int SUSHI_ROLL_COUNT = 12;
	private static final int APPETIZER_COUNT = 8;
	private static final int SPECIALS_COUNT = 3;
	private static final int DESSERT_COUNT = 15;
	
	public int playerCount = 0;
	
	public Board(int playerNumber, List<Card> mode) {
		this.playerCount = playerNumber;
		for(int i = 1; i <= playerNumber; i++) {
			players.add(new Player("Player " + playerNumber));
		}
		newDeck(mode);
		
	}
	
	private void shuffleDeck(List<Card> cards) {
		long seed = System.nanoTime();
		Collections.shuffle(cards, new Random(seed));
	}
	
	private void newDeck(List<Card> mode) {
		for(Card card : mode) {
			if(card instanceof Nigiri) {
				for(int i = 0; i < NIGIRI_COUNT; i++) {
					deck.add(new Nigiri(i/4+1));
				}
			}
			if(card instanceof Maki) {
				for(int i = 0; i < SUSHI_ROLL_COUNT; i++) {
					deck.add(new Maki(i/4+1));
				}
			}
			if(card instanceof Tempura) {
				for(int i = 0; i < APPETIZER_COUNT; i++)
					deck.add(new Tempura());
			}
			if(card instanceof Sashimi) {
				for(int i = 0; i < APPETIZER_COUNT; i++)
					deck.add(new Sashimi());
			}
			if(card instanceof Dumpling) {
				for(int i = 0; i < APPETIZER_COUNT; i++)
					deck.add(new Dumpling());
			}
			if(card instanceof Chopsticks) {
				for(int i = 0; i < SPECIALS_COUNT; i++)
					deck.add(new Chopsticks());
			}
			if(card instanceof Wasabi) {
				for(int i = 0; i < SPECIALS_COUNT; i++)
					deck.add(new Wasabi());
			}
			if(card instanceof Pudding) {
				continue;
			}
		}
		shuffleDeck(this.deck);
		Card dessert = mode.get(mode.size()-1);
		List<Card> dessertCards = new Pudding();
		
	}
}
