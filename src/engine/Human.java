package engine;

import game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import cards.Card;

public class Human extends Engine{
	public Human(List<Player> opponents) {
		super(opponents);
	}

	public Human() {}
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	final String operatingSystem = System.getProperty("os.name");

	private void clearConsole() {
		if (operatingSystem.contains("Windows")) {
		    try {
				Runtime.getRuntime().exec("cls");
			} catch (IOException e) {
				for(int i=0; i<1000; i++) System.out.println();
			}
		}
		else {
			for(int i=0; i<1000; i++) System.out.println();
		}
	}
	
	private void printHand(List<Card> hand) {
		System.out.print("Your Hand: ");
		for(int i = 1; i <= hand.size(); i++) {
			System.out.print(i + "->" + hand.get(i-1) + " ");
		}
		System.out.println();
	}
	
	private void printEaten(List<Card> eaten) {
		for(Player opp : opponents) {
			System.out.print(opp.name + " Eaten:");
			for(Card e : opp.eaten)
				System.out.print(e + " ");
			System.out.println();
		}
		
		System.out.print("You Eaten: ");
		for(Card e : eaten)
			System.out.print(e + " ");
		System.out.println();
		System.out.println();
		System.out.println();
	}
	
	@Override
	public Card bestCardToEat(List<Card> hand, List<Card> eaten) {
		if(hand.size() == 0)
			return null;
		printEaten(eaten);
		printHand(hand);
		String response = "?";
	    while(isHelpCommand(response) || !isValidResponse(response, hand)) {
	    	System.out.print("Pick a card number or (?) to show eaten: ");
	    	try {
	    		response = br.readLine();
	    		if(response.equals("?")) {
	    			printEaten(eaten);
	    		} else if(response.equals("h")) {
	    			printHand(hand);
	    		}
	    	} catch(Exception e) {
				System.out.print("Invalid entry, must be a number or ?");
	    	}
	    }
	    
	    try {
			clearConsole();
	    	int cardSelected = Integer.parseInt(response) -1;
	    	return hand.get(cardSelected);
		} catch (Exception e) {
			System.out.print("Can't find card to eat!");
		}
		return null;
	}
	
	private boolean isHelpCommand(String response) {
		return response.isEmpty() || response.equals("?") || response.equals("h");
	}
	
	private boolean isValidResponse(String response, List<Card> hand) {
		try {
	    	int cardSelected = Integer.parseInt(response) - 1;
	    	if(hand.size() > cardSelected && cardSelected >= 0)
	    		return true;
	    	else
	    		System.out.println("Number must be less than " + (hand.size()+1));
		} catch (Exception e) {
			System.out.print("Invalid entry, must be a number");
		}
		return false;
	}

	@Override
	public Card bestCardToEat(List<Card> hand) {
		return null;
	}

	@Override
	public boolean useChopstick(List<Card> hand, List<Card> eaten) {
		String response = "?";
		printEaten(eaten);
		printHand(hand);
	    System.out.print("(y) to use chopstick, (return) to save for later: ");
	    try {
	    	response = br.readLine();
	    	if(response.equalsIgnoreCase("y"))
	    		return true;
	    	else
	    		return false;
	    } catch (Exception e) {
			System.out.print("Invalid entry... not using chopsticks");
	    }
		return false;
	}

}
