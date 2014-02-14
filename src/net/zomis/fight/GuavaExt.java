package net.zomis.fight;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Utility methods that make a lot of usage of Guava.
 */
public class GuavaExt {
	public static <K, V> V mapGetOrPut(Map<K, V> map, K key, Function<K, V> valueProducer) {
		if (map.containsKey(key))
			return map.get(key);
		
		V value = valueProducer.apply(key);
		map.put(key, value);
		return value;
	}
	
	/**
	 * Creates a list of all subsets containing the specified number of elements
	 * 
	 * @param set The items to create combinations of
	 * @param subsetSize The size of each of the lists in the result
	 * @return List of lists containing all the combinations of lists with the specified size
	 */
	public static <T> List<List<T>> processSubsets(List<T> set, int subsetSize) {
		if (subsetSize > set.size()) {
			subsetSize = set.size();
		}
		List<List<T>> result = Lists.newArrayList();
		List<T> subset = Lists.newArrayListWithCapacity(subsetSize);
		for (int i = 0; i < subsetSize; i++) {
			subset.add(null);
		}
		return processLargerSubsets(result, set, subset, 0, 0);
	}

	private static <T> List<List<T>> processLargerSubsets(List<List<T>> result, List<T> set, List<T> subset, int subsetSize, int nextIndex) {
		if (subsetSize == subset.size()) {
			result.add(ImmutableList.copyOf(subset));
		} else {
			for (int j = nextIndex; j < set.size(); j++) {
				subset.set(subsetSize, set.get(j));
				processLargerSubsets(result, set, subset, subsetSize + 1, j + 1);
			}
		}
		return result;
	}

	public static <T> Collection<List<T>> permutations(List<T> list, int size) {
		Collection<List<T>> all = Lists.newArrayList();
		if (list.size() < size) {
			size = list.size();
		}
		if (list.size() == size) {
			all.addAll(Collections2.permutations(list));
		} else {
			for (List<T> p : processSubsets(list, size)) {
				all.addAll(Collections2.permutations(p));
			}
		}
		return all;
	}
}
