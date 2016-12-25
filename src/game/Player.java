package game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import cards.Card;
import cards.Dumpling;
import cards.Maki;
import cards.Pudding;
import cards.Sashimi;
import cards.Tempura;
import engine.Engine;
import engine.SimpleEngine;

public class Player implements Comparable<Player> {

	String name;
	public List<Card> hand = new ArrayList<Card>();
	public List<Card> eaten = new ArrayList<Card>();
	public Player leftPlayer;
	public Player rightPlayer;
	public Engine engine = new SimpleEngine();
	public int score = 0;
	public int makiCount = 0, makiRank = 0;
	public int puddingCount = 0, puddingRank = 0;
	public int tempuraCount = 0, sashimiCount = 0, dumplingCount = 0;
	
	public Player(String name) {
		this.name = name;
	}
	
	public void newRound() {
		hand.clear();
		for (Iterator<Card> iterator = eaten.iterator(); iterator.hasNext(); ) {
			Card c = iterator.next();
			if(c instanceof Pudding == false)
				iterator.remove();
		}
		makiCount = 0;
		makiRank = 0;
		tempuraCount = 0;
		sashimiCount = 0;
		dumplingCount = 0;
	}
	
	public void makeMove() {
		Card cardEaten = engine.bestCardToEat(hand, eaten);
		if(cardEaten == null)
			return;
		eaten.add(cardEaten);
		hand.remove(cardEaten);
		if(cardEaten instanceof Maki)
			makiCount += ((Maki) cardEaten).weight;
		else if(cardEaten instanceof Pudding)
			puddingCount++;
		else if(cardEaten instanceof Dumpling) {
			dumplingCount++;
			((Dumpling) cardEaten).setDumpingScore(dumplingCount);
		}
		else if(cardEaten instanceof Sashimi)
			sashimiCount++;
		else if(cardEaten instanceof Tempura)
			tempuraCount++;
	}
	
	public void printHand() {
		System.out.print(name + " hand:");
		for(Card c : hand)
			System.out.print(c + " ");
	}
	
	public void printEaten() {
		System.out.print(String.format("%s Score:(%d), puddingRank=%d, makiRank=%d: ", name, score, puddingRank, makiRank));
		for(Card c : eaten)
			System.out.print(c + " ");
		
	}

	@Override
	public int compareTo(Player o) {
		return this.name.compareTo(o.name);
	}
	public static final Comparator<Player> MAKI_COMPARE = new Comparator<Player>() {
		@Override
		public int compare(Player p1, Player p2) {
			return p2.makiCount - p1.makiCount;
		}
	};
	public static final Comparator<Player> SCORE_COMPARE = new Comparator<Player>() {
		@Override
		public int compare(Player p1, Player p2) {
			return p2.score - p1.score;
		}
	};
	public static final Comparator<Player> PUDDING_COMPARE = new Comparator<Player>() {
		@Override
		public int compare(Player p1, Player p2) {
			return p2.puddingCount - p1.puddingCount;
		}
	};

	@Override
	public String toString() {
		return "Player [name=" + name + ", hand=" + hand + ", eaten=" + eaten
				+ ", score=" + score + ", makiCount="
				+ makiCount + ", makiRank=" + makiRank + ", puddingCount="
				+ puddingCount + ", puddingRank=" + puddingRank
				+ ", tempuraCount=" + tempuraCount + ", sashimiCount="
				+ sashimiCount + ", dumplingCount=" + dumplingCount + "]";
	}
	
}
