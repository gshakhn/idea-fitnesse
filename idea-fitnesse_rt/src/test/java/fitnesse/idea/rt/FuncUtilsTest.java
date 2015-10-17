package fitnesse.idea.rt;

import org.junit.Test;

import java.util.List;

import static fitnesse.idea.rt.FuncUtils.zip;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FuncUtilsTest {

    @Test
    public void shouldZip() {
        List<List<Integer>> table = asList(
                asList(1, 2),
                asList(3, 4)
        );

        List<List<Integer>> zipped = asList(
                asList(1, 3),
                asList(2, 4)
        );

        assertThat(zip(table, null), is(zipped));
    }

    @Test
    public void zipShouldFillNonExistantCellsWithNull() {
        List<List<Integer>> table = asList(
                asList(1, 2),
                singletonList(3)
        );

        List<List<Integer>> zipped = asList(
                asList(1, 3),
                asList(2, null)
        );

        assertThat(zip(table, null), is(zipped));
    }


}