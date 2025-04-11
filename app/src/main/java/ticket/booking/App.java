
package ticket.booking;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.TrainService;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

public class App {

    private static boolean checkLoggedIn(UserBookingService userBookingService){
        User currentUser = userBookingService.getCurrentUser();
        if((currentUser == null)){
            System.out.println("\n Please login first!");
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws ParseException, IOException {
        System.out.println("Running Train Booking Service");
        Scanner sc = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        try{
            userBookingService = new UserBookingService();
        }
        catch(IOException e){
            System.out.println("Something went wrong"+e.getMessage());
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
            System.out.println("7. Logout");
            System.out.println("8. Exit");
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
                    System.out.println("Enter the password to Login");
                    String passwordToLogin = sc.next();

                    if(userBookingService.loginUser(nameToLogin, passwordToLogin)){
                        System.out.println("Login successful!");
                        User loggedInUser = userBookingService.getCurrentUser();
                        System.out.println("Welcome back, "+loggedInUser.getName());
                    }else{
                        System.out.println("Login failed! Invalid Credentials");
                    }
                    break;
                case 3:
                    if(!checkLoggedIn(userBookingService)){
                        break;
                    }
                    System.out.println("Fetching your bookings");
                    userBookingService.fetchBooking();
                    break;
                case 4:
                    System.out.println("\n=== Search Trains ===");
                    System.out.println("Type your source station");
                    String source = sc.next().trim();
                    System.out.println("Type your destination station");
                    String destination = sc.next().trim();

                    List<Train> trains = userBookingService.getTrains(source, destination);
                    if(trains.isEmpty()){
                        System.out.println("No trains for route: "+source+" -> "+destination);
                        TrainService trainService = new TrainService();
                        trainService.printAvailableStations();
                        break;
                    }

                    System.out.println("\nFound "+ trains.size()+" trains:");
                    System.out.println("----------------------------------------");

                    int index = 1;
                    for(Train train: trains){
                        System.out.println("Train "+index+":");
                        System.out.println("Train Number: "+train.getTrainNo());
                        System.out.println("Train ID: "+train.getTrainId());
                        System.out.println("\nSchedule: ");
                        Map<String, String> schedule = train.getStationTime();

                        for(String station: train.getStations()){
                            System.out.println(station +" -> "+ schedule.get(station.toLowerCase()));

                        }
                        System.out.println("----------------------------------------");
                        index++;
                    }

                    break;
                case 5:
                    if(!checkLoggedIn(userBookingService)){
                        break;
                    }
                    System.out.println("=== Book a Ticket ===");
                    System.out.println("Enter source station:");
                    String src = sc.next().trim().toLowerCase();
                    System.out.println("Enter destination station:");
                    String dst = sc.next().trim().toLowerCase();

                    List<Train> foundTrains = userBookingService.getTrains(src,dst);
                    if(foundTrains.isEmpty()){
                        System.out.println("No trains are available for this route!");
                        TrainService trainService = new TrainService();
                        trainService.printAvailableStations();
                        break;
                    }

                    System.out.println("\nAvailable Trains: ");
                    for(int i = 0; i<foundTrains.size(); ++i){
                        Train t = foundTrains.get(i);
                        System.out.println("\n"+(i+1)+". "+t.getTrainNo());
                        System.out.println("From: "+src+" at "+t.getStationTime().get(src));
                        System.out.println("To: "+dst+" at "+t.getStationTime().get(dst));
                    }

                    System.out.println("Select train number (1-"+foundTrains.size()+"): ");
                    int selected = sc.nextInt();

                    if(selected<1 || selected>foundTrains.size()){
                        System.out.println("Invalid train selection!");
                        break;
                    }
                    Train selectedTrain = foundTrains.get(selected-1);
                    selectedTrain.displaySeatLayout();
                    int row, col;
                    do {
                        System.out.print("\nEnter row number (0-" + (selectedTrain.getSeats().size() - 1) + "): ");
                        row = sc.nextInt();
                        System.out.print("Enter column number (0-" + (selectedTrain.getSeats().get(0).size() - 1) + "): ");
                        col = sc.nextInt();

                        if (row < 0 || row >= selectedTrain.getSeats().size() ||
                                col < 0 || col >= selectedTrain.getSeats().get(0).size()) {
                            System.out.println("\n Invalid seat numbers! Please try again.");
                            continue;
                        }

                        if (!selectedTrain.isSeatAvailable(row, col)) {
                            System.out.println("\n Seat already booked! Please choose another seat.");
                            continue;
                        }
                        break;
                    } while (true);

                    if(!selectedTrain.isSeatAvailable(row, col)){
                        System.out.println("Invalid seat or seat already booked!");
                        break;
                    }

                    System.out.println("Enter travel date (dd-mm-yyyy): ");
                    String travelDate = sc.next();

                    boolean booked = userBookingService.bookSeat(selectedTrain,src,dst,travelDate,row, col );

                    if(booked){
                        System.out.println("Ticket booking successful");
                    }else{
                        System.out.println("Booking failed! Try again.");
                    }
                    break;
                case 6:
                    if(!checkLoggedIn(userBookingService)){
                        break;
                    }
                    System.out.println("\n=== Cancel Booking ===");

                    userBookingService.fetchBooking();

                    if(userBookingService.getCurrentUser().getTicketsBooked().isEmpty()){
                        System.out.println("No tickets found!");
                        break;
                    }
                    System.out.println("Enter ticket ID to cancel: ");
                    String ticketId = sc.next();

                    try{
                        if(userBookingService.cancelBooking(ticketId)){
                            System.out.println("\nBooking cancelled successfully!");
                            System.out.println("Updated bookings: ");
                            userBookingService.fetchBooking();
                        }else{
                            System.out.println("\n Cancellation failed! Please check Ticket ID.");
                        }
                    }catch(IOException e){
                        System.out.println("Error During cancellation: "+e.getMessage());
                    }
                    break;
                case 7:
                    userBookingService.logout();
                    break;
                case 8:
                    System.out.println("\nThank you for using IRCTC Booking System!");
                    System.out.println("Goodbye!");
                    sc.close();
                    return;

            }
        }
    }
}
