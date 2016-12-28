package engine;

import game.Constants;
import game.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utis.Helpers;
import cards.Card;
import cards.Chopsticks;
import cards.Dumpling;
import cards.Nigiri;
import cards.Pudding;
import cards.Sashimi;
import cards.Tempura;
import cards.Wasabi;

public class ProbabilisticEngine extends Engine {
	double expectedTotalTempura = 0;
	int playerNumber = 1, cardsInPlay = 1;
	HashMap<String, Double> totalMap = new HashMap<String, Double>();
	HashMap<String, Integer> eatenMap = new HashMap<String, Integer>();
	HashMap<String, Integer> handMap = new HashMap<String, Integer>();
	HashMap<String, Double> nextHandOddsMap = new HashMap<String, Double>();
	
	List<Card> eaten;
	List<Card> hand;
	public ProbabilisticEngine() {}
	public ProbabilisticEngine(List<Player> opponents) {
		super(opponents);
		this.playerNumber = opponents.size() + 1;
		this.cardsInPlay = Constants.getCardsPerPlayer(playerNumber);
		initProbabilities();
	}
	
	private void initProbabilities() {
		totalMap.put(Tempura.class.getSimpleName(), cardsInPlay * Constants.TEMPURA_COUNT/(double) Constants.TOTAL_CARDS);
		totalMap.put(Chopsticks.class.getSimpleName(), cardsInPlay * Constants.CHOPSTICKS_COUNT/(double) Constants.TOTAL_CARDS);
		totalMap.put(Dumpling.class.getSimpleName(), cardsInPlay * Constants.DUMPLING_COUNT/(double) Constants.TOTAL_CARDS);
		totalMap.put(Pudding.class.getSimpleName(), cardsInPlay * Constants.PUDDING_COUNT/(double) Constants.TOTAL_CARDS);
		totalMap.put(Sashimi.class.getSimpleName(), cardsInPlay * Constants.SASHIMI_COUNT/(double) Constants.TOTAL_CARDS);
		totalMap.put(Wasabi.class.getSimpleName(), cardsInPlay * Constants.WASABI_COUNT/(double) Constants.TOTAL_CARDS);
		totalMap.put("Maki1", cardsInPlay * Constants.MAKI_ROLL_1_COUNT/(double) Constants.TOTAL_CARDS);
		totalMap.put("Maki2", cardsInPlay * Constants.MAKI_ROLL_2_COUNT/(double) Constants.TOTAL_CARDS);
		totalMap.put("Maki3", cardsInPlay * Constants.MAKI_ROLL_3_COUNT/(double) Constants.TOTAL_CARDS);
		totalMap.put(Nigiri.EGG, cardsInPlay * Constants.EGG_NIGIRI_COUNT/(double) Constants.TOTAL_CARDS);
		totalMap.put(Nigiri.SALMON, cardsInPlay * Constants.SALMON_NIGIRI_COUNT/(double) Constants.TOTAL_CARDS);
		totalMap.put(Nigiri.SQUID, cardsInPlay * Constants.SQUID_NIGIRI_COUNT/(double) Constants.TOTAL_CARDS);
	}
	
	private void buildHandMap() {
		handMap.clear();
		for(Card c : hand) {
			int count = handMap.containsKey(c.name) ? handMap.get(c.name) : 0;
			handMap.put(c.name, count + 1);
		}
	}
	
	private void buildEatenMap() {
		eatenMap.clear();
		for(Card e : eaten) {
			String itemName = e.name;
			int count = eatenMap.containsKey(itemName) ? eatenMap.get(itemName) + 1 : 1;
			eatenMap.put(itemName, count);
		}
		for(Player opponent : opponents) {
			for(Card e : opponent.eaten) {
				String itemName = e.getClass().getSimpleName();
				int count = eatenMap.containsKey(itemName) ? eatenMap.get(itemName) + 1 : 1;
				eatenMap.put(itemName, count);
			}
		}
	}
	
	private void buildOddsMap() {
		nextHandOddsMap.clear();
		for (Map.Entry<String, Double> entry : totalMap.entrySet()) {
			String name = entry.getKey();
			Double total = entry.getValue();
			int seenCount = eatenMap.containsKey(name) ? eatenMap.get(name) : 0;
			int handCount = handMap.containsKey(name) ? handMap.get(name) : 0;
			double odds = total-seenCount-handCount;
			nextHandOddsMap.put(name, (hand.size()-1)*odds);
		}

	}
	
	@Override
	public Card bestCardToEat(List<Card> hand) {
		return null;
	}

	@Override
	public Card bestCardToEat(List<Card> hand, List<Card> eaten) {
		if(hand.isEmpty())
			return null;
		this.eaten = eaten;
		this.hand = hand;
		buildEatenMap();
		buildHandMap();
		buildOddsMap();
		
		Map<String, Double> sortedBest = Helpers.sortByValue(nextHandOddsMap);
		for (Map.Entry<String, Double> entry : sortedBest.entrySet()) {
	        System.out.print(String.format("[%s,%.2f] ", entry.getKey(), entry.getValue()));
	    }
		System.out.println();
		for (Map.Entry<String, Double> entry : sortedBest.entrySet()) {
			for(Card h : hand) {
				if(h.name.equals(entry.getKey()))
					return h;
			}
        }
		
		return hand.get(0);
	}
	@Override
	public boolean useChopstick(List<Card> hand, List<Card> eaten) {
		return true;
	}

}
