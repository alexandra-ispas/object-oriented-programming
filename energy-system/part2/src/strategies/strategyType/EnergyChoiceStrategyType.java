package strategies.strategyType;

/**
 * Strategy types for distributors to choose their producers
 */
public enum EnergyChoiceStrategyType {
    GREEN("GREEN"),
    PRICE("PRICE"),
    QUANTITY("QUANTITY"),
    NONE("NONE");

    private final String label;

    EnergyChoiceStrategyType(final String label) {
        this.label = label;
    }

}
