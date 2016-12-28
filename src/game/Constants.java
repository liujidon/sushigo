package game;

public class Constants {
	public static final int SALMON_NIGIRI_COUNT = 10;
	public static final int SQUID_NIGIRI_COUNT = 5;
	public static final int EGG_NIGIRI_COUNT = 5;
	
	public static final int MAKI_ROLL_1_COUNT = 6;
	public static final int MAKI_ROLL_2_COUNT = 12;
	public static final int MAKI_ROLL_3_COUNT = 8;
	
	public static final int TEMPURA_COUNT = 14;
	public static final int SASHIMI_COUNT = 14;
	public static final int DUMPLING_COUNT = 14;
	public static final int PUDDING_COUNT = 10;
	public static final int WASABI_COUNT = 6;
	public static final int CHOPSTICKS_COUNT = 4;
	
	public static final int TOTAL_CARDS = 108;
	
	public static int getCardsPerPlayer(int playerNumber) {
		switch(playerNumber) {
			case 2:	return 10;
			case 3: return 9;
			case 4: return 8;
			case 5: return 7;
			default: return 7;
		}
	}
}
