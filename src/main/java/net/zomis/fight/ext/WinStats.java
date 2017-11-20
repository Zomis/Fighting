package net.zomis.fight.ext;


/**
 * Class to keep track of wins, losses, and draws.
 */
public class WinStats implements Comparable<WinStats> {
	private int wins;
	private int losses;
	private int draws;
	
	public WinStats() {
		this(0, 0, 0);
	}
	
	public WinStats(int wins, int losses, int draws) {
		this.wins = wins;
		this.losses = losses;
		this.draws = draws;
	}
	
	public WinStats copyWith(WinStats combineWith) {
		return new WinStats(wins + combineWith.wins, losses + combineWith.losses, draws + combineWith.draws);
	}
	
	public void combine(WinStats combineWith) {
		wins += combineWith.wins;
		losses += combineWith.losses;
		draws += combineWith.draws;
	}
	
	public void add(WinResult result) {
		if (result == null)
			throw new NullPointerException("Result may not be null");
		switch (result) {
			case WIN:
				wins++;
				break;
			case LOSS:
				losses++;
				break;
			case DRAW:
				draws++;
				break;
			default:
				throw new IllegalArgumentException("Illegal argument for result");
		}
	}
	
	public int getTotal() {
		return wins + draws + losses;
	}
	
	public int getWins() {
		return wins;
	}
	
	public int getLosses() {
		return losses;
	}
	
	public int getDraws() {
		return draws;
	}
	
	public double getPercentage() {
		double drawsBonus = draws / 2.0;
		return (wins + drawsBonus) / (double) getTotal();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		WinStats winStats = (WinStats) o;

		if (wins != winStats.wins) return false;
		if (losses != winStats.losses) return false;
		return draws == winStats.draws;

	}

	@Override
	public int hashCode() {
		int result = wins;
		result = 31 * result + losses;
		result = 31 * result + draws;
		return result;
	}

	@Override
	public String toString() {
		return String.format("%d: %d/%d/%d (%.2f %%)", getTotal(), wins, draws, losses, getPercentage() * 100);
	}

	@Override
	public int compareTo(WinStats other) {
		return Double.compare(getPercentage(), other.getPercentage());
	}
	
}