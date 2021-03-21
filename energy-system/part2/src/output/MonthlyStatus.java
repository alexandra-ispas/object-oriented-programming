package output;

import java.util.List;

public final class MonthlyStatus {

    private final int month;
    private final List<Integer> distributorsIds;

    public MonthlyStatus(final int month, final List<Integer> distributorsId) {
        this.month = month;
        this.distributorsIds = distributorsId;
    }
    public int getMonth() {
        return month;
    }

    public List<Integer> getDistributorsIds() {
        return distributorsIds;
    }
}
