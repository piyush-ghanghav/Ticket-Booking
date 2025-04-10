package ticket.booking.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

public class UserBookingService {

    private User user;
    private List<User> userList;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper
    private static final String USERS_PATH = "./app/src/main/resources/localDb/users.json";

    public UserBookingService(User user1) throws IOException {
        this.user = user1;
        this.userList = loadUsers();
    }

    public UserBookingService() throws IOException {
        this.userList = loadUsers();
    }

    private List<User> loadUsers() throws IOException {
        File users = new File(USERS_PATH);
        if (!users.exists()) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public Boolean loginUser(String username, String password) {
        Optional<User> foundUser = userList.stream()
                .filter(existingUser -> existingUser.getName().equalsIgnoreCase(username))
                .findFirst();
        if(foundUser.isPresent()){
            User existingUser = foundUser.get();
            if(UserServiceUtil.checkPassword(password,existingUser.getHashedPassword())){
                this.user = existingUser;
                return true;
            }
        }
        return false;
    }
    public User getCurrentUser() {
        return this.user;
    }
    public Boolean signUp(User user1) {
        try {
            boolean userExists = userList.stream().anyMatch(existingUser -> existingUser.getName().equalsIgnoreCase(user1.getName()));
            if (userExists) {
                System.out.println("Username already exists");
                return Boolean.FALSE;
            }
            user1.setHashedPassword(UserServiceUtil.hashPassword(user1.getPassword()));
            user1.setUserId(UUID.randomUUID().toString());
            user1.setTicketsBooked(new ArrayList<>());
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException e) {
            System.out.println("Error while signing up"+e.getMessage());
            return Boolean.FALSE;
        }
    }
    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        File parentDir = usersFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        objectMapper.writeValue(usersFile, userList);
    }


    public void fetchBooking() {
        if(user == null){
            System.out.println("Please login first!");
            return;
        }

        List<Ticket> tickets = user.getTicketsBooked();
        if (tickets == null || tickets.isEmpty()) {
            System.out.println("No bookings found!");
            return;
        }

        System.out.println("\n=== Your Bookings ===");
        for(Ticket ticket : tickets){
            System.out.println("\nTicket ID: "+ticket.getTicketId());
            System.out.println("From: "+ ticket.getSource());
            System.out.println("To: "+ticket.getDestination());
            System.out.println("Train Number: "+ticket.getTrain().getTrainNo());
            System.out.println("Travel Date: "+ticket.getDateOfTravel());
//            System.out.println("Seat: Row "+ticket.getSeatRow()+", Col "+ ticket.getSeatCol());
            System.out.println("------------------------------------");
        }
    }

    public Boolean cancelBooking(String ticketId) {
        // CancelBooking 
        
        return Boolean.FALSE;
    }

    public boolean bookSeat(Train train, String source, String destination, String date, int row, int col) throws ParseException, IOException {

        if(!train.isSeatAvailable(row, col)){
            System.out.println("Seat is booked!");
            return false;
        }

        try{
            TrainService trainService = new TrainService();
            if (!train.bookSeat(train.getTrainId(), row, col)) {
                return false;
            }
            Ticket ticket = new Ticket();

            ticket.setTicketId(UUID.randomUUID().toString());
            ticket.setUserId(user.getUserId());
            ticket.setSource(source);
            ticket.setDestination(destination);
            ticket.setDateOfTravel(new SimpleDateFormat("dd-MM-yyyy").parse(date));
            ticket.setTrain(train);

            if(user.getTicketsBooked() == null){
                user.setTicketsBooked(new ArrayList<>());
            }
            user.getTicketsBooked().add(ticket);
            saveUserListToFile();
            return true;
        }catch(Exception e){
            System.out.println("Error booking seat: "+e.getMessage());
            return false;
        }
    }

    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
