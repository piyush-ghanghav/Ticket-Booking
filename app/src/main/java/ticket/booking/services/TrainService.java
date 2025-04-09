package ticket.booking.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;/**/
import com.fasterxml.jackson.databind.ObjectMapper;

import ticket.booking.entities.Train;

public class TrainService {

    // Update path to be relative to project root
    private static final String TRAINS_PATH = "./src/main/resources/localDb/trains.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Train train;
    private List<Train> trainList;

    public TrainService() throws IOException {
        loadTrains();
    }

    public TrainService(Train train) throws IOException {
        this.train = train;
        loadTrains();
    }

    private void loadTrains() throws IOException {
        File trainFile = new File(TRAINS_PATH);
        System.out.println("Loading Trains");
        if (!trainFile.exists()) {
            System.out.println("File Not found");
            trainList = List.of();  // empty list fallback
        } else {

            trainList = objectMapper.readValue(trainFile, new TypeReference<List<Train>>() {});
            System.out.println("Loaded " + trainList.size() + " trains");
        }
        System.out.println("Done");
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream()
                .filter(train -> validTrain(train, source, destination))
                .collect(Collectors.toList());
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations();
        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());
        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }
}
