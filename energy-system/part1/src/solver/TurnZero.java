package solver;

import common.Constants;
import fileio.Consumer;
import fileio.Distributor;
import fileio.Input;

import java.util.List;

public final class TurnZero implements Turn {

    private static TurnZero instance = null;

    public TurnZero() {
    }

    /**
     * implements singleton desingn pattern
     * @return instance of this class
     */
    public static TurnZero getInstance() {
        if (instance == null) {
            instance = new TurnZero();
        }
        return instance;
    }

    /**
     * updates monthlyCost and budget for distributor
     *
     * @param distributors to be modified
     */
    public static void updateDistributor(final List<Distributor> distributors) {
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

    /**
     * modifies input to perform round zero
     */
    @Override
    public void computeTurn(final Input input) {

        List<Consumer> consumers = input.getConsumers();
        List<Distributor> distributors = input.getDistributors();

        Distributor bestDistributor = getBestDistributor(input);

        long minPrice = bestDistributor.getFinalPrice();

        for (Consumer consumer : consumers) {
            consumer.setPrice(minPrice);
            consumer.setContractLength(bestDistributor.getContractLength());

            bestDistributor.addConsumer(consumer);

            consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());

            checkConsumerInDebt(consumer);
        }
        //updates monthlyCost and budget for distributor
        updateDistributor(distributors);
    }

    /**
     * checks if a consumer has the money to pay
     * if not, he is in debt
     * @param consumer who has to pay
     */
    static void checkConsumerInDebt(final Consumer consumer) {
        if (consumer.getBudget() < consumer.getPrice()) {
            long price = Math.round(Math.floor(Constants.DEBT * consumer.getPrice()));
            consumer.setDebt(consumer.getDebt() + price);
        } else {
            //consumer pays
            consumer.setBudget(consumer.getBudget() - consumer.getPrice());
        }
    }

    /**
     * computes the price and profit
     * for each distributor
     * @param input from JSON file
     * @return the cheapest distributor
     */
    public Distributor getBestDistributor(final Input input) {
        Distributor bestDistributor = null;
        long minPrice = Integer.MAX_VALUE;

        List<Distributor> distributors = input.getDistributors();
        for (Distributor distributor : distributors) {

            long prof =
                    Math.round(Math.floor(
                            Constants.PROFIT_PERCENTAGE * distributor.getProductionCost()));

            distributor.setProfit(prof);

            long price = distributor.getInfrastructureCost() + distributor.getProductionCost()
                    + distributor.getProfit();
            distributor.setFinalPrice(price);

            if (price < minPrice) {
                minPrice = price;
                bestDistributor = distributor;
            }
        }
        return bestDistributor;
    }
}
