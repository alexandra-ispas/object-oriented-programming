package strategies.sort;

import entities.producers.Producer;

import java.util.Comparator;

public final class PriceQuantityComparator implements Comparator<Producer> {

    @Override
    public int compare(Producer o1, Producer o2) {

        if (o1.getPrice() == o2.getPrice()) {
            if ((o2.getEnergyPerDistributor() - o1.getEnergyPerDistributor()) < 0) {
                return -1;
            } else {
                return 1;
            }
        } else {
            if ((o1.getPrice() - o2.getPrice()) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }

}
