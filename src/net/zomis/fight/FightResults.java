package net.zomis.fight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.base.Function;

public class FightResults<PL> {
	private final Map<PL, PlayerResults<PL>> playerData = new HashMap<PL, PlayerResults<PL>>();
	private final boolean	separateIndexes;
	private final String label;
	private final long timeStart;
	private long timeEnd;
	
	public FightResults(String label, boolean separateIndexes) {
		this.label = label;
		this.separateIndexes = separateIndexes;
		this.timeStart = System.nanoTime();
	}
	
	/**
	 * @return A list of all the player results, with the worst performing player first and the best player last.
	 */
	public List<PlayerResults<PL>> getResultsAsc() {
		List<PlayerResults<PL>> list = new ArrayList<PlayerResults<PL>>(playerData.values());
		Collections.sort(list);
		return list;
	}
	
	/**
	 * @return An ordered {@link LinkedHashMap} ranking all fighters with their associated win percentage with the best performing player first
	 */
	public LinkedHashMap<PL, Double> getPercentagesDesc() {
		LinkedHashMap<PL, Double> result = new LinkedHashMap<PL, Double>();
		for (Entry<PL, PlayerResults<PL>> ee : entriesSortedByValues(playerData, true)) {
			result.put(ee.getKey(), ee.getValue().calcTotal().getPercentage());
		}
		return result;
	}

	private static class ProduceValue<PL> implements Function<PL, PlayerResults<PL>> {
		@Override
		public PlayerResults<PL> apply(PL arg0) {
			return new PlayerResults<PL>(arg0);
		}
	}
	
	void finished() {
		this.timeEnd = System.nanoTime();
	}
	
	private final ProduceValue<PL> prodValue = new ProduceValue<PL>();
	public void saveResult(PL[] fighters, PL winner) {
		final int DEFAULT_INDEX = 0;
		for (int i = 0; i < fighters.length; i++) {
			PL pp1 = fighters[i];
			PlayerResults<PL> result = GuavaExt.mapGetOrPut(playerData, pp1, prodValue);
			
			for (int j = 0; j < fighters.length; j++) {
				if (i == j)
					continue;
				
				result.addResult(separateIndexes ? i : DEFAULT_INDEX , fighters[j], winner);
			}
		}
	}
	
	@Override
	public String toString() {
		return playerData.toString();
	}
	
	/**
	 * @return A human-readable text about the results of this fight
	 */
	public String toStringMultiLine() {
		StringBuilder str = new StringBuilder();
		if (this.label != null) {
			str.append(this.label);
			str.append("\n");
		}
		str.append(super.toString());
		str.append("\n");
		for (Entry<PL, PlayerResults<PL>> ee : entriesSortedByValues(playerData, false)) {
			str.append(ee.getKey());
			str.append("\n");
			str.append(ee.getValue().toStringMultiLine());
			str.append("\n");
		}
		double timeSpent = (timeEnd - timeStart) / 1000000.0;
		str.append("Time spent: " + (timeSpent) + " milliseconds\n");
		return str.toString();
	}

	/**
	 * Sort a map by it's values
	 * 
	 * @param map The map to sort
	 * @param descending True to sort in descending order, false to sort in ascending order
	 * @return A SortedSet containing all the entries from the original map.
	 */
	public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map, final boolean descending) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
			new Comparator<Map.Entry<K, V>>() {
				@Override
				public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
					int res;
					if (descending)	
						 res = e1.getValue().compareTo(e2.getValue());
					else res = e2.getValue().compareTo(e1.getValue());
					return res != 0 ? -res : 1; // Special fix to preserve items with equal values
				}
		    }
		);
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}
}
