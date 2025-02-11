package org.noSaczException.task04SudokuQuadrantChecker;

import java.util.*;

/**
 * Sudoku Quadrant Checker
 * Have the function
 * SudokuQuadrantChecker (strArr) read the strArr parameter being passed which will represent a 9x9 Sudoku board of integers
 * ranging from 1 to 9. The rules of Sudoku are to place each of the 9 integers integer in every row and column and not have
 * any integers repeat in the respective row, column, or 3x3 sub-grid. The input strArr will represent a Sudoku board
 * and it will be structured in the following format: ["(N,N,N,N,N,x,x,x,x)","(...)","(...)",....)] where N stands for an integer
 * between 1 and 9 and x will stand for an empty cell. Your program will determine if the board is legal;
 * the board also does not necessarily have to be finished. If the board is legal, your program should return the string legal
 * but if it isn't legal, it should return the 3x3 quadrants (separated by commas) where the errors exist.
 * The 3x3 quadrants are numbered from 1 to 9 starting from top-left going to bottom-right.
 * *
 * For example, if strArr is: [
 * "(1,2,3,4,5,6,7,8,1)",
 * "(Χ,Χ,Χ,Χ,Χ,Χ,Χ,X,X)",
 * "(Χ,Χ,Χ,Χ,Χ,Χ,Χ,X,X)",
 * "(1,Χ,Χ,Χ,Χ,Χ,Χ,Χ,X)",
 * "(Χ,Χ,Χ,Χ,Χ,Χ,Χ,X,X)",
 * "(Χ,Χ,Χ,Χ,Χ,Χ,Χ,X,X)",
 * "(X,X,X,X,X,X,X,X,X)",
 * "(X,X,X,X,X,X,X,X,X)",
 * "(Χ,Χ,Χ,Χ,Χ,Χ,Χ,X,X)"]
 * then your program should return 1,3,4 since the errors are in quadrants 1, 3 and 4 because of the repeating integer 1.
 * *
 * Another example, if strArr is: [
 * "(1,2,3,4,5,6,7,8,9)",
 * "(Χ,Χ,Χ,Χ,Χ,Χ,Χ,X,X)",
 * "(6,Χ,5,Χ,3,Χ,Χ,4,X)",
 * "(2,Χ,1,1,Χ,Χ,Χ,Χ,Χ)",
 * "(Χ,Χ,Χ,Χ,Χ,Χ,Χ,X,X)",
 * "(Χ,Χ,Χ,Χ,Χ,Χ,Χ,X,X)",
 * "(Χ,Χ,Χ,Χ,Χ,Χ,Χ,X,X)",
 * "(Χ,Χ,Χ,Χ,Χ,Χ,Χ,X,X)",
 * "(Χ,Χ,Χ,Χ,Χ,Χ,Χ,X,9)]
 * *
 * then your program should return
 * 3,4,5,9.
 */
public class SudokuQuadrantChecker {

    public static void main(String[] args) {
        System.out.println(SudokuChecker(firstExample()));
        System.out.println(SudokuChecker(secondExample()));
    }

    public static Set<Integer> SudokuChecker(String[][] sudokuGrid) {
        Set<Integer> result = new HashSet<>();
        Map<Integer, String> values = new LinkedHashMap<>();
        checkRowsOrColumns(sudokuGrid, values, result, true);
        checkRowsOrColumns(sudokuGrid, values, result, false);
        checkMatrix3x3(sudokuGrid, result);
        return result;
    }

    private static void checkRowsOrColumns(String[][] sudokuGrid, Map<Integer, String> values, Set<Integer> result, boolean isRow) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                values.put(j + 1, isRow ? sudokuGrid[i][j] : sudokuGrid[j][i]);
            }

            Map<String, List<Integer>> valueToKeys = groupKeysByValues(values);
            List<List<Integer>> listsWithErrors = new ArrayList<>();
            getListsWithDuplicates(valueToKeys, listsWithErrors);

            if (!listsWithErrors.isEmpty()) {
                for (List<Integer> listWithErrors : listsWithErrors) {
                    for (Integer key : listWithErrors) {
                        result.add(checkRowAndColumn(key, i, isRow));
                    }
                }
            }
        }
    }

    private static void checkMatrix3x3(String[][] sudokuGrid, Set<Integer> result) {
        Integer blockNumber3x3 = 1;
        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) {
                if (!check3x3(sudokuGrid, row, col)) {
                    result.add(blockNumber3x3);
                }
                blockNumber3x3++;
            }
        }
    }

    private static void getListsWithDuplicates(Map<String, List<Integer>> valueToKeys, List<List<Integer>> listsWithErrors) {
        valueToKeys.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .forEach(entry -> {
                    List<Integer> value = entry.getValue();
                    listsWithErrors.add(value);
                });
    }

    private static Map<String, List<Integer>> groupKeysByValues(Map<Integer, String> values) {
        Map<String, List<Integer>> valueToKeys = new HashMap<>();
        for (Map.Entry<Integer, String> entry : values.entrySet()) {
            if (!entry.getValue().equals("X")) {
                valueToKeys.computeIfAbsent(entry.getValue(), k -> new ArrayList<>()).add(entry.getKey());
            }
        }
        return valueToKeys;
    }

    private static Integer checkRowAndColumn(double key, int i, boolean isRow) {
        double divide = key / 3;
        if (divide <= 1) {
            return check3x3(i, 1, isRow ? 4 : 2, isRow ? 7 : 3);
        } else if (divide <= 2) {
            return check3x3(i, isRow ? 2 : 4, 5, isRow ? 8 : 6);
        } else if (divide <= 3) {
            return check3x3(i, isRow ? 3 : 7, isRow ? 6 : 8, 9);
        }
        return 0;
    }

    private static Integer check3x3(int i, Integer first3x3, Integer second3x3, Integer third3x3) {
        return (i < 3) ? first3x3 : (i < 6) ? second3x3 : third3x3;
    }

    private static boolean check3x3(String[][] sudokuGrid, int row3x3, int col3x3) {
        ArrayList<String> arrayGrid3x3 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String gridValue = sudokuGrid[row3x3 + i][col3x3 + j];
                if (!gridValue.equals("X")) {
                    arrayGrid3x3.add(gridValue);
                }
            }
        }
        Set<String> setGrid3x3 = new HashSet<>(arrayGrid3x3);
        return setGrid3x3.size() - arrayGrid3x3.size() == 0;
    }

    private static String[][] firstExample() {
        return new String[][]{
                {"1", "2", "3", "4", "5", "6", "7", "8", "1"},
                {"1", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"1", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X"}};
    }

    private static String[][] secondExample() {
        return new String[][]{
                {"1", "2", "3", "4", "5", "6", "7", "8", "9"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"6", "X", "5", "X", "3", "X", "X", "4", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"2", "X", "1", "1", "X", "X", "X", "X", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "9"}};
    }
}
