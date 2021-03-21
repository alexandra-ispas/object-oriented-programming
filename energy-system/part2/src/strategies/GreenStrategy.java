package strategies;

import entities.producers.Producer;
import strategies.sort.GreenMultipleFieldsSorter;

import java.util.List;

public final class GreenStrategy implements Strategy {
    @Override
    public void sort(final List<Producer> producers) {
        GreenMultipleFieldsSorter sorter = new GreenMultipleFieldsSorter();
        sorter.sort(producers);
    }
}
