package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;

    public UserBookingService(User user1)throws IOException{
        this.user = user1;
        loadUsers();
    }

    private List<User> userList;

    private ObjectMapper objectMapper = new ObjectMapper();     //Jackson ObjMapper
    private static final String USERS_PATH = "../localDb/users.json";       // final - unchangable


    public UserBookingService() throws IOException {       //IOException chance of getting exception near readvlaue
        loadUsers();
    }

    public List<User>loadUsers() throws IOException{
        File users = new File(USERS_PATH);
        return objectMapper.readValue(users, new TypeReference<List<User>>(){});
        //TypeReference resolve then Deserializing at runtime
    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream()
                .filter(user1 -> (user1.getName().equalsIgnoreCase(user.getName())) &&
                        UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword()))
                .findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;

        }catch(IOException e){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException{
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile, userList);
    }


    public void fetchBooking(){
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId){
        // CancelBooking
        return Boolean.FALSE;
    }
    public String getTrains(String source, String destination){
        return trainService.searchTrains(source, destination);
    }


}
