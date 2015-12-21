package fitnesse.idea.rt;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TableFormatterTest {

    @Test
    public void parseSimpleTable() {
        TableFormatter tableFormatter = new TableFormatter(asList(
            asList(15),
            asList(10, 7),
            asList(2, 3)));

        assertThat(tableFormatter.rightPadding(0, 0), is(10+1+7 - 15));
        assertThat(tableFormatter.rightPadding(1, 0), is(0));
        assertThat(tableFormatter.rightPadding(1, 1), is(0));
        assertThat(tableFormatter.rightPadding(2, 0), is(10 - 2));
        assertThat(tableFormatter.rightPadding(2, 1), is(7 - 3));
    }

    @Test
    public void parseSimpleTableWithSmallHeading() {
        TableFormatter tableFormatter = new TableFormatter(asList(
                asList(1),
                asList(10, 7)));

        assertThat(tableFormatter.rightPadding(0, 0), is(10+1+7 - 1));
        assertThat(tableFormatter.rightPadding(1, 1), is(0));
        assertThat(tableFormatter.rightPadding(1, 0), is(0));
    }

    @Test
    public void parseSimpleTableWithMultipleColumns() {
        TableFormatter tableFormatter = new TableFormatter(asList(
                asList(3, 1, 2),
                asList(10, 10)));

        assertThat(tableFormatter.rightPadding(0, 0), is(10 - 3));
        assertThat(tableFormatter.rightPadding(0, 1), is(0));
        assertThat(tableFormatter.rightPadding(0, 2), is(10 - 2 - 2));
        assertThat(tableFormatter.rightPadding(1, 1), is(0));
        assertThat(tableFormatter.rightPadding(1, 0), is(0));
    }

    @Test
    public void parseSimpleTableWithSimgleColumn() {
        TableFormatter tableFormatter = new TableFormatter(asList(
                asList(6),
                asList(10)));

        assertThat(tableFormatter.rightPadding(0, 0), is(10 - 6));
        assertThat(tableFormatter.rightPadding(1, 0), is(0));
    }
}