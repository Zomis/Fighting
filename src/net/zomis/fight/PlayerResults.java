package net.zomis.fight;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;

public class PlayerResults<PL> implements Comparable<PlayerResults<PL>> {
	private final PL player;

	public PL getPlayer() {
		return player;
	}
	PlayerResults(PL player) {
		this.player = player;
	}

	private final Map<Integer, IndexResults<PL>> results = new HashMap<Integer, IndexResults<PL>>();

	private final Function<Integer, IndexResults<PL>> producer = new Function<Integer, IndexResults<PL>>() {
		@Override
		public IndexResults<PL> apply(Integer arg0) {
			return new IndexResults<PL>();
		}
	};
	
	void addResult(int myIndex, PL opponent, PL winner) {
		IndexResults<PL> result = GuavaExt.mapGetOrPut(results, myIndex, producer);
		result.informAbout(opponent, winner == this.player);
	}
	
	public String toStringMultiLine() {
		StringBuilder str = new StringBuilder();
		for (Entry<Integer, IndexResults<PL>> ee : results.entrySet()) {
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
		
		for (Entry<Integer, IndexResults<PL>> ee : results.entrySet()) {
			WinsLosses tot = ee.getValue().calcTotal();
			wins += tot.getWins();
			losses += tot.getLosses();
		}
		return new WinsLosses(wins, losses);
	}
	
	public double calculatePercentage() {
		return calcTotal().getPercentage();
	}
	
	@Override
	public int compareTo(PlayerResults<PL> o) {
		return Double.compare(this.calculatePercentage(), o.calculatePercentage());
	}
}