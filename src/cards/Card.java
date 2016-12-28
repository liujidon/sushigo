package cards;

public class Card {
	public String name = "";
	public float value = 0;
	public double expValue = 0;
	
	@Override
	public String toString() {
		return String.format("[%s,%.1f]", name, value);
	}

}
