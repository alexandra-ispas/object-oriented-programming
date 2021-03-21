package input;

import common.Constants;
import entities.consumers.Consumer;
import entities.distributors.Distributor;
import entities.producers.Producer;
import entities.updates.DistributorChanges;
import entities.updates.ProducerChanges;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class InputLoader {

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
    public Input readData() {

        JSONParser jsonParser = new JSONParser();

        long numberOfTurns = 0;
        List<Consumer> inputConsumers = new ArrayList<>();
        List<Distributor> inputDistributors = new ArrayList<>();
        List<Producer> inputProducers = new ArrayList<>();
        List<List<Consumer>> newConsumers = new ArrayList<>();
        List<List<ProducerChanges>> producerChanges = new ArrayList<>();
        List<List<DistributorChanges>> distributorChanges = new ArrayList<>();

        try {

            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(inputPath));

            numberOfTurns =
                    (long) jsonObject.get(Constants.NUMBER_OF_TURNS);

            JSONObject jsonInitialData = (JSONObject) jsonObject.get(Constants.INITIAL_DATA);

            JSONArray jsonConsumers = (JSONArray) jsonInitialData.get(Constants.CONSUMERS);

            JSONArray jsonDistributors = (JSONArray) jsonInitialData.get(Constants.DISTRIBUTORS);

            JSONArray jsonProducers = (JSONArray) jsonInitialData.get(Constants.PRODUCERS);

            if (jsonConsumers != null) {
                readConsumers(inputConsumers, jsonConsumers);
            } else {
                System.out.println(Constants.NO_CONSUMERS);
            }

            if (jsonDistributors != null) {
                for (Object jsonDistributor : jsonDistributors) {

                    if (((JSONObject) jsonDistributor).get(Constants.ID) != null
                            && ((JSONObject) jsonDistributor)
                                    .get(Constants.CONTRACT_LENGTH) != null
                            && ((JSONObject) jsonDistributor)
                                    .get(Constants.INITIAL_BUDGET) != null
                            && ((JSONObject) jsonDistributor)
                                    .get(Constants.INITIAL_INFRASTRUCTURE_COST) != null
                            && ((JSONObject) jsonDistributor)
                                    .get(Constants.ENERGY_NEEDED_KW) != null
                            && ((JSONObject) jsonDistributor)
                                    .get(Constants.PRODUCER_STRATEGY) != null) {

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
                                                .get(Constants.ENERGY_NEEDED_KW)
                                                .toString()),
                                ((JSONObject) jsonDistributor)
                                        .get(Constants.PRODUCER_STRATEGY)
                                        .toString()));
                    }
                }
            } else {
                System.out.println(Constants.NO_DISTRIBUTORS);
            }

            if (jsonProducers != null) {
                for (Object jsonDistributor : jsonProducers) {
                    if (((JSONObject) jsonDistributor).get(Constants.ID) != null
                            && ((JSONObject) jsonDistributor)
                                    .get(Constants.ENERGY_TYPE) != null
                            && ((JSONObject) jsonDistributor)
                                    .get(Constants.MAX_DISTRIBUTORS) != null
                            && ((JSONObject) jsonDistributor)
                                    .get(Constants.PRICE_KW) != null
                            && ((JSONObject) jsonDistributor)
                                    .get(Constants.ENERGY_PER_DISTRIBUTOR) != null) {
                        inputProducers.add(new Producer(
                                Integer.parseInt(
                                        ((JSONObject) jsonDistributor).get(Constants.ID)
                                                .toString()),
                                ((JSONObject) jsonDistributor)
                                        .get(Constants.ENERGY_TYPE).toString(),
                                Integer.parseInt(
                                        ((JSONObject) jsonDistributor)
                                                .get(Constants.MAX_DISTRIBUTORS)
                                                .toString()),
                                Float.parseFloat(
                                        ((JSONObject) jsonDistributor)
                                                .get(Constants.PRICE_KW)
                                                .toString()),
                                Integer.parseInt(
                                        ((JSONObject) jsonDistributor)
                                                .get(Constants.ENERGY_PER_DISTRIBUTOR)
                                                .toString())));
                    }
                }
            } else {
                System.out.println(Constants.NO_PRODUCERS);
            }

            readUpdates(jsonObject, newConsumers, producerChanges, distributorChanges);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return new Input(numberOfTurns, inputConsumers, inputDistributors, inputProducers,
                newConsumers,
                producerChanges, distributorChanges);
    }

    /**
     * reads updates from each month
     * @param jsonObject the given object to read from
     * @param newConsumers in each month
     * @param producerChanges in each month
     * @param distributorChanges in each month
     */
    public void readUpdates(final JSONObject jsonObject, final List<List<Consumer>> newConsumers,
                            final List<List<ProducerChanges>> producerChanges,
                            final List<List<DistributorChanges>> distributorChanges) {

        JSONArray jsonData = (JSONArray) jsonObject.get(Constants.MONTHLY_UPDATES);

        if (jsonData != null) {
            for (Object jsonIterator : jsonData) {

                List<Consumer> newConsumer = new ArrayList<>();
                JSONArray arrayCons =
                        ((JSONArray) ((JSONObject) jsonIterator).get(Constants.NEW_CONSUMERS));
                if (arrayCons.isEmpty()) {
                    newConsumer.add(null);
                }
                readConsumers(newConsumer, arrayCons);
                newConsumers.add(newConsumer);

                List<DistributorChanges> distributorChanges1 = new ArrayList<>();

                JSONArray arrayDis =
                        ((JSONArray) ((JSONObject) jsonIterator)
                                .get(Constants.DISTRIBUTOR_CHANGES));
                if (arrayDis.isEmpty()) {
                    distributorChanges1.add(null);
                }
                for (Object jsonCons : arrayDis) {
                    if (((JSONObject) jsonCons).get(Constants.ID) != null
                            && ((JSONObject) jsonCons).get(Constants.INFRASTRUCTURE_COST) != null) {
                        distributorChanges1.add(new DistributorChanges(
                                Integer.parseInt((
                                        (JSONObject) jsonCons).get(Constants.ID).toString()),
                                Integer.parseInt(
                                        ((JSONObject) jsonCons).get(Constants.INFRASTRUCTURE_COST)
                                                .toString())));
                    }
                }
                distributorChanges.add(distributorChanges1);


                JSONArray arrayProd =
                        ((JSONArray) ((JSONObject) jsonIterator).get(Constants.PRODUCER_CHANGES));

                List<ProducerChanges> producerChanges1 = new ArrayList<>();
                if (arrayProd.isEmpty()) {
                    producerChanges1.add(null);
                }
                for (Object jsonCons : arrayProd) {

                    if (((JSONObject) jsonCons).get(Constants.ID) != null
                            && ((JSONObject) jsonCons).
                            get(Constants.ENERGY_PER_DISTRIBUTOR) != null) {
                        producerChanges1.add(new ProducerChanges(
                                Integer.parseInt((
                                        (JSONObject) jsonCons).get(Constants.ID).toString()),
                                Integer.parseInt(
                                        ((JSONObject) jsonCons)
                                                .get(Constants.ENERGY_PER_DISTRIBUTOR)
                                                .toString())));
                        Integer.parseInt((
                                (JSONObject) jsonCons).get(Constants.ID).toString());
                    }
                }
                producerChanges.add(producerChanges1);
            }
        } else {
            System.out.println(Constants.NO_UPDATES);
        }
    }

    /**
     * reads consumers from JSON file
     * @param newConsumer consumers
     * @param arrayCons array of JSONs
     */
    private void readConsumers(List<Consumer> newConsumer, JSONArray arrayCons) {
        for (Object jsonCons : arrayCons) {
            if (((JSONObject) jsonCons).get(Constants.ID) != null
                    && ((JSONObject) jsonCons).get(Constants.INITIAL_BUDGET) != null
                    && ((JSONObject) jsonCons).get(Constants.MONTHLY_INCOME) != null) {
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
        }
    }
}
