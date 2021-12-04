package day04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Board {
    private final List<List<Integer>> numbers = new ArrayList<>();
    private final Map<Integer, Set<Integer>> drawnNumbers = new HashMap<>();

    public void addRow(List<Integer> row) {
        numbers.add(row);
    }

    public boolean processDrawnNumber(Integer number) {
        // find number on board
        for (int i = 0; i < numbers.size(); ++i) {
            for (int j = 0; j < numbers.size(); j++) {
                if (numbers.get(i).get(j).equals(number)) {
                    this.markAsDrawn(i, j);
                    return isRowMarked(i) || isColumnMarked(j);
                }
            }
        }

        return false;
    }

    private void markAsDrawn(int i, int j) {
        Set<Integer> row = drawnNumbers.computeIfAbsent(i, k -> new HashSet<>());

        row.add(j);
    }

    private boolean isRowMarked(int rowIndex) {
        Set<Integer> row = drawnNumbers.get(rowIndex);

        return row != null && row.size() == numbers.size();
    }

    private boolean isColumnMarked(int columnIndex) {
        if (drawnNumbers.size() < numbers.size()) {
            return false;
        }

        return drawnNumbers.values().stream().allMatch(row -> row.contains(columnIndex));
    }

    public int getUnmarkedSum() {
        int result = 0;

        for (int i = 0; i < numbers.size(); ++i) {
            List<Integer> row = numbers.get(i);
            Set<Integer> drawnRow = drawnNumbers.get(i);
            for (int j = 0; j < row.size(); ++j) {
                if (drawnRow == null || !drawnRow.contains(j)) {
                    result += row.get(j);
                }
            }
        }

        return result;
    }
}
