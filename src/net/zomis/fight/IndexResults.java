package net.zomis.fight;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The results for when a fighter is at a specific index
 */
public class IndexResults<PL> {
	private final Map<PL, WinsLosses> results = new HashMap<PL, WinsLosses>();
	
	void informAbout(PL opponent, Boolean winner) {
		WinsLosses winLoss = results.get(opponent);
		if (winLoss == null) {
			winLoss = new WinsLosses();
			results.put(opponent, winLoss);
		}
		if (winner == null) {
			winLoss.draw();
		}
		else if (winner)
			winLoss.win();
		else winLoss.loss();
	}
	
	public String toStringMultiLine() {
		StringBuilder str = new StringBuilder();
		for (Entry<PL, WinsLosses> ee : results.entrySet()) {
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