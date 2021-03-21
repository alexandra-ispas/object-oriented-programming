package entities.consumers;

import entities.distributors.Distributor;


public final class AddContract {

    private static AddContract instance = null;

    /**
     *
     * @return an instance of the AddContract class
     */
    public static AddContract getInstance() {
        if (instance == null) {
            instance = new AddContract();
        }
        return instance;
    }

    /**
     *
     * @param consumer who is assigned a new contract
     * @param bestDistributor who has the cheapest contract
     */
    public void computeAction(final Consumer consumer, final Distributor bestDistributor) {

        consumer.setPrice(bestDistributor.getFinalPrice());
        consumer.setContractLength(bestDistributor.getContractLength());
        bestDistributor.addConsumer(consumer);

    }
}
