package entities.distributors.factory.actions;

import common.Constants;
import entities.consumers.AddContract;
import entities.consumers.Consumer;
import entities.distributors.Distributor;
import entities.distributors.factory.DistributorAction;
import entities.distributors.factory.DistributorFactory;
import entities.producers.Producer;
import input.Input;

public final class UpdateDistributors implements DistributorAction {

    private static UpdateDistributors instance = null;

    /**
     * an instance of this class
     * @return
     */
    public static UpdateDistributors getInstance() {
        if (instance == null) {
            instance = new UpdateDistributors();
        }
        return instance;
    }

    @Override
    public void computeAction(final Distributor bestDistributor, final Input input) {

        for (Distributor distributor : input.getDistributors()) {

            if (distributor.isUpdatable()) {
                for (Producer producer : input.getProducers()) {
                    producer.removeObserver(distributor);
                }

                (new DistributorFactory()).getAction(Constants.PRODUCTION_COST)
                        .computeAction(distributor, input);
            }
            if (distributor.isBankrupt()) {
                for (Producer producer : input.getProducers()) {
                    producer.removeObserver(distributor);
                }
                if (distributor.getConsumers() != null) {
                    for (Consumer consumer : distributor.getConsumers()) {
                        AddContract.getInstance().computeAction(consumer, bestDistributor);
                    }
                }
            }
        }
    }
}
