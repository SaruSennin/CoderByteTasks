package org.noSaczException.task03Wildcards;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wildcards
 * Have the function Wildcards (str) read str which will contain two strings separated by a space. The first string will
 * consist of the following sets of characters: +, *, $, and {N} which is optional.
 * The plus (+) character represents a single alphabetic character,
 * the ($) character represents a number between 1-9,
 * and the asterisk (*) represents a sequence of the same character of length 3 unless it is followed by {N} which
 * represents how many characters should appear in the sequence where N will be at least 1.
 * Your goal is to determine if the second string exactly matches the pattern of the first string in the input.
 * *
 * For example: if str is "++*{5} jtggggg" then the second string in this case does match the pattern, so your program
 * should return the string true. If the second string does not match the pattern your program should return
 * the string false.
 */
public class Wildcards {

    public static void main(String[] args) {
        System.out.println(wildcards("++*{5 jtggggg") + " ++*{5 jtggggg \n"); // false
        System.out.println(wildcards("++*{5} jtggggg") + " ++*{5} jtggggg \n"); // true
        System.out.println(wildcards("++$ a1") + " ++$ a1 \n"); // false
        System.out.println(wildcards("+* aaa a") + " +* aaaas \n"); // false
        System.out.println(wildcards("*{4} bbbb") + " *{4} bbbb \n"); // true
    }

    public static boolean wildcards(String str) {
        String[] patternAndValue = validateData(str);
        if (patternAndValue != null) {
            String patternString = patternAndValue[0];
            String value = patternAndValue[1];

            int valueIterator = 0;
            for (int i = 0; i < patternString.length(); i++) {
                String patternSymbol = String.valueOf(patternString.charAt(i));
                Pattern patternPlusAZ = Pattern.compile("[a-z]");
                Pattern patternDollar09 = Pattern.compile("[0-9]");
                Pattern patternAloneAsterisk = Pattern.compile("(^([a-z])\\2{2}$|([0-9])\\3{2}$)");

                switch (patternSymbol) {
                    case "+":
                        valueIterator++;
                        return isPatternNotCorrect(patternPlusAZ, value, valueIterator);
                    case "$":
                        valueIterator++;
                        return isPatternNotCorrect(patternDollar09, value, valueIterator);
                    case "*":
                        if (isPatternRepeatsByAsterisk(patternSymbol, patternString, i)) {
                            valueIterator += 3;
                            return isPatternNotCorrect(patternAloneAsterisk, value, valueIterator);
                        } else if (isPatternRepeatsByNumber(patternSymbol, patternString, i)) {
                            int replications = Integer.parseInt(patternString.substring(i + 2));
                            Pattern patternAsterisk = Pattern.compile("(^([a-z])\\2{" + replications + "}$|([0-9])\\3{" + replications + "}$)");
                            valueIterator += replications;
                            return isPatternNotCorrect(patternAsterisk, value, valueIterator);
                        }
                        break;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private static boolean isPatternNotCorrect(Pattern pattern, String value, int valueIterator) {
        return pattern.matcher(String.valueOf(value.charAt(valueIterator))).find();
    }

    private static boolean isPatternRepeatsByAsterisk(String patternSymbol, String patternString, int i) {
        return patternSymbol.equals("*") && i == patternString.length() - 1 && !patternString.substring(i + 1).equals("{");
    }

    private static boolean isPatternRepeatsByNumber(String patternSymbol, String patternString, int i) {
        return patternSymbol.equals("*") && patternString.substring(i + 1).equals("{");
    }

    private static String[] validateData(String str) {
        String[] patternAndValue = str.split(" ");
        String patternString = patternAndValue[0];
        String value = patternAndValue[1];
        try {
            if (patternString.contains(" ") || patternAndValue.length > 2) {
                throw new IllegalArgumentException("a pattern starting with " + patternString + " should not contain more than one space");
            } else if (checkCurlyBrackets(patternString)) {
                throw new IllegalArgumentException("a problem was detected with the brackets {} in the pattern " + patternString);
            } else if (!compareLength(patternString, value)) {
                throw new IllegalArgumentException("a problem was detected with the expected number of characters for " + patternString + " and " + value);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        return patternAndValue;
    }

    private static boolean checkCurlyBrackets(String patternString) {
        Pattern invalidPattern1 = Pattern.compile("\\*\\{[^}]*$");
        Pattern invalidPattern2 = Pattern.compile("\\*\\{\\s*}");
        Pattern invalidPattern3 = Pattern.compile("\\*\\{\\D+}");

        Matcher matcher1 = invalidPattern1.matcher(patternString);
        Matcher matcher2 = invalidPattern2.matcher(patternString);
        Matcher matcher3 = invalidPattern3.matcher(patternString);

        for (Matcher matcher : List.of(matcher1, matcher2, matcher3)) {
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    private static boolean compareLength(String patternString, String value) {
        int patternLength = 0;

        Pattern pattern = Pattern.compile("\\*\\{([1-9][0-9]*)}");
        Matcher matcher = pattern.matcher(patternString);

        while (matcher.find()) {
            String fullMatch = matcher.group();
            patternLength += Integer.parseInt(matcher.group(1));
            patternString = patternString.replace(fullMatch, "");
        }

        patternLength += patternString.chars()
                .map(charItem -> switch (charItem) {
                    case '*' -> 3;
                    case '+', '$' -> 1;
                    default -> 0;
                })
                .sum();

        return patternLength == value.length();
    }
}
