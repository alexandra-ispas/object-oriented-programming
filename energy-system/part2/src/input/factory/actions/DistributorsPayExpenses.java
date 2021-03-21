package input.factory.actions;

import entities.consumers.Consumer;
import entities.distributors.Distributor;
import input.Input;
import input.factory.InputAction;

import java.util.List;

public final class DistributorsPayExpenses implements InputAction {

    private static DistributorsPayExpenses instance = null;

    /**
     *
     * @return  an instance of the DistributorsPayExpenses class
     */
    public static DistributorsPayExpenses getInstance() {
        if (instance == null) {
            instance = new DistributorsPayExpenses();
        }
        return instance;
    }

    @Override
    public void computeAction(Input input, int month) {

        List<Distributor> distributors = input.getDistributors();
        for (Distributor distributor : distributors) {
            if (!distributor.isBankrupt()) {
                int consumerMoney = 0;
                if (distributor.getConsumers() != null) {
                    if (distributor.getConsumers().size() != 0) {
                        for (Consumer consumer : distributor.getConsumers()) {
                            if (consumer.isBankrupt()) {
                                //distributor pays production cost
                                //for bankrupt consumer
                                consumerMoney -= distributor.getProductionCost();
                            } else if (consumer.getDebt() == 0) {
                                consumerMoney += consumer.getPrice();
                            }
                        }
                    }
                }
                //get the number of non-bankrupt consumers
                int size = 0;
                if (distributor.getConsumers() != null) {
                    for (Consumer consumer : distributor.getConsumers()) {
                        if (!consumer.isBankrupt()) {
                            size++;
                        }
                    }
                }
                distributor.setMonthlyCosts(distributor.getInfrastructureCost()
                        + size * distributor.getProductionCost());
                distributor.setBudget(distributor.getBudget()
                        - distributor.getMonthlyCosts() + consumerMoney);

                //check if distributor is bankrupt after updates
                if (distributor.getBudget() < 0) {
                    distributor.setBankrupt();
                }
            }
        }
    }
}
