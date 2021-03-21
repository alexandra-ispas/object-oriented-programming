package entities.energyType;

import common.Constants;

public final class CreateEnergyType {

    private static CreateEnergyType instance = null;

    /**
     * @return an instance of this class
     */
    public static CreateEnergyType getInstance() {
        if (instance == null) {
            instance = new CreateEnergyType();
        }
        return instance;
    }

    /**
     *
     * @param energyType string specifying the type of energy
     * @return an object of type EnergyType with the label 'energyType'
     */
    public EnergyType createEnergyType(final String energyType) {
        switch (energyType) {
            case Constants.WIND -> {
                return EnergyType.WIND;
            }
            case Constants.SOLAR -> {
                return EnergyType.SOLAR;
            }
            case Constants.HYDRO -> {
                return EnergyType.HYDRO;
            }
            case Constants.COAL -> {
                return EnergyType.COAL;
            }
            case Constants.NUCLEAR -> {
                return EnergyType.NUCLEAR;
            }

            default -> {
                return EnergyType.NONE;
            }
        }
    }
}
