package net.zomis.fight;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PlayerResults<T> implements Comparable<PlayerResults<T>> {
	private final T player;

	public T getPlayer() {
		return player;
	}
	PlayerResults(T player) {
		this.player = player;
	}

	private final Map<Integer, IndexResults<T>> results = new HashMap<Integer, IndexResults<T>>();

	void addResult(int myIndex, T opponent, T winner) {
		IndexResults<T> result = results.computeIfAbsent(myIndex, i -> new IndexResults<T>());
		Boolean winStatus = null;
		if (winner == this.player) // allow for drawed games as winner = null.
			winStatus = true;
		else if (winner != null)
			winStatus = false;
		result.informAbout(opponent, winStatus);
	}
	
	public String toStringMultiLine() {
		StringBuilder str = new StringBuilder();
		for (Entry<Integer, IndexResults<T>> ee : results.entrySet()) {
			str.append("as index ");
			str.append(ee.getKey());
			str.append(": (");
			str.append(ee.getValue().calcTotal());
			str.append(")\n");
			str.append(ee.getValue().toStringMultiLine());
		}
		return str.toString();
	}
	@Override
	public String toString() {
		return results.toString();
	}
	
	public WinsLosses calcTotal() {
		int wins = 0;
		int losses = 0;
		int draws = 0;
		for (Entry<Integer, IndexResults<T>> ee : results.entrySet()) {
			WinsLosses tot = ee.getValue().calcTotal();
			wins += tot.getWins();
			draws = tot.getDraws();
			losses += tot.getLosses();
		}
		return new WinsLosses(wins, losses, draws);
	}
	
	public double calculatePercentage() {
		return calcTotal().getPercentage();
	}
	
	@Override
	public int compareTo(PlayerResults<T> o) {
		return Double.compare(this.calculatePercentage(), o.calculatePercentage());
	}
}