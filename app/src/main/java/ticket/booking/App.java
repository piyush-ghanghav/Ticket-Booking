
package ticket.booking;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

public class App {

    public static void main(String[] args) throws ParseException, IOException {
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
            System.out.println("Menu");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Bookings");
            System.out.println("7. Exit");
            System.out.println("Choose a option: ");
            option = sc.nextInt();
            switch (option){
                case 1:
                    System.out.println("Enter username to signup");
                    String nameToSignUp = sc.next();
                    System.out.println("Enter password to signup");
                    String passwordToSignUp = sc.next();

                    User userToSignup = new User();
                    userToSignup.setName(nameToSignUp);
                    userToSignup.setPassword(passwordToSignUp);
                    userToSignup.setTicketsBooked(new ArrayList<>());
                    Boolean hasSignUp = userBookingService.signUp(userToSignup);
                    if(hasSignUp){
                        System.out.println("Sign Up Successful");
                    }else{
                        System.out.println("Sign Up Failed");
                }
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
                    String destination = sc.next();
                    List<Train> trains = userBookingService.getTrains(source, destination);
                    if(trains.isEmpty()){
                        System.out.println("No trains for this route");
                        break;
                    }

                    int index = 1;
                    for(Train t: trains){
                        System.out.println(index+"Train id : "+t.getTrainId());
                        for(Map.Entry<String, String> entry: t.getStationTime().entrySet()){
                            System.out.println("station"+entry.getKey()+"| Time: "+entry.getValue());
                        }
                        index++;
                    }
                    break;
                case 5:
                    System.out.println("Enter source station:");
                    String src = sc.next();
                    System.out.println("Enter destination station:");
                    String dst = sc.next();

                    List<Train> foundTrains = userBookingService.getTrains(src,dst);
                    if(foundTrains.isEmpty()){
                        System.out.println("No trains are available for this route");
                        break;
                    }

                    int trainIndex = 1;
                    for(Train t: foundTrains){
                        System.out.println(trainIndex+". Train ID: "+ t.getTrainId());
                        trainIndex++;
                    }

                    System.out.println("Select train number (1 - "+foundTrains.size()+"):");
                    int selected = sc.nextInt();

                    if(selected < 1 || selected > foundTrains.size()){
                        System.out.println("Invalid Train selected");
                        break;
                    }

                    Train selectedTrain  = foundTrains.get(selected-1);

                    System.out.println("Enter Date of travel (dd-mm-yyyy):");
                    String travelDate = sc.next();

                    System.out.println("Enter seat row:");
                    int row = sc.nextInt();

                    System.out.println("Enter seat column:");
                    int col = sc.nextInt();

                    boolean booked = userBookingService.bookSeat(selectedTrain, src, dst, travelDate, row, col);
                    if(booked){
                        System.out.println("Seat Booked Successfully");
                    }else{
                        System.out.println("Booking Failed");
                    }
                    break;
            }
        }
    }
}
