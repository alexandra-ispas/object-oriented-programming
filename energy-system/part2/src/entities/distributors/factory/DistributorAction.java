package entities.distributors.factory;

import entities.distributors.Distributor;
import input.Input;

public interface DistributorAction {

    /**
     * computes different actions based on a constant
     * @param distributor who is usually the cheapest distributor
     * @param input which has all the entities
     */
    void computeAction(Distributor distributor, Input input);
}
