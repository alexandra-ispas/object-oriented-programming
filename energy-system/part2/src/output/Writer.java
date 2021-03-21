package output;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.nio.file.Paths;
import java.util.List;

public final class Writer {
    private final List<ConsumerOutput> consumers;
    private final List<DistributorOutput> distributors;
    private final List<ProducerOutput> energyProducers;

    public Writer(List<ConsumerOutput> consumers, List<DistributorOutput> distributors,
                  List<ProducerOutput> producers) {
        this.consumers = consumers;
        this.distributors = distributors;
        this.energyProducers = producers;
    }

    public List<ConsumerOutput> getConsumers() {
        return consumers;
    }

    public List<DistributorOutput> getDistributors() {
        return distributors;
    }

    public List<ProducerOutput> getEnergyProducers() {
        return energyProducers;
    }

    /**
     * writes the object to filePath
     *
     * @param filePath where the result should be written
     */
    public void closeJSON(final String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter objectWriter = mapper.writer(new DefaultPrettyPrinter());
            objectWriter.writeValue(Paths.get(filePath).toFile(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
