package strategies.strategyType;

import common.Constants;

public final class CreateStrategyType {

    private static CreateStrategyType instance = null;

    /**
     *
     * @return an instance of this class
     */
    public static CreateStrategyType getInstance() {
        if (instance == null) {
            instance = new CreateStrategyType();
        }
        return instance;
    }

    /**
     *
     * @param producerStrategy the label for a EnergyChoiceStrategyType object
     * @return an object of type EnergyChoiceStrategyType
     */
    public EnergyChoiceStrategyType createStrategy(final String producerStrategy) {

        switch (producerStrategy) {
            case Constants.GREEN -> {
                return EnergyChoiceStrategyType.GREEN;
            }
            case Constants.PRICE -> {
                return EnergyChoiceStrategyType.PRICE;
            }
            case Constants.QUANTITY -> {
                return EnergyChoiceStrategyType.QUANTITY;
            }
            default -> {
                return EnergyChoiceStrategyType.NONE;
            }
        }
    }
}
