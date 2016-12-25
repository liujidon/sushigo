package cards;

public class Dumpling extends Card {

	public Dumpling() {
		this.name = "Dumpling";
	}
	public void setDumpingScore(int count) {
		if(count <= 5)
			super.value = count;
	}
}
