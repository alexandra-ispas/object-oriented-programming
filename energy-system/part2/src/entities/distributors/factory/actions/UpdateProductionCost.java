package entities.distributors.factory.actions;

import common.Constants;
import entities.distributors.Distributor;
import entities.distributors.factory.DistributorAction;
import entities.producers.Producer;
import input.Input;
import strategies.ContextStrategy;
import strategies.strategyType.EnergyChoiceStrategyType;
import strategies.GreenStrategy;
import strategies.PriceStrategy;
import strategies.QuantityStrategy;

import java.util.Comparator;
import java.util.List;

public final class UpdateProductionCost implements DistributorAction {

    private static UpdateProductionCost instance = null;

    public UpdateProductionCost() {
    }

    /**
     *
     * @return instance of UpdateProductionCost
     */
    public static UpdateProductionCost getInstance() {
        if (instance == null) {
            instance = new UpdateProductionCost();
        }
        return instance;
    }

    @Override
    public void computeAction(final Distributor distributor, final Input input) {

        List<Producer> producers = input.getProducers();

        producers.sort(Comparator.comparingInt(Producer::getId));

        //sort producers for green strategy
        if (distributor.getProducerStrategy().equals(EnergyChoiceStrategyType.GREEN)) {
            ContextStrategy contextStrategy = new ContextStrategy(new GreenStrategy());
            contextStrategy.executeStrategy(producers);
        }

        //sort producers for price strategy
        if (distributor.getProducerStrategy().equals(EnergyChoiceStrategyType.PRICE)) {
            ContextStrategy contextStrategy = new ContextStrategy(new PriceStrategy());
            contextStrategy.executeStrategy(producers);
        }

        //sort producers for quantity strategy
        if (distributor.getProducerStrategy().equals(EnergyChoiceStrategyType.QUANTITY)) {
            ContextStrategy contextStrategy = new ContextStrategy(new QuantityStrategy());
            contextStrategy.executeStrategy(producers);
        }

        long energy = distributor.getEnergyNeededKW();
        float cost = 0;

        for (Producer producer : producers) {
            if (producer.getMaxDistributors() > producer.getObservers().size()) {
                producer.attach(distributor);
                cost += producer.getEnergyPerDistributor() * producer.getPrice();
                energy -= producer.getEnergyPerDistributor();
            }
            if (energy <= 0) {
                break;
            }
        }

        distributor.setProductionCost((int) Math.round(Math.floor(cost
                / Constants.PRODUCTION_COST_NUMBER)));
        distributor.resetUpdate();
    }
}
