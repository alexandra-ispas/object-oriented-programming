package solver;

import fileio.Input;

public interface Turn {
    /**
     * computes the specific modifications
     * to the input according to the type of turn
     * @param input
     */
    void computeTurn(Input input);
}
