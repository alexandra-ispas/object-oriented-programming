package entities.consumers;

import common.Constants;

public final class Consumer {
    private int id;
    private long budget;
    private int monthlyIncome;
    private int contractLength;
    private boolean isBankrupt;
    private long price;
    private long debt;

    public Consumer(final int id, final int initialBudget, final int monthlyIncome) {
        this.id = id;
        this.monthlyIncome = monthlyIncome;
        this.contractLength = 0;
        this.isBankrupt = false;
        this.budget = initialBudget;
        this.price = 0;
        this.debt = 0;
    }

    /**
     * checks if a consumer has the money to pay
     * if not, he is in debt
     */
    public void checkConsumerInDebt() {

        if (this.getBudget() < this.getPrice()) {
            long newPrice = Math.round(Math.floor(Constants.DEBT * this.getPrice()));
            this.setDebt(this.getDebt() + newPrice);
        } else {
            //consumer pays
            this.setBudget(this.getBudget() - this.getPrice());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(final int monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(final int contractLength) {
        this.contractLength = contractLength;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    /**
     * makes a consumer bankrupt
     */
    public void setBankrupt() {
        isBankrupt = true;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(final long budget) {
        this.budget = budget;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(final long price) {
        this.price = price;
    }

    public long getDebt() {
        return debt;
    }

    public void setDebt(final long debt) {
        this.debt = debt;
    }
}
