package engine;

/**
 * Created by MichaelWang on 2017-01-04.
 */

import cards.*;

import java.util.ArrayList;
import java.util.List;

public class SetEngine extends Engine {

    List<Card> set = new ArrayList<Card>();
    List<Card> setWithoutWasabi = new ArrayList<Card>();
    List<Card> setWithChopstick = new ArrayList<Card>();

    boolean usedWasabi = true;
    boolean usedChopstick = true;

    public SetEngine() {
        setWithoutWasabi.add(new Wasabi());
        setWithoutWasabi.add(new Chopsticks());
        setWithoutWasabi.add(new Nigiri(3));
        setWithoutWasabi.add(new Nigiri(2));
        setWithoutWasabi.add(new Tempura());
        setWithoutWasabi.add(new Dumpling());
        setWithoutWasabi.add(new Pudding());
        setWithoutWasabi.add(new Maki(3));
        setWithoutWasabi.add(new Maki(2));
        setWithoutWasabi.add(new Maki(1));
        setWithoutWasabi.add(new Nigiri(1));
        setWithoutWasabi.add(new Sashimi());

        set.add(new Nigiri(3));
        set.add(new Nigiri(2));
        set.add(new Tempura());
        set.add(new Pudding());
        set.add(new Dumpling());
        set.add(new Nigiri(1));
        set.add(new Maki(3));
        set.add(new Maki(2));
        set.add(new Maki(1));
        set.add(new Sashimi());
        set.add(new Chopsticks());
        set.add(new Wasabi());


        setWithChopstick.add(new Wasabi());
        setWithChopstick.add(new Nigiri(3));
        setWithChopstick.add(new Nigiri(2));
        setWithChopstick.add(new Tempura());
        setWithChopstick.add(new Dumpling());
        setWithChopstick.add(new Pudding());
        setWithChopstick.add(new Maki(3));
        setWithChopstick.add(new Maki(2));
        setWithChopstick.add(new Maki(1));
        setWithChopstick.add(new Nigiri(1));
        setWithChopstick.add(new Sashimi());
        setWithChopstick.add(new Chopsticks());

    }

    @Override
    public Card bestCardToEat(List<Card> hand) {

        if (hand.isEmpty()) {
            return null;
        }

        else if (usedWasabi == false) {
            return findNigiri(hand);


        }
        else if (usedChopstick == false) {
            return useChopstickChoice(hand);

        }else if (hand.size() >= 4 && hand.size() <= 10) {
            return withoutWasabiChoice(hand);
        } else if (hand.size() == 1) {
            usedChopstick = true;
            usedWasabi = true;
            return hand.get(0);
        }

        return findNigiri(hand);

    }

    @Override
    public boolean useChopstick(List<Card> hand, List<Card> eaten) {
        return true;
    }

    @Override
    public Card bestCardToEat(List<Card> hand, List<Card> eaten) {
        return bestCardToEat(hand);
    }

    public Card withoutWasabiChoice(List<Card> hand) {
        int cardIndex = 0;
        outer:
        for (int i = 0; i < setWithoutWasabi.size(); i++) {
            for (int j = 0; j < hand.size(); j++) {
                if (setWithoutWasabi.get(i).name.equals(hand.get(j).name)) {
                    cardIndex = j;
                    break outer;
                }
            }
        }

        if (hand.get(cardIndex) instanceof Wasabi) {
            usedWasabi = false;
        }
        if (hand.get(cardIndex) instanceof Chopsticks) {
            usedChopstick = false;
        }


        return hand.get(cardIndex);
    }


    public Card findNigiri(List<Card> hand) {
        int cardIndex = 0;
        outer:
        for (int i = 0; i < set.size(); i++) {
            for (int j = 0; j < hand.size(); j++) {
                if (set.get(i).name.equals(hand.get(j).name)) {
                    cardIndex = j;
                    break outer;
                }
            }
        }
        if (hand.get(cardIndex) instanceof Nigiri) {
            usedWasabi = true;
        }

        if (hand.get(cardIndex) instanceof Chopsticks) {
            usedChopstick = false;
        }
        return hand.get(cardIndex);
    }

    public Card useChopstickChoice(List<Card> hand) {
        int cardIndex = 0;
        outer:
        for (int i = 0; i < setWithChopstick.size(); i++) {
            for (int j = 0; j < hand.size(); j++) {
                if (setWithChopstick.get(i).name.equals(hand.get(j).name)) {
                    cardIndex = j;
                    break outer;
                }
            }
        }
        usedChopstick = true;

        if (hand.get(cardIndex) instanceof Nigiri) {
            usedWasabi = true;
        }
        if (hand.get(cardIndex) instanceof Wasabi) {
            usedWasabi = false;
        }
        if (hand.get(cardIndex) instanceof Chopsticks) {
            usedChopstick = false;
        }
            return hand.get(cardIndex);
        }


    }

