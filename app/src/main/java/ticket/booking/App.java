
package ticket.booking;

import ticket.booking.entities.User;
import ticket.booking.entities.Train;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.sql.SQLOutput;
import java.util.*;

public class App {

    public static void main(String[] args) {
        System.out.println("Running Train Booking Service");
        Scanner sc = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        try{
            userBookingService = new UserBookingService();
        }
        catch(IOException e){
            System.out.println("Something went wrong");
            return;
        }

        while(option!=7){
            System.out.println("Choose option:");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Bookings");
            System.out.println("7. Exit");
            option = sc.nextInt();
            switch (option){
                case 1:
                    System.out.println("Enter username to signup");
                    String nameToSignUp = sc.next();
                    System.out.println("Enter password to signup");
                    String passwordToSignUp = sc.next();
                    User userToSignup = new User(nameToSignUp, passwordToSignUp,
                            UserServiceUtil.hashPassword(passwordToSignUp),
                            new ArrayList<>(), UUID.randomUUID().toString()
                            );
                    userBookingService.signUp(userToSignup);
                    break;
                case 2:
                    System.out.println("Enter username to Login");
                    String nameToLogin = sc.next();
                    System.out.println("Enter the password to Signup");
                    String passwordToLogin = sc.next();
                    User userToLogin = new User(nameToLogin, passwordToLogin,
                                    UserServiceUtil.hashPassword(passwordToLogin),
                                    new ArrayList<>(), UUID.randomUUID().toString()
                            );
                    try{
                        userBookingService = new UserBookingService(userToLogin);
                    }catch(IOException e){
                        return;
                    }
                    break;
                case 3:
                    System.out.println("Fetching your bookings");
                    userBookingService.fetchBooking();
                    break;
                case 4:
                    System.out.println("Type your source station");
                    String source = sc.next();
                    System.out.println("Type your destination station");
                    String dest = sc.next();
                    List<Train> trains = userBookingService.getTrains(source, dest);
                    int index = 1;
                    for(Train t: trains){
                        System.out.println(index+"Train id : "+t.getTrainId());
                        for(Map.Entry<String, String> entry: t.getStationTime().entrySet()){
                            System.out.println("station"+entry.getKey()+"time: "+entry.getValue());
                        }
                    }
                    System.out.println("Select a train by typing 1,2,3...");
            }
        }
    }
}
