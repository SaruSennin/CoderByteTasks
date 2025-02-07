package org.noSaczException.task01DistinctList;

import java.util.HashMap;

/**
 * Distinct List
 * Have the function DistinctList (arr) take the array of numbers stored in arr and determine the total number
 * of duplicate entries. For example if the input is [1, 2, 2, 2, 3] then your program should output 2 because
 * there are two duplicates of one of the elements.\
*/
public class DistinctList {
    public static void main(String[] args) {
        int[] arr = {1, 2, 2, 2, 3, 3};
        System.out.println(distinctList(arr));
    }

    private static int distinctList(int[] arr) {
        HashMap<Integer, Integer> countedElements = new HashMap<>();

        for (int num : arr) {
            countedElements.put(num, countedElements.getOrDefault(num, 0) + 1);
        }

        return countedElements.values().stream()
                .filter(value -> value > 1)
                .mapToInt(value -> value - 1)
                .sum();
    }
}
