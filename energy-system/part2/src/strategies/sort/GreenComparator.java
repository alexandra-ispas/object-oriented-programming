package strategies.sort;

import entities.producers.Producer;

import java.util.Comparator;

public final class GreenComparator implements Comparator<Producer> {
    @Override
    public int compare(Producer o1, Producer o2) {
        if (o1.getEnergyType().isRenewable() && !o2.getEnergyType().isRenewable()) {
            return -1;
        }
        if (!o1.getEnergyType().isRenewable() && o2.getEnergyType().isRenewable()) {
            return 1;
        }
        return 0;
    }
}
