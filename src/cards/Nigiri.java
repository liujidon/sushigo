package cards;

public class Nigiri extends Card{
	public static final String EGG = "Egg Nigiri";
	public static final String SALMON = "Salmon Nigiri";
	public static final String SQUID = "Squid Nigiri";
	public Nigiri() {
	}
	public Nigiri(int points) {
		switch(points) {
			case 1: this.name = EGG;
					break;
			case 2: this.name = SALMON;
					break;
			case 3: this.name = SQUID;
					break;
			default: this.name = "Nigiri";
					break;
		}
		this.value = points;
	}
}
