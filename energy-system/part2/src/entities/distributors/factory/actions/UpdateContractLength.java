package entities.distributors.factory.actions;

import entities.consumers.AddContract;
import entities.consumers.Consumer;
import entities.distributors.Distributor;
import entities.distributors.factory.DistributorAction;
import input.Input;

import java.util.List;

public final class UpdateContractLength implements DistributorAction {

    private static UpdateContractLength instance = null;

    /**
     *
     * @return instance of UpdateContractLength
     */
    public static UpdateContractLength getInstance() {
        if (instance == null) {
            instance = new UpdateContractLength();
        }
        return instance;
    }

    @Override
    public void computeAction(final Distributor bestDistributor, final Input input) {
        List<Consumer> consumers = input.getConsumers();
        List<Distributor> distributors = input.getDistributors();

        for (Consumer consumer : consumers) {
            if (!consumer.isBankrupt()) {
                consumer.setContractLength(consumer.getContractLength() - 1);

                if (consumer.getContractLength() == 0) {

                    //remove consumer from the distributors' lists
                    for (Distributor distributor : distributors) {
                        if (distributor.getConsumers() != null) {
                            if (distributor.getConsumers().contains(consumer)) {
                                distributor.removeConsumer(consumer);
                            }
                        }
                    }

                    AddContract.getInstance().computeAction(consumer, bestDistributor);
                }
            }
        }
    }
}
