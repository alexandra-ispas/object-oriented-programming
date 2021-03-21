package input.factory.actions;

import entities.consumers.Consumer;
import entities.distributors.Distributor;
import input.Input;
import input.factory.InputAction;

import java.util.List;

public final class RemoveBankruptConsumers implements InputAction {

    private static RemoveBankruptConsumers instance = null;

    /**
     *
     * @return an instance of the RemoveBankruptConsumers class
     */
    public static RemoveBankruptConsumers getInstance() {
        if (instance == null) {
            instance = new RemoveBankruptConsumers();
        }
        return instance;
    }

    @Override
    public void computeAction(Input input, int month) {
        List<Consumer> consumers = input.getConsumers();
        List<Distributor> distributors = input.getDistributors();
        for (Consumer consumer : consumers) {
            if (consumer.isBankrupt()) {
                for (Distributor distributor : distributors) {
                    if (distributor.getConsumers() != null) {
                        if (distributor.getConsumers().contains(consumer)) {
                            distributor.removeConsumer(consumer);
                            break;
                        }
                    }
                }
            }
        }
    }
}
