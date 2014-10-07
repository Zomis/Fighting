package net.zomis.fight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility methods that make a lot of usage of Guava.
 */
public class GuavaExt {
	
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
		List<List<T>> result = new ArrayList<>();
		List<T> subset = new ArrayList<>(subsetSize);
		for (int i = 0; i < subsetSize; i++) {
			subset.add(null);
		}
		return processLargerSubsets(result, set, subset, 0, 0);
	}

	private static <T> List<List<T>> processLargerSubsets(List<List<T>> result, List<T> set, List<T> subset, int subsetSize, int nextIndex) {
		if (subsetSize == subset.size()) {
			result.add(Collections.unmodifiableList(new ArrayList<>(subset)));
		} else {
			for (int j = nextIndex; j < set.size(); j++) {
				subset.set(subsetSize, set.get(j));
				processLargerSubsets(result, set, subset, subsetSize + 1, j + 1);
			}
		}
		return result;
	}

/*	public static <T> Collection<List<T>> permutations(List<T> list, int size) {
		Collection<List<T>> all = new ArrayList<>();
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
	}*/
}
