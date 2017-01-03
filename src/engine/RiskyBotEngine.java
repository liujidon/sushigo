package engine;

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

public class RiskyBotEngine extends Engine{
	List<Card> pref = new ArrayList<Card>();
	
	public RiskyBotEngine() {
		
		pref.add(new Sashimi());
		pref.add(new Tempura());
		pref.add(new Dumpling());
		pref.add(new Chopsticks());
		pref.add(new Nigiri(3));
		pref.add(new Nigiri(2));
	    pref.add(new Nigiri(1));
	    pref.add(new Pudding());
	    pref.add(new Maki(3));
	    pref.add(new Maki(2));
	    pref.add(new Maki(1));
		pref.add(new Wasabi());
	}
	
	@Override
	public Card bestCardToEat(List<Card> hand) {
		if(hand.isEmpty())
			return null;
		List<Integer> handRank = new ArrayList<Integer>();
		for(Card c : hand) {
			for(int i = 0; i < pref.size(); i++) {
				if(pref.get(i).name.equals(c.name))
					handRank.add(i);
			}
		}
		int bestCardIndex = 0;
		int bestRank = Integer.MAX_VALUE;
		for(int i = 0; i < handRank.size(); i++) {
			if(handRank.get(i) < bestRank) {
				bestCardIndex = i;
				bestRank = handRank.get(i);
			}
		}
		bestCardIndex = Math.min(bestCardIndex, hand.size()-1);
		return hand.get(bestCardIndex);
	}

	@Override
	public Card bestCardToEat(List<Card> hand, List<Card> eaten) {
		return bestCardToEat(hand);
	}

	@Override
	public boolean useChopstick(List<Card> hand, List<Card> eaten) {
		return true;
	}

}
