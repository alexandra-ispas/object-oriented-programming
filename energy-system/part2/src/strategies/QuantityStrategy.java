package strategies;

import entities.producers.Producer;

import java.util.List;

public final class QuantityStrategy implements Strategy {
    @Override
    public void sort(final List<Producer> producers) {
        producers.sort((o1, o2) -> {
            if ((o1.getEnergyPerDistributor() - o2.getEnergyPerDistributor()) < 0) {
                return 1;
            } else if ((o1.getEnergyPerDistributor() - o2.getEnergyPerDistributor()) > 0) {
                return -1;
            } else {
                return 0;
            }
        });
    }
}
