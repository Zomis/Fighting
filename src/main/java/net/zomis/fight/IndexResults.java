package net.zomis.fight;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The results for when a fighter is at a specific index
 */
public class IndexResults<T> {
	private final Map<T, WinsLosses> results = new HashMap<T, WinsLosses>();
	
	void informAbout(T opponent, Boolean winner) {
		WinsLosses winLoss = results.computeIfAbsent(opponent, fn -> new WinsLosses());
		if (winner == null) {
			winLoss.draw();
		}
		else if (winner)
			winLoss.win();
		else winLoss.loss();
	}
	
	public String toStringMultiLine() {
		StringBuilder str = new StringBuilder();
		for (Entry<T, WinsLosses> ee : results.entrySet()) {
			str.append("vs. ");
			str.append(ee.getKey());
			str.append(": ");
			str.append(ee.getValue());
			str.append("\n");
		}
		return str.toString();
	}
	
	@Override
	public String toString() {
		return results.toString();
	}

	public WinsLosses calcTotal() {
		return new WinsLosses(results.values());
	}
}