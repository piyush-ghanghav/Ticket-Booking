package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Train {

    private String trainId;
    private String trainNo;
    private List<List<Integer>> seats;
    private Map<String, String> stationTime;
    private List<String> stations;


    Train(){}
    public Train(String trainId, String trainNo, List<List<Integer>> seats, Map<String, String> stationTime, List<String> stations) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTime = stationTime;
        this.stations = stations;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public Map<String, String> getStationTime() {
        return stationTime;
    }

    public void setStationTime(Map<String, String> stationTime) {
        this.stationTime = stationTime;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    @JsonIgnore
    public String trainInfo(){
        return String.format("Train ID: %s Train No: %s", trainId, trainNo);
    }

    public  void displaySeatLayout(){
        System.out.println("Seat Layout: (0: Available, 1: Booked)");
        System.out.println("  "+ IntStream.range(0, seats.get(0).size())
                .mapToObj(i -> " "+" ")
                .collect(Collectors.joining()));
        for(int i =0; i<seats.size(); i++){
            System.out.print(i+": ");{
                for(Integer seat: seats.get(i)){
                    System.out.print("["+seat+"]");
                }
                System.out.println();
            }
        }
    }
    public boolean isSeatAvailable(int row, int col){
        if(row >= 0 && row < seats.size() &&
           col>= 0 && col < seats.get(row).size()){
            return seats.get(row).get(col) == 0;
        }
        return false;
    }

    public boolean bookSeat(String trainId, int row, int col){
        if(isSeatAvailable(row,col)){
            seats.get(row).set(col,1);
            return true;
        }
        return false;
    }
}
