package output;

import entities.consumers.Consumer;
import entities.distributors.Distributor;
import entities.producers.Producer;
import input.Input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ComputeOutput {

    private final Input input;

    public ComputeOutput(final Input input) {
        this.input = input;
    }

    /**
     * creates new Lists with the fields which should be
     * displayed in the output files
     * @param outputConsumers which should be displayed
     * @param outputDistributors which should be displayed
     * @param outputProducers which should be displayed
     */
    public void createOutput(final List<ConsumerOutput> outputConsumers,
                             final List<DistributorOutput> outputDistributors,
                             final List<ProducerOutput> outputProducers) {

        List<Consumer> consumers = input.getConsumers();
        List<Distributor> distributors = input.getDistributors();
        List<Producer> producers = input.getProducers();

        for (Consumer consumer : consumers) {
            outputConsumers.add(new ConsumerOutput(consumer.getId(), consumer.isBankrupt(),
                    consumer.getBudget()));
        }

        for (Distributor distributor : distributors) {

            List<ContractOutput> contracts = new ArrayList<>();

            if (distributor.getConsumers() != null) {
                for (Consumer consumer : distributor.getConsumers()) {
                    contracts.add(new ContractOutput(consumer.getId(), consumer.getPrice(),
                            consumer.getContractLength() - 1));
                }
            }
            outputDistributors
                    .add(new DistributorOutput(distributor.getId(), distributor.getEnergyNeededKW(),
                            distributor.getFinalPrice(), distributor.getBudget(),
                            distributor.getProducerStrategy(), distributor.isBankrupt(),
                            contracts));
        }

        producers.sort(Comparator.comparingInt(Producer::getId));

        for (Producer producer : producers) {

            List<MonthlyStatus> monthlyStatuses = new ArrayList<>();

            for (Map.Entry<Integer, List<Integer>> distributorsMonth : producer
                    .getDistributorsInEachMonth().entrySet()) {
                List<Integer> distributorsIds = new ArrayList<>(distributorsMonth.getValue());

                Collections.sort(distributorsIds);

                monthlyStatuses.add(new MonthlyStatus(distributorsMonth.getKey(), distributorsIds));
            }
            outputProducers.add(new ProducerOutput(producer.getId(), producer.getMaxDistributors(),
                    producer.getPrice(), producer.getEnergyType(),
                    producer.getEnergyPerDistributor(),
                    monthlyStatuses));
        }
    }
}
