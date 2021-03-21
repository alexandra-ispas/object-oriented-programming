package strategies;

import entities.producers.Producer;

import java.util.List;

public interface Strategy {

    /**
     * sort producers based on a specific strategy
     * @param producers which should be sorted
     */
    void sort(List<Producer> producers);
}
