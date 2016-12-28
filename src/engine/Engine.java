package engine;

import game.Player;

import java.util.ArrayList;
import java.util.List;

import cards.Card;

public abstract class Engine {
	public abstract Card bestCardToEat(List<Card> hand);
	public abstract Card bestCardToEat(List<Card> hand, List<Card> eaten);
	public abstract boolean useChopstick(List<Card> hand, List<Card> eaten);

	public List<Player> opponents = new ArrayList<Player>();
	public Engine(List<Player> opponents) {
		this.opponents = opponents;
	}
	public Engine() {}
}
