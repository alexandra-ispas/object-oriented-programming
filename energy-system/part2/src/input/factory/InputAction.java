package input.factory;

import input.Input;

public interface InputAction {
    /**
     * computes a specific action for a given input and a moth
     * @param input with all the entities
     * @param month when action occurs
     */
    void computeAction(Input input, int month);
}
