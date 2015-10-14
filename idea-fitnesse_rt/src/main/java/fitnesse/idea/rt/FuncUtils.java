package fitnesse.idea.rt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.max;

public class FuncUtils {

    private FuncUtils() {
    }

    public static <A, B> List<B> map(List<A> collection, Mapper<A, B> mapper) {
        List<B> c = new ArrayList<B>(collection.size());
        for (A v : collection) {
            c.add(mapper.call(v));
        }
        return c;
    }

    public static <A> List<List<A>> zip(List<List<A>> collection, final A defaultValue) {
        final int nColumns = max(map(collection, new Mapper<List<A>, Integer>() {
            @Override public Integer call(List<A> row) {
                return row.size();
            }
        }));

        List<List<A>> out = new ArrayList<List<A>>();
        for(int i = 0; i < nColumns; i++) {
            List<A> row = new ArrayList<A>();
            for (List<A> c : collection) {
                if (i < c.size()) {
                    row.add(c.get(i));
                } else {
                    row.add(defaultValue);
                }
            }
            out.add(row);
        }
        return out;
    }

    public interface Mapper<A, B> {
        B call(A a);
    }

}
