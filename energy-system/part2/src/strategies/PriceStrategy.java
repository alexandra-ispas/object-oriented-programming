package strategies;

import entities.producers.Producer;
import strategies.sort.PriceQuantityComparator;

import java.util.List;

public final class PriceStrategy implements Strategy {
    @Override
    public void sort(final List<Producer> producers) {
        producers.sort(new PriceQuantityComparator());
    }
}
