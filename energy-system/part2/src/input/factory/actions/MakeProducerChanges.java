package input.factory.actions;

import entities.producers.Producer;
import entities.updates.ProducerChanges;
import input.Input;
import input.factory.InputAction;

import java.util.List;

public final class MakeProducerChanges implements InputAction {

    private static MakeProducerChanges instance = null;

    /**
     *
     * @return an instance of the MakeProducerChanges class
     */
    public static MakeProducerChanges getInstance() {
        if (instance == null) {
            instance = new MakeProducerChanges();
        }
        return instance;
    }

    @Override
    public void computeAction(Input input, int month) {
        List<ProducerChanges> producerChanges = input.getProducerChanges().get(month);
        List<Producer> producers = input.getProducers();
        for (ProducerChanges change : producerChanges) {
            if (change != null) {
                for (Producer producer : producers) {
                    if (producer.getId() == change.getId()) {
                        producer.setEnergyPerDistributor(change.getEnergyPerDistributor());

                        producer.setUpdated();
                        break;
                    }
                }
            }
        }
    }
}
