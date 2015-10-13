package fitnesse.idea.rt;

import fitnesse.idea.rt.FuncUtils.Mapper;

import java.util.List;

import static fitnesse.idea.rt.FuncUtils.map;
import static fitnesse.idea.rt.FuncUtils.zip;
import static java.util.Collections.max;

public class TableFormatter {
    public final static int PADDING_LEFT = 1;

    private final List<List<Integer>> table;
    private final List<Integer> columnsPerRow;
    private final List<Integer> maxWidthPerColumn;
    private final List<Integer> extraPaddingForLastColumn;

    public TableFormatter(List<List<Integer>> table) {
        this.table = table;
        columnsPerRow = map(table, new Mapper<List<Integer>, Integer>() {
            @Override
            public Integer call(List<Integer> row) {
                return row.size();
            }
        });
        final Integer nColumns = max(columnsPerRow);

        List<List<Integer>> nonColspanWidthMatrix = map(table, new Mapper<List<Integer>, List<Integer>>() {
            @Override
            public List<Integer> call(List<Integer> row) {
                return row.subList(0, row.size() - 1);
            }
        });

        maxWidthPerColumn = map(zip(nonColspanWidthMatrix, 0), new Mapper<List<Integer>, Integer>() {
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
                    rowWidth += maxWidthPerColumn.get(i) + 3; // + " | ".length()
                }
                rowWidth += row.get(row.size() - 1);
                return rowWidth;
            }
        });

        final int maxRowWidth = max(rowWidth);

        extraPaddingForLastColumn = map(rowWidth, new Mapper<Integer, Integer>() {
            @Override
            public Integer call(Integer width) {
                return maxRowWidth - width;
            }
        });
    }

    public int getRightPadding(int row, int col) {
        if (columnsPerRow.get(row) == col + 1) {
            return extraPaddingForLastColumn.get(row) + 1;
        }
        int contentWidth = table.get(row).get(col);
        return maxWidthPerColumn.get(col) - contentWidth + 1;
    }

    public interface TableCell {
        int width();
        int colspan();
    }

    public static class SimpleTableCell implements TableCell {
        private final int width;
        private final int colspan;

        public SimpleTableCell(int width, int colspan) {

            this.width = width;
            this.colspan = colspan;
        }

        @Override
        public int width() {
            return width;
        }

        @Override
        public int colspan() {
            return colspan;
        }
    }
}
