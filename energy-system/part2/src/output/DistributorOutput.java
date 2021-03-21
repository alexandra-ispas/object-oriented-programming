package output;

import strategies.strategyType.EnergyChoiceStrategyType;

import java.util.List;

public final class DistributorOutput {
    private final int id;
    private final long energyNeededKW;
    private final long contractCost;
    private final long budget;
    private final EnergyChoiceStrategyType producerStrategy;
    private final boolean isBankrupt;
    private final List<ContractOutput> contracts;

    public DistributorOutput(int id, long energyNeededKW, long contractCost, long budget,
                             EnergyChoiceStrategyType producerStrategy, boolean isBankrupt,
                             List<ContractOutput> contracts) {
        this.id = id;
        this.energyNeededKW = energyNeededKW;
        this.contractCost = contractCost;
        this.budget = budget;
        this.producerStrategy = producerStrategy;
        this.isBankrupt = isBankrupt;
        this.contracts = contracts;
    }

    public int getId() {
        return id;
    }

    public long getEnergyNeededKW() {
        return energyNeededKW;
    }

    public long getContractCost() {
        return contractCost;
    }

    public long getBudget() {
        return budget;
    }

    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    public List<ContractOutput> getContracts() {
        return contracts;
    }
}

