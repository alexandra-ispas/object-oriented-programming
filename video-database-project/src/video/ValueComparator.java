package video;

import java.util.Comparator;
import java.util.TreeMap;

public class ValueComparator implements Comparator {
    private final TreeMap map;

    public ValueComparator(final TreeMap map) {
        this.map = map;
    }

    /**
     * compara cheile din 2 map-uri
     * @param keyA
     * @param keyB
     * @return
     */
    public int compare(final Object keyA, final Object keyB) {
        Comparable valueA = (Comparable) map.get(keyA);
        Comparable valueB = (Comparable) map.get(keyB);

        return valueB.compareTo(valueA);
    }
}
