package entities.distributors;

import common.Constants;
import input.Input;

import java.util.List;

public final class GetTheBestDistributor {

    private static GetTheBestDistributor instance = null;

    /**
     *
     * @return an instance of this class
     */
    public static GetTheBestDistributor getInstance() {
        if (instance == null) {
            instance = new GetTheBestDistributor();
        }

        return instance;
    }

    /**
     *
     * @param input with all the entities
     * @return the cheapest distributor
     */
    public Distributor computeAction(Input input) {
        List<Distributor> distributors = input.getDistributors();
        Distributor bestDistributor = null;
        long minPrice = Integer.MAX_VALUE;

        for (Distributor distributor : distributors) {

            if (!distributor.isBankrupt()) {

                long prof = Math.round(Math.floor(
                        Constants.PROFIT_PERCENTAGE * distributor.getProductionCost()));
                distributor.setProfit(prof);

                //price for distributor with no clients
                long price =
                        distributor.getInfrastructureCost() + distributor.getProductionCost()
                                + distributor.getProfit();

                //computes the price if the distributor has clients
                if (distributor.getConsumers() != null) {
                    if (distributor.getConsumers().size() != 0) {
                        price = Math.round(Math.floor(distributor.getInfrastructureCost()
                                / distributor.getConsumers().size()))
                                + distributor.getProductionCost()
                                + distributor.getProfit();
                    }
                }

                distributor.setFinalPrice(price);

                if (distributor.getFinalPrice() < minPrice) {
                    minPrice = distributor.getFinalPrice();
                    bestDistributor = distributor;
                }
            }
        }
        return bestDistributor;
    }
}
