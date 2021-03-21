import common.Constants;
import fileio.Input;
import fileio.InputLoader;
import output.ComputeOutput;
import output.ConsumerOutput;
import output.DistributorOutput;
import output.Writer;
import solver.Turn;
import solver.TurnFactory;

import java.util.ArrayList;
import java.util.List;


public final class Main {
    private Main() {
    }

    /**
     * the entry point for the programme
     * loads the input
     * computes round zero
     * computes the other numberOfTurns rounds
     * creates the output files
     *
     * @param args for main
     */
    public static void main(final String[] args) {

        String inputPath = "/home/alexandra/facultate/an2/poo/proiect-etapa1/checker/resources/in/basic_7.json";

        String outputPath = args[1];

        InputLoader inputLoader = new InputLoader(inputPath);
        Input input = inputLoader.readData();

        TurnFactory turnFactory = new TurnFactory();

        //object for computing the zero turn
        Turn turn = turnFactory.getTurn(Constants.PREPARE);
        turn.computeTurn(input);

        System.out.println("runda0:");
        System.out.println(input.getConsumers());
        System.out.println("\n");

        //compute the other turns
        Turn turn1 = turnFactory.getTurn(Constants.NUMBER_OF_TURNS);
        turn1.computeTurn(input);

        ComputeOutput computeOutput = new ComputeOutput(input);

        List<ConsumerOutput> outputConsumers = new ArrayList<>();
        List<DistributorOutput> outputDistributors = new ArrayList<>();

        computeOutput.createOutput(outputConsumers, outputDistributors);

        Writer fileWriter = new Writer(outputConsumers, outputDistributors);
        fileWriter.closeJSON(outputPath);
    }

}
