package net.zomis.fight;

import java.util.Collection;

public class WinsLosses {
	private int wins;
	private int losses;
	
	public WinsLosses() {
		this(0, 0);
	}
	public WinsLosses(int wins, int losses) {
		this.wins = wins;
		this.losses = losses;
	}
	public WinsLosses(Collection<WinsLosses> total) {
		this(0, 0);
		for (WinsLosses winlose : total) {
			wins += winlose.wins;
			losses += winlose.losses;
		}
	}
	
	void win() {
		wins++;
	}
	
	void loss() {
		losses++;
	}
	
	public int getTotal() {
		return wins + losses;
	}
	
	public int getLosses() {
		return losses;
	}
	public int getWins() {
		return wins;
	}
	public double getPercentage() {
		return wins / (double) getTotal();
	}
	
	@Override
	public String toString() {
		return String.format("%d wins, %d losses (%.2f %%)", wins, losses, getPercentage() * 100);
	}
	
}