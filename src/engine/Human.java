package engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import cards.Card;

public class Human extends Engine{
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	@Override
	public Card bestCardToEat(List<Card> hand, List<Card> eaten) {
		if(hand.size() == 0)
			return null;
		for(int i = 1; i <= hand.size(); i++) {
			System.out.print(i + " " + hand.get(i-1) + ", ");
		}
		System.out.println();
		String response = "?";
	    while(response.equals("?") || !isValidResponse(response, hand)) {
	    	System.out.print("Pick a card or (?) to show eaten: ");
	    	try {
	    		response = br.readLine();
	    		if(response.equals("?")) {
	    			System.out.print("Eaten: ");
	    			for(Card e : eaten)
	    				System.out.print(e + " ");
	    			System.out.println();
	    		}
	    			
	    	} catch(Exception e) {
				System.out.print("Invalid entry, must be a number or ?");
	    	}
	    }
	    
	    try {
	    	int cardSelected = Integer.parseInt(response) -1;
	    	return hand.get(cardSelected);
		} catch (Exception e) {
			System.out.print("Can't find card to eat!");
		}
		return null;
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

}
