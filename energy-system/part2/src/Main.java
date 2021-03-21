import input.Input;
import input.InputLoader;
import output.ComputeOutput;
import output.ConsumerOutput;
import output.DistributorOutput;
import output.ProducerOutput;
import output.Writer;
import solver.Solver;

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

        String inputPath = args[0];
        String outputPath = args[1];

        InputLoader inputLoader = new InputLoader(inputPath);
        Input input = inputLoader.readData();

        Solver.getInstance().computeTurns(input);

        ComputeOutput computeOutput = new ComputeOutput(input);

        List<ConsumerOutput> outputConsumers = new ArrayList<>();
        List<DistributorOutput> outputDistributors = new ArrayList<>();
        List<ProducerOutput> outputProducers = new ArrayList<>();

        computeOutput.createOutput(outputConsumers, outputDistributors, outputProducers);

        Writer fileWriter = new Writer(outputConsumers, outputDistributors, outputProducers);
        fileWriter.closeJSON(outputPath);
    }
}
