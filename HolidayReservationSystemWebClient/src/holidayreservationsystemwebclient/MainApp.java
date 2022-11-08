/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemwebclient;


import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import ws.client.partner.CarCategory;
import ws.client.partner.CarModel;
import ws.client.partner.Customer;
import ws.client.partner.InvalidLoginCredentialException_Exception;
import ws.client.partner.Outlet;
import ws.client.partner.Partner;
import ws.client.partner.PartnerWebService;
import ws.client.partner.PartnerWebService_Service;
import ws.client.partner.Reservation;

/**
 *
 * @author Uni
 */
public class MainApp {

    private PartnerWebService_Service partnerWebService_Service;

    Partner currentPartner;
    Customer currentCustomer;

    public MainApp(PartnerWebService_Service partnerWebService_Service) {
        this.partnerWebService_Service = partnerWebService_Service;
    }

    public void runApp() {
        PartnerWebService partnerWebServicePort = partnerWebService_Service.getPartnerWebServicePort();

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** Welcome to the Holiday Reservation System (HRS) ***\n");
        while (true) {
            System.out.println("1: Partner Login");
            System.out.println("2: Partner Search Car");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doLogin();
                } else if (response == 2) {
                    doSearchCar();
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }
    
    public void partnerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response;

        while (true) {
            System.out.println("*** HRS System :: Partner ***\n");
            System.out.println("1: Search Car");
            System.out.println("2: Reserve Car");
            System.out.println("3: Cancel Reservation");
            System.out.println("4: View Reservation Details");
            System.out.println("5: View All Partner Reservations");
            System.out.println("6: Logout");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doSearchCar();
                } else if (response == 2) {
                    //doReserveCar();
                } else if (response == 3) {
                   // doCancelReservation();
                } else if (response == 4) {
                   // doViewReservationDetails();
                } else if (response == 5) {
                   // doViewAllMyReservations();
                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 6) {
                break;
            }
        }
    }

    public void doLogin() {
        PartnerWebService partnerWebServicePort = partnerWebService_Service.getPartnerWebServicePort();

        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** HRS System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            try {
                currentPartner = partnerWebServicePort.partnerLogin(username, password);
                System.out.println("Login successful!\n");
                partnerMenu();
            } catch (InvalidLoginCredentialException_Exception ex) {
                System.out.println("\nInvalid login credential: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Invalid input, please try again!\n");
        }
    }
    
    
    private Reservation doSearchCar() {
        //System.out.println("*** CaRMSRC System :: Customer :: Search Car ***\n");
        Scanner scanner = new Scanner(System.in);
        String startDate = "";
        String startTime = "";
        String endDate = "";
        String endTime = "";
        LocalDateTime pickUpDateTime;
        LocalDateTime returnDateTime;
        long pickupOutletId;
        long returnOutletId;
        long makeModelId;
        long categoryId;
        boolean available = false;
        Reservation reservation = new Reservation();
        
        // cannot copy paste, need manual coding

        return reservation;
        // NOT DONE
    }
    
    
}
