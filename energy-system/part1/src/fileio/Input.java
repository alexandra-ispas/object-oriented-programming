package fileio;

import java.util.List;

public class Input {
    private final long numberOfTurns;
    private final List<Consumer> consumers;
    private final List<Distributor> distributors;
    private final List<List<Consumer>> newConsumers;
    private final List<List<CostChanges>> costChanges;

    public Input(final long numberOfTurns, final List<Consumer> inputConsumers,
                 final List<Distributor> inputDistributors, final List<List<Consumer>> newConsumers,
                 final List<List<CostChanges>> costChanges) {
        this.numberOfTurns = numberOfTurns;
        this.consumers = inputConsumers;
        this.distributors = inputDistributors;
        this.newConsumers = newConsumers;
        this.costChanges = costChanges;
    }

    public final long getNumberOfTurns() {
        return numberOfTurns;
    }

    public final List<Consumer> getConsumers() {
        return consumers;
    }

    public final List<Distributor> getDistributors() {
        return distributors;
    }

    public final List<List<Consumer>> getNewConsumers() {
        return newConsumers;
    }

    public final List<List<CostChanges>> getCostChanges() {
        return costChanges;
    }

}
