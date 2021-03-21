package output;

import entities.energyType.EnergyType;

import java.util.List;

public final class ProducerOutput {
    private final int id;
    private final int maxDistributors;
    private final float priceKW;
    private final EnergyType energyType;
    private final long energyPerDistributor;
    private final List<MonthlyStatus> monthlyStats;

    public ProducerOutput(final int id, final int maxDistributors, final float priceKW,
                          final EnergyType energyType, final long energyPerDistributor,
                          final List<MonthlyStatus> monthlyStats) {
        this.id = id;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyType = energyType;
        this.energyPerDistributor = energyPerDistributor;
        this.monthlyStats = monthlyStats;
    }

    public int getId() {
        return id;
    }

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public float getPriceKW() {
        return priceKW;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public List<MonthlyStatus> getMonthlyStats() {
        return monthlyStats;
    }
}
