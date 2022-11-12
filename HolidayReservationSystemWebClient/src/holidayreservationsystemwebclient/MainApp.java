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
    private PartnerWebService partnerWebServicePort;

    Partner currentPartner;
    Customer currentCustomer;

    public MainApp() {
        this.partnerWebService_Service = new PartnerWebService_Service();
        this.partnerWebServicePort = partnerWebService_Service.getPartnerWebServicePort();
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** Welcome to the Holiday Reservation System (HRS) ***\n");
        while (true) {
            System.out.println("1: Login");
            System.out.println("2: Search Car");
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
        System.out.print("Enter pickup date in the format YYYY-MM-DD> ");
        startDate = scanner.nextLine().trim();
        System.out.print("Enter pickup time in the format HH:MM> ");
        startTime = scanner.nextLine().trim();
        pickUpDateTime = LocalDateTime.parse(startDate + "T" + startTime);
        System.out.print("Enter return date in the format YYYY-MM-DD> ");
        endDate = scanner.nextLine().trim();
        System.out.print("Enter return time in the format HH:MM> ");
        endTime = scanner.nextLine().trim();
        returnDateTime = LocalDateTime.parse(endDate + "T" + endTime);

        List<Outlet> outlets = partnerWebServicePort.();

        System.out.println("\nOutlets");
        System.out.println("-----------------------------------");
        for (Outlet o : outlets) {
            System.out.println("ID: " + o.getOutletId() + ", address: " + o.getAddress()
                    + " , opening time: " + o.getOpeningTime() + " , closing time: " + o.getClosingTime());
        }
        System.out.println("-----------------------------------\n");

        System.out.print("Enter pickup outlet ID> ");
        pickupOutletId = scanner.nextLong();
        scanner.nextLine();
        reservation.setDepartureOutlet(outletSessionBeanRemote.retrieveOutlet(pickupOutletId));

        System.out.print("Enter return outlet ID> ");
        returnOutletId = scanner.nextLong();
        scanner.nextLine();
        reservation.setDestinationOutlet(outletSessionBeanRemote.retrieveOutlet(returnOutletId));

        System.out.print("Do you want to search by Make and Model? (Y/N)> ");
        String searchByMakeModel = scanner.nextLine().trim().toLowerCase();
        if (searchByMakeModel.equals("y")) {
            List<CarModel> carModels = carModelSessionBeanRemote.retrieveAllCarModels();

            System.out.println("-----------------------------------\n");
            for (CarModel carModel : carModels) {
                System.out.println(carModel.toString());
            }
            System.out.println("-----------------------------------\n");
            System.out.print("Enter Make and Model ID> ");
            makeModelId = scanner.nextLong();
            scanner.nextLine();
            reservation.setCarModel(carModelSessionBeanRemote.retrieveCarModel(makeModelId));

            available = carSessionBeanRemote.searchCarByMakeModel(makeModelId, pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId);

        } else {
            try {
                List<CarCategory> carCategories = carCategorySessionBeanRemote.retrieveAllCarCategories();

                System.out.println("-----------------------------------\n");
                for (CarCategory carCategory : carCategories) {
                    System.out.println(carCategory.toString());
                }
                System.out.println("-----------------------------------\n");
                System.out.print("Enter Category ID> ");
                categoryId = scanner.nextLong();
                scanner.nextLine();
                reservation.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategory(categoryId));

                available = carSessionBeanRemote.searchCarByCategory(categoryId, pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId);
            } catch (InvalidIdException ex) {
                System.out.println("\nInvalid ID entered!\n");
            }

        }

        return reservation;
        // NOT DONE
    }

    private void doCancelReservation() {

    }

}
