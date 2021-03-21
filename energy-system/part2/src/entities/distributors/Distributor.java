package entities.distributors;

import entities.consumers.Consumer;
import entities.producers.observer.Observer;
import strategies.strategyType.CreateStrategyType;
import strategies.strategyType.EnergyChoiceStrategyType;

import java.util.ArrayList;
import java.util.List;

public final class Distributor extends Observer {
    private final int id;
    private final int contractLength;
    private final long energyNeededKW;
    private long budget;
    private int infrastructureCost;
    private final EnergyChoiceStrategyType producerStrategy;
    private int productionCost;
    private long finalPrice;
    private long profit;
    private boolean isBankrupt;
    private int monthlyCosts;
    private List<Consumer> consumers;
    private boolean updatable;

    public Distributor(final int id, final int contractLength, final long budget,
                       final int infrastructureCost,
                       final long energyNeededKW, final String producerStrategy) {
        this.id = id;
        this.contractLength = contractLength;
        this.budget = budget;
        this.infrastructureCost = infrastructureCost;
        this.energyNeededKW = energyNeededKW;
        this.producerStrategy = CreateStrategyType.getInstance().createStrategy(producerStrategy);
        this.isBankrupt = false;
        this.finalPrice = 0;
        this.profit = 0;
        this.monthlyCosts = 0;
        this.consumers = new ArrayList<>();
        this.updatable = false;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    /**
     * make the distributor not updatable
     */
    public void resetUpdate() {
        this.updatable = false;
    }

    public int getId() {
        return id;
    }

    public int getContractLength() {
        return contractLength;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(final int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public int getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(final int productionCost) {
        this.productionCost = productionCost;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(final long budget) {
        this.budget = budget;
    }

    public long getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(final long finalPrice) {
        this.finalPrice = finalPrice;
    }

    public long getProfit() {
        return profit;
    }

    public void setProfit(final long profit) {
        this.profit = profit;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    /**
     * makes a distributor bankrupt
     */
    public void setBankrupt() {
        this.isBankrupt = true;
    }

    public int getMonthlyCosts() {
        return monthlyCosts;
    }

    public void setMonthlyCosts(final int monthlyCosts) {
        this.monthlyCosts = monthlyCosts;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<Consumer> consumers) {
        this.consumers = consumers;
    }

    /**
     * adds a consumer who has this distributor
     *
     * @param consumer to be added to the list
     */
    public void addConsumer(final Consumer consumer) {
        this.consumers.add(consumer);
    }

    /**
     * removes a consumer from the distributor's list
     *
     * @param consumer to be removed from the list
     */
    public void removeConsumer(final Consumer consumer) {
        this.consumers.remove(consumer);
    }

    public long getEnergyNeededKW() {
        return energyNeededKW;
    }

    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    @Override
    public void update() {
        this.updatable = true;
    }
}
