package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
import engine.Human;
import engine.ProbabilisticEngine;

public class Board {

	LinkedList<Card> deck = new LinkedList<Card>();
	LinkedList<Card> round1Deck = new LinkedList<Card>();
	LinkedList<Card> round2Deck = new LinkedList<Card>();
	LinkedList<Card> round3Deck = new LinkedList<Card>();
	
	List<Player> players = new ArrayList<Player>();
	
	public int playerCount = 0;
	public int currentRound = 0;
	public int cardsPerPlayer = 9;
	
	public Board(int playerNumber, List<Card> mode) {
		if(playerNumber < 2) {
			System.out.print("Must require at least 2 players.");
			return;
		}
		this.playerCount = playerNumber;
		this.cardsPerPlayer = Constants.getCardsPerPlayer(playerNumber);
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
	
	public Player selectHumanPlayer(int playerNumber) {
		Player human = players.get(playerNumber);
		human.engine = new Human();
		human.name = "Human Player";
		System.out.println("You are " + human.name);
		return human;
	}
	
	public Player selectProbEnginePlayer(int playerNumber) {
		if(playerNumber >= players.size())
			return null;
		Player probPlayer = players.get(playerNumber);
		List<Player> opp = new ArrayList<Player>();
		for(int i = 0; i < players.size(); i++) {
			if(i != playerNumber)
				opp.add(players.get(i));
		}
		probPlayer.engine = new ProbabilisticEngine(opp);
		probPlayer.name = probPlayer.engine.getClass().getSimpleName();
		return probPlayer;
	}
	public void takeTurn() {
		for(Player player : players)
			player.makeMove();
		if(players.get(0).hand.isEmpty()) {
			return;
		}
		//pass on the cards
		List<Card> tempHand = players.get(0).hand;
		for(int i = 0; i < players.size() - 1; i++) {
			players.get(i).hand = players.get(i).rightPlayer.hand;
		}
		players.get(players.size()-1).hand = tempHand;
	}
	
	public void newRound() {
		currentRound++;
		System.out.println("Round " + currentRound);
		LinkedList<Card> currentDeck = null;
		if(currentRound == 1)
			currentDeck = round1Deck;
		else if(currentRound == 2)
			currentDeck = round2Deck;
		else if(currentRound == 3)
			currentDeck = round3Deck;
		else
			return;
		for(Player p : players) {
			p.newRound();
			for(int i = 0; i < cardsPerPlayer; i++) {
				p.hand.add(currentDeck.pop());
			}
		}
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
			lastMakiCount = p.makiCount;
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
					c.value = makiScore * ((Maki) c).weight/(float)p.makiCount;
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
			lastPuddingCount = p.puddingCount;
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
					c.value = puddingScore / (float) p.puddingCount;
			}
		}
		//add up scores
		for(Player p : players) {
			for(Card c : p.eaten) {
				if(c instanceof Pudding == false || currentRound == 3)
					p.score += c.value;
			}
			//account for no pudding card
			if(p.puddingCount == 0)
				p.score -= Pudding.LAST_PLACE_SCORE/lastPlaceCount;
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
		int cardDistributed = 0;
		for (Iterator<Card> iterator = deck.iterator(); iterator.hasNext(); ) {
			if(cardDistributed < cardsPerPlayer * playerCount)
				round1Deck.add(deck.pop());
			else if(cardDistributed < 2 * cardsPerPlayer * playerCount)
				round2Deck.add(deck.pop());
			else if(cardDistributed < 3 * cardsPerPlayer * playerCount)
				round3Deck.add(deck.pop());
			else
				break;
			cardDistributed++;
		}
		newRound();
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
				for(int i = 0; i < Constants.EGG_NIGIRI_COUNT; i++)
					deck.add(new Nigiri(1));
				for(int i = 0; i < Constants.SALMON_NIGIRI_COUNT; i++)
					deck.add(new Nigiri(2));
				for(int i = 0; i < Constants.SQUID_NIGIRI_COUNT; i++)
					deck.add(new Nigiri(3));
			}
			if(card instanceof Maki) {
				for(int i = 0; i < Constants.MAKI_ROLL_1_COUNT; i++)
					deck.add(new Maki(1));
				for(int i = 0; i < Constants.MAKI_ROLL_2_COUNT; i++)
					deck.add(new Maki(2));
				for(int i = 0; i < Constants.MAKI_ROLL_3_COUNT; i++)
					deck.add(new Maki(3));
			}
			if(card instanceof Tempura) {
				for(int i = 0; i < Constants.TEMPURA_COUNT; i++)
					deck.add(new Tempura());
			}
			if(card instanceof Sashimi) {
				for(int i = 0; i < Constants.SASHIMI_COUNT; i++)
					deck.add(new Sashimi());
			}
			if(card instanceof Dumpling) {
				for(int i = 0; i < Constants.DUMPLING_COUNT; i++)
					deck.add(new Dumpling());
			}
			if(card instanceof Chopsticks) {
				for(int i = 0; i < Constants.CHOPSTICKS_COUNT; i++)
					deck.add(new Chopsticks());
			}
			if(card instanceof Wasabi) {
				for(int i = 0; i < Constants.WASABI_COUNT; i++)
					deck.add(new Wasabi());
			}
			if(card instanceof Pudding) {
				for(int i = 0; i < Constants.PUDDING_COUNT; i++)
					deck.add(new Pudding());
			}
		}
		shuffleDeck(this.deck);
	}
}
