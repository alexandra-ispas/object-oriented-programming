package output;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.nio.file.Paths;
import java.util.List;

public final class Writer {
    private final List<ConsumerOutput> consumers;
    private final List<DistributorOutput> distributors;

    public Writer(final List<ConsumerOutput> consumersOut,
                  final List<DistributorOutput> distributorOut) {
        this.consumers = consumersOut;
        this.distributors = distributorOut;
    }

    public List<ConsumerOutput> getConsumers() {
        return consumers;
    }

    public List<DistributorOutput> getDistributors() {
        return distributors;
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
