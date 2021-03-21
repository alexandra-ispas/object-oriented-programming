package input.factory.actions;

import entities.consumers.Consumer;
import input.Input;
import input.factory.InputAction;

import java.util.List;

public final class ConsumersPayExpenses implements InputAction {

    private static ConsumersPayExpenses instance = null;

    /**
     *
     * @return an instance of the ConsumersPayExpenses class
     */
    public static ConsumersPayExpenses getInstance() {
        if (instance == null) {
            instance = new ConsumersPayExpenses();
        }
        return instance;
    }

    @Override
    public void computeAction(Input input, int month) {
        List<Consumer> consumers = input.getConsumers();

        for (Consumer consumer : consumers) {
            if (!consumer.isBankrupt()) {
                consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());

                if (consumer.getDebt() > 0) {
                    if (consumer.getBudget() < consumer.getDebt() + consumer.getPrice()) {
                        consumer.setBankrupt();
                    } else {
                        consumer.setBudget(consumer.getBudget() - consumer.getDebt()
                                - consumer.getPrice());
                        consumer.setDebt(0);
                    }
                } else {
                    consumer.checkConsumerInDebt();
                }
            }
        }
    }
}
