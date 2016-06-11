package de.uni_potsdam.hpi.bpt.bp2014.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class CollectionUtil {
    public static <T> List<List<T>> computeCartesianProduct(List<List<T>> lists) {
        List<List<T>> currentCombinations = Arrays.asList(Arrays.asList());
        for (List<T> list : lists) {
            currentCombinations = appendElements(currentCombinations, list);
        }
        return currentCombinations;
    }

    public static <T> List<List<T>> appendElements(List<List<T>> combinations, List<T> extraElements) {
        return combinations.stream().flatMap(oldCombination
                -> extraElements.stream().map(extra -> {
            List<T> combinationWithExtra = new ArrayList<>(oldCombination);
            combinationWithExtra.add(extra);
            return combinationWithExtra;
        })).collect(Collectors.toList());
    }

    /**
     * This method can be used when searching for the key of a value in a mapping
     * where each key points to a list of values. It is assumed that each value can
     * only occur for one key.
     * @param mapping Map from key to the List of values
     * @return Inverted Map from value to it's respective key
     */
    public static Map<Integer, Integer> invertMapping(Map<Integer,
            ? extends Collection<Integer>> mapping) {
        Map<Integer, Integer> newMap = new HashMap<>();
        for (Map.Entry<Integer, ? extends Collection<Integer>> entry : mapping.entrySet()) {
            for (int i : entry.getValue()) {
                assert !newMap.containsKey(i);
                newMap.put(i, entry.getKey());
            }
        }
        return newMap;
    }
}
