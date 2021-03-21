package strategies;

import entities.producers.Producer;

import java.util.List;

public final class ContextStrategy {
    private final Strategy strategy;

    public ContextStrategy(final Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * sort producers based on
     * a specific strategy type
     * @param producers which should be sorted
     */
    public void executeStrategy(final List<Producer> producers) {
        strategy.sort(producers);
    }
}
