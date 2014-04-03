package net.zomis.fight;

import java.util.Collection;

/**
 * Class to keep track of wins, losses, and draws.
 */
public class WinsLosses {
	private int wins;
	private int losses;
	private int draws;
	
	public WinsLosses() {
		this(0, 0, 0);
	}
	
	public WinsLosses(int wins, int losses, int draws) {
		this.wins = wins;
		this.losses = losses;
		this.draws = draws;
	}
	
	public WinsLosses(Collection<WinsLosses> total) {
		this(0, 0, 0);
		for (WinsLosses winlose : total) {
			wins += winlose.wins;
			losses += winlose.losses;
			draws += winlose.draws;
		}
	}
	
	void win() {
		wins++;
	}
	
	void loss() {
		losses++;
	}
	
	public int getTotal() {
		return wins + draws + losses;
	}
	
	public int getLosses() {
		return losses;
	}
	public int getWins() {
		return wins;
	}
	public double getPercentage() {
		double drawsBonus = draws / 2.0;
		return (wins + drawsBonus) / (double) getTotal();
	}
	
	@Override
	public String toString() {
		return String.format("%d wins, %d draws, %d losses (%.2f %%)", wins, draws, losses, getPercentage() * 100);
	}
	void draw() {
		draws++;
	}
	
	public int getDraws() {
		return draws;
	}
	
}