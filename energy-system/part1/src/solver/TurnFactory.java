package solver;

import common.Constants;

public final class TurnFactory {

    /**
     * creates an object based on a given string
     * @param turnType
     * @return a TurnZero or an AllTurns object
     */
    public Turn getTurn(final String turnType) {
        if (turnType == null) {
            return null;
        }

        if (turnType.equals(Constants.PREPARE)) {
            return TurnZero.getInstance();
        } else {
            return AllTurns.getInstance();
        }
    }
}
