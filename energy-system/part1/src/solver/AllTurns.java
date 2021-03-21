package solver;

import common.Constants;
import fileio.Consumer;
import fileio.CostChanges;
import fileio.Distributor;
import fileio.Input;

import java.util.List;

public final class AllTurns implements Turn {

    private static AllTurns instance = null;

    private AllTurns() {
    }

    /**
     * implements singleton design pattern
     * @return instance of this class
     */
    public static AllTurns getInstance() {
        if (instance == null) {
            instance = new AllTurns();
        }
        return instance;
    }

    /**
     * computes the price and profit for a distributor
     *
     * @param input from input file
     * @return the cheapest distributor
     */
    public Distributor getBestDistributor(final Input input) {
        List<Distributor> distributors = input.getDistributors();

        long minPrice = Integer.MAX_VALUE;
        Distributor bestDistributor = null;

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

    /**
     * modifies the input based on the round
     */
    @Override
    public void computeTurn(final Input input) {

        for (long roundNO = 0; roundNO < input.getNumberOfTurns(); roundNO++) {
            System.out.println("runda = " + roundNO);

            List<Consumer> newConsumers = input.getNewConsumers().get((int) roundNO);
            List<CostChanges> costChanges = input.getCostChanges().get((int) roundNO);
            List<Consumer> oldConsumers = input.getConsumers();
            List<Distributor> distributors = input.getDistributors();

            for (CostChanges change : costChanges) {
                if (change != null) {
                    for (Distributor distr : distributors) {

                        if (distr.getId() == change.getId()) {
                            distr.setInfrastructureCost(change.getInfrastructureCost());
                            distr.setProductionCost(change.getProductionCost());
                        }
                    }
                }
            }

            Distributor bestDistributor = getBestDistributor(input);
            long minPrice = bestDistributor.getFinalPrice();

            for (Consumer consumer : oldConsumers) {
                if (!consumer.isBankrupt()) {
                    consumer.setContractLength(consumer.getContractLength() - 1);
                    if (consumer.getContractLength() == 0) {
                        for (Distributor distributor : distributors) {
                            if (distributor.getConsumers() != null) {
                                if (distributor.getConsumers().contains(consumer)) {
                                    distributor.removeConsumer(consumer);
                                }
                            }
                        }
                    }
                }
            }
            for (Consumer consumer : oldConsumers) {
                if (!consumer.isBankrupt()) {
                    if (consumer.getContractLength() == 0) {
                        consumer.setPrice(minPrice);
                        consumer.setContractLength(bestDistributor.getContractLength());
                        bestDistributor.addConsumer(consumer);
                    }
                }
            }
            for (Consumer cons : newConsumers) {
                if (cons != null) {
                    cons.setPrice(minPrice);
                    cons.setContractLength(bestDistributor.getContractLength());
                    bestDistributor.addConsumer(cons);
                    oldConsumers.add(cons);
                }
            }
            for (Consumer consumer : oldConsumers) {
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
                        TurnZero.checkConsumerInDebt(consumer);
                    }
                }
            }
            TurnZero.updateDistributor(distributors);

            if (bestDistributor.isBankrupt()) {
                bestDistributor = getBestDistributor(input);
            }

            for (Distributor distributor : distributors) {
                if (distributor.isBankrupt()) {
                    if (distributor.getConsumers() != null) {
                        for (Consumer consumer : distributor.getConsumers()) {
                            consumer.setPrice(minPrice);
                            bestDistributor.addConsumer(consumer);
                            consumer.setContractLength(bestDistributor.getContractLength());
                        }
                    }
                }
            }
            for (Consumer consumer : oldConsumers) {
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
            System.out.println(input.getConsumers());
        }
    }
}
