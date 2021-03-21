package output;

import java.util.List;

public final class DistributorOutput {
    private final int id;
    private final long budget;
    private final boolean isBankrupt;
    private final List<ContractOutput> contracts;

    public DistributorOutput(final int id, final long budget, final boolean isBankrupt,
                             final List<ContractOutput> contracts) {
        this.id = id;
        this.budget = budget;
        this.isBankrupt = isBankrupt;
        this.contracts = contracts;
    }

    public int getId() {
        return id;
    }

    public boolean getIsBankrupt() {
        return this.isBankrupt;
    }

    public long getBudget() {
        return budget;
    }

    public List<ContractOutput> getContracts() {
        return contracts;
    }

    @Override
    public String toString() {
        return "{"
                + "id=" + id
                + ", budget=" + budget
                + ", contracts=" + contracts
                + '}';
    }
}

