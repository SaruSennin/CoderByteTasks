package org.noSaczException.task02RomanNumberReduction;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Roman Numeral Reduction
 * Have the function RomanNumeralReduction(str) read str which will be a string of roman numerals in decreasing order.
 * The numerals being used are: I for 1, V for 5, X for 10, L for 50, C for 100, D for 500 and M for 1000. Your program
 * should return the same number given by str using a smaller set of roman numerals. For example: if str is "LLLXXXVVVV"
 * this is 200, so your program should return CC because this is the shortest way to write 200 using the roman numeral
 * system given above. If a string is given in its shortest form, just return that same string.
 */
public class RomanNumeralReduction {

    public static void main(String[] args) {
        System.out.println(romanNumeralReduction("LLLXXXVVVV")); // CC
        System.out.println(romanNumeralReduction("CC"));         // CC
        System.out.println(romanNumeralReduction("XIIIII"));     // XV
    }

    public static String romanNumeralReduction(String str) {

        int totalArabicValue = 0;
        for (char c : str.toCharArray()) {
            totalArabicValue += romanToArabic().get(c);
        }

        StringBuilder romanNumber = new StringBuilder();
        for (Map.Entry<Integer, String> entry : arabicToRoman().entrySet()) {
            while (totalArabicValue >= entry.getKey()) {
                romanNumber.append(entry.getValue());
                totalArabicValue -= entry.getKey();
            }
        }

        return romanNumber.toString();
    }

    private static Map<Integer, String> arabicToRoman() {
        Map<Integer, String> valueToRoman = new LinkedHashMap<>();
        valueToRoman.put(1000, "M");
        valueToRoman.put(500, "D");
        valueToRoman.put(100, "C");
        valueToRoman.put(50, "L");
        valueToRoman.put(10, "X");
        valueToRoman.put(5, "V");
        valueToRoman.put(1, "I");
        return valueToRoman;
    }

    private static Map<Character, Integer> romanToArabic() {
        return Map.of('M', 1000, 'D', 500, 'C', 100, 'L', 50, 'X', 10, 'V', 5, 'I', 1);
    }
}
