package strategies.sort;

import entities.producers.Producer;

import java.util.List;

public final class GreenMultipleFieldsSorter {

    /**
     * sort producers by their energy type
     * then compare price and quantity
     * @param producers which should be sorted
     */
    public void sort(List<Producer> producers) {
        producers.sort(new GreenComparator()
                .thenComparing(new PriceQuantityComparator()));
    }
}
