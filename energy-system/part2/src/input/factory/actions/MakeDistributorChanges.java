package input.factory.actions;

import entities.distributors.Distributor;
import entities.updates.DistributorChanges;
import input.Input;
import input.factory.InputAction;

import java.util.List;

public final class MakeDistributorChanges implements InputAction {

    private static MakeDistributorChanges instance = null;

    /**
     *
     * @return an instance of the MakeDistributorChanges class
     */
    public static MakeDistributorChanges getInstance() {
        if (instance == null) {
            instance = new MakeDistributorChanges();
        }
        return instance;
    }

    @Override
    public void computeAction(final Input input, final int month) {
        List<DistributorChanges> distributorChanges = input.getDistributorChanges().get(month);
        List<Distributor> distributors = input.getDistributors();

        for (DistributorChanges change : distributorChanges) {
            if (change != null) {
                for (Distributor distributor : distributors) {
                    if (distributor.getId() == change.getId()) {
                        distributor.setInfrastructureCost(change.getInfrastructureCost());
                    }
                }
            }
        }
    }
}
