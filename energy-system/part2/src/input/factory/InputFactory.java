package input.factory;

import common.Constants;
import input.factory.actions.ConsumersPayExpenses;
import input.factory.actions.DistributorsPayExpenses;
import input.factory.actions.MakeDistributorChanges;
import input.factory.actions.MakeProducerChanges;
import input.factory.actions.RemoveBankruptConsumers;

public final class InputFactory {

    /**
     * creates a specific action based on a constant
     * @param actionType the type of action required
     * @return an instance of the action
     */
    public InputAction getAction(final String actionType) {
        if (actionType == null) {
            return null;
        }

        return switch (actionType) {
            case Constants.DISTRIBUTOR_CHANGES -> MakeDistributorChanges.getInstance();
            case Constants.PRODUCER_CHANGES -> MakeProducerChanges.getInstance();

            case Constants.DISTRIBUTORS_PAY -> DistributorsPayExpenses.getInstance();
            case Constants.REMOVE_BANKRUPT_CONSUMERS -> RemoveBankruptConsumers.getInstance();
            case Constants.CONSUMERS_PAY -> ConsumersPayExpenses.getInstance();
            default -> null;
        };
    }
}
