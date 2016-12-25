package cards;

public class Nigiri extends Card{

	public Nigiri() {
	}
	public Nigiri(int points) {
		switch(points) {
			case 1: this.name = "Egg Nigiri";
					break;
			case 2: this.name = "Salmon Nigiri";
					break;
			case 3: this.name = "Squid Nigiri";
					break;
			default: this.name = "Nigiri";
					break;
		}
		this.value = points;
	}
}
