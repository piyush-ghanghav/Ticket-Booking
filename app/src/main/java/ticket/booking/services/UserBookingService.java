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

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream()
                .filter(user1 -> user1.getName().equalsIgnoreCase(user.getName()) &&
                        UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword()))
                .findFirst();
        return foundUser.isPresent();
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

        // Ensure parent directories exist
        File parentDir = usersFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        objectMapper.writeValue(usersFile, userList);
    }


    public void fetchBooking() {
        if (user != null) {
            user.printTickets();
        }
    }

    public Boolean cancelBooking(String ticketId) {
        // CancelBooking 
        
        return Boolean.FALSE;
    }

    public boolean bookSeat(Train train, String source, String destination, String date, int row, int col) throws ParseException, IOException {
        try{
            Ticket ticket = new Ticket();
            ticket.setTicketId(UUID.randomUUID().toString());
            ticket.setUserId(user.getUserId());
            ticket.setSource(source);
            ticket.setDestination(destination);
            ticket.setDateOfTravel(new SimpleDateFormat("dd-mm-yyyy").parse(date));
            ticket.setTrain(train);
            user.getTicketsBooked().add(ticket);
            saveUserListToFile();
            return true;
        }catch(Exception e){

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
