package output;

public final class ContractOutput {
    private final int consumerId;
    private final long price;
    private final int remainedContractMonths;

    public ContractOutput(final int consumerId, final long price,
                          final int remainedContractMonths) {
        this.consumerId = consumerId;
        this.price = price;
        this.remainedContractMonths = remainedContractMonths;
    }

    public int getConsumerId() {
        return consumerId;
    }

    public long getPrice() {
        return price;
    }

    public int getRemainedContractMonths() {
        return remainedContractMonths;
    }

    @Override
    public String toString() {
        return "{"
                + "consumerId=" + consumerId
                + ", price=" + price
                + ", remainedContractMonths=" + remainedContractMonths
                + '}';
    }
}
