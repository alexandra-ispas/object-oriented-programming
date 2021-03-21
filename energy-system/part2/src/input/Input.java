package input;

import entities.consumers.Consumer;
import entities.distributors.Distributor;
import entities.producers.Producer;
import entities.updates.DistributorChanges;
import entities.updates.ProducerChanges;

import java.util.List;

public final class Input {
    private final long numberOfTurns;
    private final List<Consumer> consumers;
    private final List<Distributor> distributors;
    private final List<Producer> producers;
    private final List<List<Consumer>> newConsumers;
    private final List<List<ProducerChanges>> producerChanges;
    private final List<List<DistributorChanges>> distributorChanges;

    public Input(final long numberOfTurns, final List<Consumer> inputConsumers,
                 final List<Distributor> inputDistributors,
                 final List<Producer> producers, final List<List<Consumer>> newConsumers,
                 final List<List<ProducerChanges>> producerChanges,
                 final List<List<DistributorChanges>> distributorChanges) {

        this.numberOfTurns = numberOfTurns;
        this.consumers = inputConsumers;
        this.distributors = inputDistributors;
        this.producers = producers;
        this.newConsumers = newConsumers;
        this.producerChanges = producerChanges;
        this.distributorChanges = distributorChanges;
    }

    public long getNumberOfTurns() {
        return numberOfTurns;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public List<Distributor> getDistributors() {
        return distributors;
    }

    public List<List<Consumer>> getNewConsumers() {
        return newConsumers;
    }

    public List<Producer> getProducers() {
        return producers;
    }

    public List<List<ProducerChanges>> getProducerChanges() {
        return producerChanges;
    }

    public List<List<DistributorChanges>> getDistributorChanges() {
        return distributorChanges;
    }

    /**
     * adds a new consumer to the structure
     * @param consumer to be added
     */
    public void addNewConsumer(final Consumer consumer) {
        consumers.add(consumer);
    }

}
