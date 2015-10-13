package fitnesse.idea.rt;

import fitnesse.idea.rt.FuncUtils.Mapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static fitnesse.idea.rt.FuncUtils.map;
import static fitnesse.idea.rt.FuncUtils.zip;
import static java.util.Collections.max;

public class TableFormatter {
    public final static String CELL_SEPARATOR = " | ";
    public final static int MIN_PADDING = 1;

    private final List<List<Integer>> table;
    private final List<Integer> columnsPerRow;
    private final List<Integer> widthPerColumn;
    private final List<Integer> paddingForLastColumn;

    public TableFormatter(List<List<Integer>> table) {
        this.table = table;
        columnsPerRow = map(table, new Mapper<List<Integer>, Integer>() {
            @Override
            public Integer call(List<Integer> row) {
                return row.size();
            }
        });

        List<List<Integer>> nonColspanWidthMatrix = map(table, new Mapper<List<Integer>, List<Integer>>() {
            @Override
            public List<Integer> call(List<Integer> row) {
                return row.size() > 0 ? row.subList(0, row.size() - 1) : Collections.<Integer>emptyList();
            }
        });

        widthPerColumn = map(zip(nonColspanWidthMatrix, 0), new Mapper<List<Integer>, Integer>() {
            @Override
            public Integer call(List<Integer> column) {
                return max(column);
            }
        });

        List<Integer> rowWidth = map(table, new Mapper<List<Integer>, Integer>() {
            @Override
            public Integer call(List<Integer> row) {
                int rowWidth = 0;
                for (int i = 0; i < row.size() - 1; i++) {
                    rowWidth += widthPerColumn.get(i) + CELL_SEPARATOR.length();
                }
                rowWidth += row.get(row.size() - 1);
                return rowWidth;
            }
        });

        final int maxRowWidth = max(rowWidth);

        paddingForLastColumn = map(rowWidth, new Mapper<Integer, Integer>() {
            @Override
            public Integer call(Integer width) {
                return maxRowWidth - width;
            }
        });
    }

    public int leftPadding() {
        return MIN_PADDING;
    }

    public char[] leftPaddingString() {
        return new char[] { ' ' };
    }

    public int rightPadding(int row, int col) {
        if (col == columnsPerRow.get(row) - 1) {
            return rightPadding(row);
        }
        int contentWidth = table.get(row).get(col);
        return widthPerColumn.get(col) - contentWidth + MIN_PADDING;
    }

    public int rightPadding(int row) {
        return paddingForLastColumn.get(row) + MIN_PADDING;
    }

    public char[] rightPaddingString(int row, int col) {
        return paddingString(rightPadding(row, col));
    }

    private char[] paddingString(int i) {
        char[] chars = new char[i > 0 ? i : 0];
        Arrays.fill(chars, ' ');
        return chars;
    }
}
