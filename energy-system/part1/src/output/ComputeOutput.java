package output;

import fileio.Consumer;
import fileio.Distributor;
import fileio.Input;

import java.util.ArrayList;
import java.util.List;

public class ComputeOutput {

    private final Input input;

    public ComputeOutput(final Input input) {
        this.input = input;
    }

    /**
     * creates new Lists with the fields which should be
     * displayed in the output files
     *
     * @param outputConsumers
     * @param outputDistributors
     */
    public void createOutput(final List<ConsumerOutput> outputConsumers,
                             final List<DistributorOutput> outputDistributors) {

        List<Consumer> consumers = input.getConsumers();
        List<Distributor> distributors = input.getDistributors();

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
                    .add(new DistributorOutput(distributor.getId(), distributor.getBudget(),
                            distributor.isBankrupt(), contracts));
        }
    }
}
