package fileio;

import common.Constants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputLoader {

    private final String inputPath;

    public InputLoader(final String inputPath) {
        this.inputPath = inputPath;
    }

    /**
     * reads data from input file
     * and stores it
     *
     * @return data from json file
     */
    public final Input readData() {

        JSONParser jsonParser = new JSONParser();

        long numberOfTurns = 0;
        List<Consumer> inputConsumers = new ArrayList<>();
        List<Distributor> inputDistributors = new ArrayList<>();
        List<List<Consumer>> newConsumers = new ArrayList<>();
        List<List<CostChanges>> costChanges = new ArrayList<>();

        try {

            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(inputPath));

            numberOfTurns =
                    (long) jsonObject.get(Constants.NUMBER_OF_TURNS);

            JSONObject jsonInitialData = (JSONObject) jsonObject.get(Constants.INITIAL_DATA);

            JSONArray jsonConsumers = (JSONArray) jsonInitialData.get(Constants.CONSUMERS);

            JSONArray jsonDistributors = (JSONArray) jsonInitialData.get(Constants.DISTRIBUTORS);

            if (jsonConsumers != null) {
                for (Object jsonConsumer : jsonConsumers) {
                    inputConsumers.add(new Consumer(Integer.parseInt(
                            ((JSONObject) jsonConsumer).get(Constants.ID).toString()),
                            Integer.parseInt((
                                    (JSONObject) jsonConsumer).get(Constants.INITIAL_BUDGET)
                                    .toString()),
                            Integer.parseInt(
                                    ((JSONObject) jsonConsumer).get(Constants.MONTHLY_INCOME)
                                            .toString())));
                }
            } else {
                System.out.println(Constants.NO_CONSUMERS);
            }

            if (jsonDistributors != null) {
                for (Object jsonDistributor : jsonDistributors) {
                    inputDistributors.add(new Distributor(
                            Integer.parseInt(
                                    ((JSONObject) jsonDistributor).get(Constants.ID)
                                            .toString()),
                            Integer.parseInt(
                                    ((JSONObject) jsonDistributor)
                                            .get(Constants.CONTRACT_LENGTH)
                                            .toString()),
                            Integer.parseInt(
                                    ((JSONObject) jsonDistributor)
                                            .get(Constants.INITIAL_BUDGET)
                                            .toString()),
                            Integer.parseInt(
                                    ((JSONObject) jsonDistributor)
                                            .get(Constants.INITIAL_INFRASTRUCTURE_COST)
                                            .toString()),
                            Integer.parseInt(
                                    ((JSONObject) jsonDistributor)
                                            .get(Constants.INITIAL_PRODUCTION_COST)
                                            .toString())));
                }
            } else {
                System.out.println(Constants.NO_DISTRIBUTORS);
            }

            readUpdates(jsonObject, newConsumers, costChanges);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return new Input(numberOfTurns, inputConsumers, inputDistributors, newConsumers,
                costChanges);
    }

    /**
     * reads monthly updates from input
     *
     * @param jsonObject created for reading
     * @param newConsumers from input
     * @param costChanges from input
     */
    public void readUpdates(final JSONObject jsonObject, final List<List<Consumer>> newConsumers,
                            final List<List<CostChanges>> costChanges) {

        JSONArray jsonData = (JSONArray) jsonObject.get(Constants.MONTHLY_UPDATES);

        if (jsonData != null) {
            for (Object jsonIterator : jsonData) {

                List<Consumer> newConsumer = new ArrayList<>();
                JSONArray arrayCons =
                        ((JSONArray) ((JSONObject) jsonIterator).get(Constants.NEW_CONSUMERS));
                if (arrayCons.isEmpty()) {
                    newConsumer.add(null);
                }
                for (Object jsonCons : arrayCons) {

                    newConsumer.add(new Consumer(
                            Integer.parseInt((
                                    (JSONObject) jsonCons).get(Constants.ID).toString()),
                            Integer.parseInt(
                                    ((JSONObject) jsonCons).get(Constants.INITIAL_BUDGET)
                                            .toString()),
                            Integer.parseInt(
                                    ((JSONObject) jsonCons).get(Constants.MONTHLY_INCOME)
                                            .toString())));
                }
                newConsumers.add(newConsumer);

                JSONArray arrayCostChg =
                        ((JSONArray) ((JSONObject) jsonIterator).get(Constants.COSTS_CHANGES));
                List<CostChanges> costChg = new ArrayList<>();
                if (arrayCostChg.isEmpty()) {
                    costChg.add(null);
                }
                for (Object jsonCons : arrayCostChg) {

                    costChg.add(new CostChanges(
                            Integer.parseInt((
                                    (JSONObject) jsonCons).get(Constants.ID).toString()),
                            Integer.parseInt(
                                    ((JSONObject) jsonCons)
                                            .get(Constants.INFRASTRUCTURE_COST)
                                            .toString()),
                            Integer.parseInt(
                                    ((JSONObject) jsonCons)
                                            .get(Constants.PRODUCTION_COST)
                                            .toString())));
                }
                costChanges.add(costChg);
            }
        } else {
            System.out.println(Constants.NO_UPDATES);
        }
    }
}
