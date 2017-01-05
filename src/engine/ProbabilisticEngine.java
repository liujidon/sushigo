package engine;

import cards.*;
import game.Constants;
import game.Player;
import org.apache.commons.math3.distribution.HypergeometricDistribution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProbabilisticEngine extends Engine {
    int playerNumber = 1, cardsInPlay = 1, round = 1;
    HashMap<String, Double> totalMap = new HashMap<String, Double>();
    HashMap<String, Integer> eatenMap = new HashMap<String, Integer>();
    HashMap<String, Integer> handMap = new HashMap<String, Integer>();
    HashMap<String, Integer> seenMap = new HashMap<String, Integer>();
    HashMap<String, Double> nextHandOddsMap = new HashMap<String, Double>();

    List<Card> eaten;
    List<Card> hand;

    public ProbabilisticEngine() {
    }

    public ProbabilisticEngine(List<Player> opponents) {
        super(opponents);
        this.playerNumber = opponents.size() + 1;
        this.cardsInPlay = Constants.getCardsPerPlayer(playerNumber);
        initProbabilities();
    }

    private void initProbabilities() {
        double totalCards = (double) (Constants.TOTAL_CARDS - getSeenTotalCount());
        totalMap.put(Tempura.class.getSimpleName(), this.playerNumber * cardsInPlay * (Constants.TEMPURA_COUNT - getSeenCount(Tempura.class.getSimpleName())) / totalCards);
        totalMap.put(Chopsticks.class.getSimpleName(), this.playerNumber * cardsInPlay * (Constants.CHOPSTICKS_COUNT - getSeenCount(Chopsticks.class.getSimpleName())) / totalCards);
        totalMap.put(Dumpling.class.getSimpleName(), this.playerNumber * cardsInPlay * (Constants.DUMPLING_COUNT - getSeenCount(Dumpling.class.getSimpleName())) / totalCards);
        totalMap.put(Pudding.class.getSimpleName(), this.playerNumber * cardsInPlay * (Constants.PUDDING_COUNT - getSeenCount(Pudding.class.getSimpleName())) / totalCards);
        totalMap.put(Sashimi.class.getSimpleName(), this.playerNumber * cardsInPlay * (Constants.SASHIMI_COUNT - getSeenCount(Sashimi.class.getSimpleName())) / totalCards);
        totalMap.put(Wasabi.class.getSimpleName(), this.playerNumber * cardsInPlay * (Constants.WASABI_COUNT - getSeenCount(Wasabi.class.getSimpleName())) / totalCards);
        totalMap.put("Maki1", this.playerNumber * cardsInPlay * (Constants.MAKI_ROLL_1_COUNT - getSeenCount("Maki1")) / totalCards);
        totalMap.put("Maki2", this.playerNumber * cardsInPlay * (Constants.MAKI_ROLL_2_COUNT - getSeenCount("Maki2")) / totalCards);
        totalMap.put("Maki3", this.playerNumber * cardsInPlay * (Constants.MAKI_ROLL_3_COUNT - getSeenCount("Maki3")) / totalCards);
        totalMap.put(Nigiri.EGG, this.playerNumber * cardsInPlay * (Constants.EGG_NIGIRI_COUNT - getSeenCount(Nigiri.EGG)) / totalCards);
        totalMap.put(Nigiri.SALMON, this.playerNumber * cardsInPlay * (Constants.SALMON_NIGIRI_COUNT - getSeenCount(Nigiri.SALMON)) / totalCards);
        totalMap.put(Nigiri.SQUID, this.playerNumber * cardsInPlay * (Constants.SQUID_NIGIRI_COUNT - getSeenCount(Nigiri.SQUID)) / totalCards);
    }

    private void buildHandMap() {
        handMap.clear();
        for (Card c : hand) {
            int count = handMap.containsKey(c.name) ? handMap.get(c.name) : 0;
            handMap.put(c.name, count + 1);
        }
    }


    private void buildEatenMap() {
        HashMap<String, Integer> lastTurnEaten = new HashMap<String, Integer>();
        lastTurnEaten.putAll(eatenMap);
        eatenMap.clear();
        for (Card e : eaten) {
            String itemName = e.name;
            int count = eatenMap.containsKey(itemName) ? eatenMap.get(itemName) + 1 : 1;
            eatenMap.put(itemName, count);
        }
        for (Player opponent : opponents) {
            for (Card e : opponent.eaten) {
                String itemName = e.name;
                int count = eatenMap.containsKey(itemName) ? eatenMap.get(itemName) + 1 : 1;
                eatenMap.put(itemName, count);
            }
        }

        //new round
        if (eaten.isEmpty() && !lastTurnEaten.isEmpty()) {
            round++;
            if (round == 2) {
                seenMap.putAll(lastTurnEaten);
            } else if (round == 3) {
                for (Map.Entry<String, Integer> entry : lastTurnEaten.entrySet()) {
                    if (seenMap.containsKey(entry.getKey()))
                        seenMap.put(entry.getKey(), seenMap.get(entry.getKey()) + entry.getValue());
                    else
                        seenMap.put(entry.getKey(), entry.getValue());
                }

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

            int nextHandSize = hand.size() - 1;
            int totalCardsRemaining = nextHandSize * (playerNumber - 1);
            int thisCardRemaining = (int) (Math.round(total) - seenCount - handCount);
            thisCardRemaining = Math.min(totalCardsRemaining, Math.max(thisCardRemaining, 0));

            HypergeometricDistribution hypDist = new HypergeometricDistribution(totalCardsRemaining, thisCardRemaining, nextHandSize);

            //drawing at least 1
            nextHandOddsMap.put(name, 1 - hypDist.probability(0));
        }

    }

    private Double getNextHandOdds(String name) {
        return nextHandOddsMap.containsKey(name) ? nextHandOddsMap.get(name) : 0.0;
    }

    private Integer getSeenTotalCount() {
        int total = 0;
        for (Map.Entry<String, Integer> entry : seenMap.entrySet())
            total += entry.getValue();
        return total;
    }

    private Integer getSeenCount(String name) {
        return seenMap.containsKey(name) ? seenMap.get(name) : 0;
    }

    @Override
    public Card bestCardToEat(List<Card> hand) {
        return null;
    }

    @Override
    public Card bestCardToEat(List<Card> hand, List<Card> eaten) {
        if (hand.isEmpty())
            return null;
        else if (hand.size() == 1)
            return hand.get(0);

        this.eaten = eaten;
        this.hand = hand;
        buildEatenMap();
        buildHandMap();
        initProbabilities();
        buildOddsMap();

        //Map<String, Double> sortedBest = Helpers.sortByValue(nextHandOddsMap);
        //System.out.print("Odds:");
        //for (Map.Entry<String, Double> entry : sortedBest.entrySet()) {
        // System.out.print(String.format("[%s,%.2f] ", entry.getKey(), entry.getValue()));
        //}
        //System.out.println();

        calculateExpValue(hand, eaten);

        Card bestCard = null;
        //System.out.print("ExVal:");
        for (Card h : hand) {
            if (bestCard == null || h.expValue > bestCard.expValue)
                bestCard = h;
            //System.out.print(String.format("[%s,%.2f] ", h.name, h.expValue));
        }
        //System.out.println();

        if (bestCard != null)
            return bestCard;

        return hand.get(0);
    }

    private void calculateExpValue(List<Card> hand, List<Card> eaten) {
        for (Card h : hand) {
            if (h instanceof Tempura) {
                int count = 0;
                for (Card c : eaten)
                    if (c instanceof Tempura)
                        count++;
                int remainder = count % Tempura.SET;
                if (remainder == (Tempura.SET - 1))
                    h.expValue = Tempura.SCORE;
                else
                    h.expValue = getNextHandOdds(h.name) * Tempura.SCORE / Tempura.SET;

            } else if (h instanceof Sashimi) {
                int count = 0;
                for (Card c : eaten)
                    if (c instanceof Sashimi)
                        count++;

                int remainder = count % Sashimi.SET;
                if (remainder == (Sashimi.SET - 1))
                    h.expValue = Sashimi.SCORE;
                else if (remainder == (Sashimi.SET - 2))
                    h.expValue = (getNextHandOdds(h.name) * (Sashimi.SET - 1) * Sashimi.SCORE / Sashimi.SET);
                else
                    h.expValue = getNextHandOdds(h.name) * Sashimi.SCORE / Sashimi.SET;
            } else if (h instanceof Wasabi) {
                //TODO subtract overlapping P
                h.expValue = (0.5 * getNextHandOdds(Nigiri.SQUID) * 3 * Wasabi.SCORE) +
                        (0.5 * getNextHandOdds(Nigiri.SALMON) * 2 * Wasabi.SCORE) +
                        (0.5 * getNextHandOdds(Nigiri.EGG) * 1 * Wasabi.SCORE);
            } else if (h instanceof Dumpling) {
                int count = 1;
                for (Card c : eaten)
                    if (c instanceof Dumpling)
                        count++;
                if (count >= 5)
                    h.expValue = 0.0;
                else
                    h.expValue = Math.max((getNextHandOdds(h.name) * (count + 1) + count) / 2, count);
            } else if (h instanceof Pudding) {
                int count = 0;
                int minOpponent = Integer.MAX_VALUE;
                int maxOpponent = 0;

                for (Card c : eaten) {
                    if (c instanceof Pudding) {
                        count++;
                    }
                }
                for (Player opponent : opponents) {
                    int currCount = 0;
                    for (Card e : opponent.eaten) {
                        if (e instanceof Pudding) {
                            currCount++;
                        }

                    }
                    if (currCount > maxOpponent) {
                        maxOpponent = currCount;
                    }
                    if (currCount < minOpponent) {
                        minOpponent = currCount;
                    }
                }
                if (count >= maxOpponent) {
                    h.expValue = 0.0;
                } else if (count == maxOpponent - 1 | count == minOpponent | count == minOpponent - 1 ) {
                    h.expValue = Pudding.FIRST_PLACE_SCORE;
                } else if(count < minOpponent){
                    h.expValue = getNextHandOdds(h.name) * 6/ (minOpponent - count);

                }


            } else if (h instanceof Maki) {
                //TODO, check other players
            } else if (h instanceof Nigiri) {
                boolean unusedWasabi = false;
                for (Card c : eaten)
                    if (c instanceof Wasabi)
                        unusedWasabi = true;
                    else if (c instanceof Nigiri)
                        unusedWasabi = false;
                if (unusedWasabi) {
                    h.expValue = h.value * Wasabi.SCORE;
                    //should wait for next round?
                    if (h.name.equals(Nigiri.EGG))
                        h.expValue -= getNextHandOdds(Nigiri.SALMON) * Wasabi.SCORE;
                    else if (h.name.equals(Nigiri.SALMON))
                        h.expValue -= getNextHandOdds(Nigiri.SQUID) * Wasabi.SCORE;
                } else
                    h.expValue = h.value;
            } else {
                h.expValue = h.value;
            }
        }
    }

    @Override
    public boolean useChopstick(List<Card> hand, List<Card> eaten) {
        return true;
    }

}
