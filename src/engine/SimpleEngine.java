package engine;

import java.util.List;
import java.util.Random;

import cards.Card;

public class SimpleEngine extends Engine {
    private Random randomGenerator = new Random();

	@Override
	public Card bestCardToEat(List<Card> hand) {
        return hand.size() != 0 ? hand.get(randomGenerator.nextInt(hand.size())) : null;
	}

	@Override
	public Card bestCardToEat(List<Card> hand, List<Card> eaten) {
		return bestCardToEat(hand);
	}

}
