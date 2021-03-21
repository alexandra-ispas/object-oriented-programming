package entities.distributors.factory;

import common.Constants;
import entities.distributors.factory.actions.UpdateContractLength;
import entities.distributors.factory.actions.UpdateDistributors;
import entities.distributors.factory.actions.UpdateProductionCost;

public final class DistributorFactory {

    /**
     * creates a specific action based on a constant
     * @param actionType the constant which specifies the action
     * @return an instance of the action
     */
    public DistributorAction getAction(final String actionType) {
        if (actionType == null) {
            return null;
        }

        return switch (actionType) {
            case Constants.PRODUCTION_COST -> UpdateProductionCost.getInstance();
            case Constants.UPDATE_DISTRIBUTORS -> UpdateDistributors.getInstance();
            case Constants.UPDATE_CONTRACT_LENGTH -> UpdateContractLength.getInstance();
            default -> null;
        };
    }
}
