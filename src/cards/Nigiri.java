package cards;

public class Nigiri extends Card{

	public int points = 1;
	public Nigiri(int points) {
		this.name = "Nigiri " + points;
		this.points = points;
	}
}
