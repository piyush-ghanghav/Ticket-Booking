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
    private static final String TRAINS_PATH = "./app/src/main/resources/localDb/trains.json";
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
        System.out.println("Searching trains from "+source+" to "+destination);
        return trainList.stream()
                .filter(train -> validTrain(train, source, destination))
                .collect(Collectors.toList());
    }


    private boolean validTrain(Train train, String source, String destination) {
        List<String> stations = train.getStations();

        source = source.toLowerCase();
        destination = destination.toLowerCase();

        int sourceIndex = -1;
        int destinationIndex = -1;

        for(int i = 0;i<stations.size(); ++i){
            String station  = stations.get(i).toLowerCase();
            if(station.equals(source)){
                sourceIndex = i;
            }
            if(station.equals(destination)){
                destinationIndex = i;
            }
        }
        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }

    public void printAvailableStations(){
        System.out.println("\nAvailable Stations: ");
        trainList.stream()
                .flatMap(train->train.getStations().stream())
                .distinct()
                .sorted()
                .forEach(station -> System.out.println("- "+station));
    }

    public boolean bookSeat(String trainId, int row, int col) throws IOException{

        Train trainToUpdate = trainList.stream()
                .filter(t->t.getTrainId().equals(trainId))
                .findFirst()
                .orElse(null);

        if(trainToUpdate == null){
            return false;
        }

        if(trainToUpdate.bookSeat(train.getTrainId(), row, col)){
            saveTrains();
            return true;
        }
        return false;
    }

    private void saveTrains() throws IOException{
        File trainFile = new File(TRAINS_PATH);
        objectMapper.writeValue(trainFile, trainList);
    }
}












