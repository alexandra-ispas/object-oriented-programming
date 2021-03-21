package solver;

import common.Constants;
import entities.consumers.AddContract;
import entities.consumers.Consumer;
import entities.distributors.Distributor;
import entities.distributors.GetTheBestDistributor;
import entities.distributors.factory.DistributorFactory;
import entities.producers.Producer;
import input.Input;
import input.factory.InputFactory;

public final class Solver {

    private static Solver instance = null;

    private Solver() {
    }

    /**
     * implements singleton design pattern
     *
     * @return instance of this class
     */
    public static Solver getInstance() {
        if (instance == null) {
            instance = new Solver();
        }
        return instance;
    }

    /**
     * modifies the input based on the round
     */
    public void computeTurns(final Input input) {

        InputFactory inputFactory = new InputFactory();
        DistributorFactory distributorFactory = new DistributorFactory();
        AddContract addContract = AddContract.getInstance();

        for (Distributor distributor : input.getDistributors()) {
            distributorFactory.getAction(Constants.PRODUCTION_COST)
                    .computeAction(distributor, input);
        }

        Distributor bestDistributor = GetTheBestDistributor.getInstance().computeAction(input);

        for (Consumer consumer : input.getConsumers()) {
            addContract.computeAction(consumer, bestDistributor);
            consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());
            consumer.checkConsumerInDebt();
        }

        inputFactory.getAction(Constants.DISTRIBUTORS_PAY).computeAction(input, -1);

        for (long roundNO = 0; roundNO < input.getNumberOfTurns(); roundNO++) {

            inputFactory.getAction(Constants.DISTRIBUTOR_CHANGES)
                    .computeAction(input, (int) roundNO);
            inputFactory.getAction(Constants.PRODUCER_CHANGES).computeAction(input, (int) roundNO);

            bestDistributor = GetTheBestDistributor.getInstance().computeAction(input);

            distributorFactory.getAction(Constants.UPDATE_CONTRACT_LENGTH)
                    .computeAction(bestDistributor, input);

            for (Consumer cons : input.getNewConsumers().get((int) roundNO)) {
                if (cons != null) {
                    addContract.computeAction(cons, bestDistributor);
                    input.addNewConsumer(cons);
                }
            }

            inputFactory.getAction(Constants.CONSUMERS_PAY).computeAction(input, (int) roundNO);

            inputFactory.getAction(Constants.DISTRIBUTORS_PAY).computeAction(input, (int) roundNO);

            if (bestDistributor.isBankrupt()) {
                bestDistributor = GetTheBestDistributor.getInstance().computeAction(input);
            }

            distributorFactory.getAction(Constants.UPDATE_DISTRIBUTORS)
                    .computeAction(bestDistributor, input);
            inputFactory.getAction(Constants.REMOVE_BANKRUPT_CONSUMERS)
                    .computeAction(input, (int) roundNO);

            for (Producer producer : input.getProducers()) {
                producer.addDistributors((int) roundNO + 1);
            }
        }
    }
}
