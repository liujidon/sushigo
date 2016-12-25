package cards;

public class Maki extends Card{
	public final static int FIRST_PLACE_SCORE = 6;
	public final static int SECOND_PLACE_SCORE = 3;
	public Maki() {}
	public int weight = 1;
	public Maki(int weight) {
		this.name = "Maki" + weight;
		this.weight = weight;
	}
}
