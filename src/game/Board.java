package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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

	LinkedList<Card> deck = new LinkedList<Card>();
	List<Card> round1cards = new ArrayList<Card>();
	List<Card> round2cards = new ArrayList<Card>();
	List<Card> round3cards = new ArrayList<Card>();
	
	List<Player> players = new ArrayList<Player>();
	private static final int SALMON_NIGIRI_COUNT = 10;
	private static final int SQUID_NIGIRI_COUNT = 5;
	private static final int EGG_NIGIRI_COUNT = 5;
	
	private static final int MAKI_ROLL_1_COUNT = 6;
	private static final int MAKI_ROLL_2_COUNT = 12;
	private static final int MAKI_ROLL_3_COUNT = 8;
	
	private static final int TEMPURA_COUNT = 14;
	private static final int SASHIMI_COUNT = 14;
	private static final int DUMPLING_COUNT = 14;
	private static final int PUDDING_COUNT = 10;
	private static final int WASABI_COUNT = 6;
	private static final int CHOPSTICKS_COUNT = 4;
	
	public int playerCount = 0;
	
	public Board(int playerNumber, List<Card> mode) {
		if(playerNumber < 2) {
			System.out.print("Must require at least 2 players.");
			return;
		}
		this.playerCount = playerNumber;
		for(int i = 1; i <= playerNumber; i++) {
			Player newPlayer = new Player("Player " + i); 
			if(i > 1) {
				newPlayer.leftPlayer = players.get(i-2);
				players.get(i-2).rightPlayer = newPlayer;
			}
			if(i == playerNumber) {
				newPlayer.rightPlayer = players.get(0);
				players.get(0).leftPlayer = newPlayer;
			}
			
			players.add(newPlayer);
		}
		newDeck(mode);
		dealCards();
	}
	
	public void takeTurn() {
		for(Player player : players)
			player.makeMove();
		if(players.get(0).hand.isEmpty()) {
			computeScores();
			return;
		}
		//pass on the cards
		List<Card> tempHand = players.get(0).hand;
		for(int i = 0; i < players.size() - 1; i++) {
			players.get(i).hand = players.get(i).rightPlayer.hand;
		}
		players.get(players.size()-1).hand = tempHand;
	}
	
	public void startNewRound() {
		
	}
	
	public void computeScores() {
		
		for(Player p : players) {
			//wasabi
			Nigiri lastNigiri = null;
			for(int i = p.eaten.size()-1; i >= 0; i--) {
				Card c = p.eaten.get(i);
				if(c instanceof Nigiri)
					lastNigiri = (Nigiri) c;
				else if(c instanceof Wasabi && lastNigiri != null) {
					c.value = Wasabi.SCORE * lastNigiri.value;
					lastNigiri = null;
				}
			}
			
			//tempura and sashimi
			float tempuraPoints = Tempura.SCORE * (int) (p.tempuraCount/Tempura.SET);
			float sashimiPoints = Sashimi.SCORE * (int) (p.sashimiCount/Sashimi.SET);
			for(Card c : p.eaten) {
				if(tempuraPoints > 0 && c instanceof Tempura) {
					c.value = Tempura.SCORE/(float)Tempura.SET;
					tempuraPoints -= c.value;
				}
				if(sashimiPoints > 0 && c instanceof Sashimi) {
					c.value = Sashimi.SCORE/(float)Sashimi.SET;
					sashimiPoints -= c.value;
				}
			}
		}
		
		//maki count
		int makiRank = 0, lastMakiCount = -1;
		int firstPlaceCount = 0, secondPlaceCount = 0;
		Collections.sort(players, Player.MAKI_COMPARE);
		for(Player p : players) {
			if(p.makiCount != lastMakiCount)
				makiRank++;
			p.makiRank = makiRank;
			if(makiRank == 1)
				firstPlaceCount++;
			else if(makiRank == 2)
				secondPlaceCount++;
		}
		for(Player p : players) {
			if(p.makiRank == 0)
				continue;
			int makiScore = 0;
			if(p.makiRank == 1)
				makiScore = Maki.FIRST_PLACE_SCORE/firstPlaceCount;
			else if(p.makiRank == 2)
				makiScore = Maki.SECOND_PLACE_SCORE/secondPlaceCount;
			for(Card c : p.eaten) {
				if(c instanceof Maki)
					c.value = makiScore * ((Maki) c).weight/p.makiCount;
			}
		}
		
		//pudding count
		Collections.sort(players, Player.PUDDING_COMPARE);
		int puddingRank = 0, lastPuddingCount = -1;
		firstPlaceCount = 0;
		int lastPlaceRank = 0, lastPlaceCount = 0;
		for(Player p : players) {
			if(p.puddingCount != lastPuddingCount)
				puddingRank++;
			p.puddingRank = puddingRank;
			if(puddingRank == 1)
				firstPlaceCount++;
			if(puddingRank > lastPlaceRank) {
				lastPlaceRank = puddingRank;
				lastPlaceCount = 1;
			} else if(puddingRank == lastPlaceRank) {
				lastPlaceCount++;
			}
		}
		for(Player p : players) {
			int puddingScore = 0;
			if(p.puddingRank == 1)
				puddingScore = Pudding.FIRST_PLACE_SCORE/firstPlaceCount;
			else if(p.puddingRank == lastPlaceRank)
				puddingScore = Pudding.LAST_PLACE_SCORE/lastPlaceCount;
			else
				continue;
			for(Card c : p.eaten) {
				if(c instanceof Pudding)
					c.value = puddingScore / p.puddingCount;
			}
		}
		//add up scores
		for(Player p : players) {
			//account for no pudding card
			if(p.puddingCount == 0)
				p.score = Pudding.LAST_PLACE_SCORE/lastPlaceCount;
			else
				p.score = 0;
			
			for(Card c : p.eaten)
				p.score += c.value;
		}
	}
	
	public void printDeck() {
		System.out.println("Deck: ");
		for(Card c : deck)
			System.out.print(c + " ");
	}
	
	public void printPlayers() {
		Collections.sort(players, Player.SCORE_COMPARE);
		for(Player p : players) {
			System.out.println(p);
		}
	}
	
	public void printPlayerHand() {
		for(Player p : players) {
			p.printHand();
			System.out.println();
		}
	}
	
	public void printPlayerEaten() {
		Collections.sort(players, Player.SCORE_COMPARE);
		for(Player p : players) {
			p.printEaten();
			System.out.println();
		}
	}
	
	private void dealCards() {
		System.out.println("Dealing cards...");
		int cardsPerPlayer = 9;
		for(Player p : players) {
			for(int i = 0; i < cardsPerPlayer; i++) {
				p.hand.add(deck.pop());
			}
		}	
	}
	
	private void shuffleDeck(List<Card> cards) {
		System.out.println("Shuffling cards...");
		long seed = System.nanoTime();
		Collections.shuffle(cards, new Random(seed));
	}
	
	private void newDeck(List<Card> mode) {
		System.out.println("Creating deck...");
		for(Card card : mode) {
			if(card instanceof Nigiri) {
				for(int i = 0; i < EGG_NIGIRI_COUNT; i++)
					deck.add(new Nigiri(1));
				for(int i = 0; i < SALMON_NIGIRI_COUNT; i++)
					deck.add(new Nigiri(2));
				for(int i = 0; i < SQUID_NIGIRI_COUNT; i++)
					deck.add(new Nigiri(3));
			}
			if(card instanceof Maki) {
				for(int i = 0; i < MAKI_ROLL_1_COUNT; i++)
					deck.add(new Maki(1));
				for(int i = 0; i < MAKI_ROLL_2_COUNT; i++)
					deck.add(new Maki(2));
				for(int i = 0; i < MAKI_ROLL_3_COUNT; i++)
					deck.add(new Maki(3));
			}
			if(card instanceof Tempura) {
				for(int i = 0; i < TEMPURA_COUNT; i++)
					deck.add(new Tempura());
			}
			if(card instanceof Sashimi) {
				for(int i = 0; i < SASHIMI_COUNT; i++)
					deck.add(new Sashimi());
			}
			if(card instanceof Dumpling) {
				for(int i = 0; i < DUMPLING_COUNT; i++)
					deck.add(new Dumpling());
			}
			if(card instanceof Chopsticks) {
				for(int i = 0; i < CHOPSTICKS_COUNT; i++)
					deck.add(new Chopsticks());
			}
			if(card instanceof Wasabi) {
				for(int i = 0; i < WASABI_COUNT; i++)
					deck.add(new Wasabi());
			}
			if(card instanceof Pudding) {
				for(int i = 0; i < PUDDING_COUNT; i++)
					deck.add(new Pudding());
			}
		}
		shuffleDeck(this.deck);
	}
}
