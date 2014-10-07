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

public class FightResults<T> {
	private final Map<T, PlayerResults<T>> playerData = new HashMap<T, PlayerResults<T>>();
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
	public List<PlayerResults<T>> getResultsAsc() {
		List<PlayerResults<T>> list = new ArrayList<PlayerResults<T>>(playerData.values());
		Collections.sort(list);
		return list;
	}
	
	/**
	 * @return An ordered {@link LinkedHashMap} ranking all fighters with their associated win percentage with the best performing player first
	 */
	public LinkedHashMap<T, Double> getPercentagesDesc() {
		LinkedHashMap<T, Double> result = new LinkedHashMap<T, Double>();
		for (Entry<T, PlayerResults<T>> ee : entriesSortedByValues(playerData, true)) {
			result.put(ee.getKey(), ee.getValue().calcTotal().getPercentage());
		}
		return result;
	}

	void finished() {
		this.timeEnd = System.nanoTime();
	}
	
	public void saveResult(T[] fighters, T winner) {
		final int DEFAULT_INDEX = 0;
		for (int i = 0; i < fighters.length; i++) {
			T pp1 = fighters[i];
			
			PlayerResults<T> result = playerData.computeIfAbsent(pp1, pl -> new PlayerResults<T>(pl));
			
			for (int j = 0; j < fighters.length; j++) { // loop to add both wins and losses for players
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
		for (Entry<T, PlayerResults<T>> ee : entriesSortedByValues(playerData, false)) {
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
