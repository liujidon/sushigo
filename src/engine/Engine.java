package engine;

import java.util.List;

import cards.Card;

public abstract class Engine {
	public abstract Card bestCardToEat(List<Card> hand);
}