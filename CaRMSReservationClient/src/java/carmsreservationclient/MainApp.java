/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Customer;
import entity.Reservation;
import exception.InvalidLoginCredentialException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Uni
 */
public class MainApp {

    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;

    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote,
            CarCategorySessionBeanRemote carCategorySessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response;

        try {
            while (true) {
                System.out.println("*** Welcome to CaRMS Reservation Client (CaRMSRC) ***\n");
                System.out.println("1: Register As Customer");
                System.out.println("2: Login");
                System.out.println("3: Search Car");
                System.out.println("3: Exit\n");
                response = 0;

                while (response < 1 || response > 4) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        doRegisterCustomer();
                    } else if (response == 2) {
                        try {
                            doLogin();
                            System.out.println("Login successful!\n");
                            customerMenu();

                        } catch (InvalidLoginCredentialException ex) {
                            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                        }
                    } else if (response == 3) {
                        //doSearchCar();
                    } else if (response == 4) {
                        break;
                    } else {
                        System.out.print("Invalid option, please try again!\n");
                    }
                }

                if (response == 3) {
                    break;
                }
            }
        } catch (InputMismatchException ex) {
            System.out.print("Invalid option, please try again!\n");
        }
    }

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** CaRMSRC System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentCustomer = customerSessionBeanRemote.customerLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void doRegisterCustomer() {
        Scanner scanner = new Scanner(System.in);
        Customer customer = new Customer();

        System.out.println("*** CaRMSRC System :: Register As Customer ***\n");
        System.out.print("Enter username> ");
        customer.setUsername(scanner.nextLine().trim());
        System.out.print("Enter password> ");
        customer.setPassword(scanner.nextLine().trim());
        System.out.print("Enter first name> ");
        customer.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter last name> ");
        customer.setLastName(scanner.nextLine().trim());
        System.out.print("Enter email> ");
        customer.setEmail(scanner.nextLine().trim());
        System.out.print("Enter contact number> ");
        customer.setContactNumber(scanner.nextLine().trim());
        System.out.print("Enter address line 1> ");
        customer.setAddressLine1(scanner.nextLine().trim());
        System.out.print("Enter address line 2> ");
        customer.setAddressLine2(scanner.nextLine().trim());
        System.out.print("Enter postal code> ");
        customer.setPostalCode(scanner.nextLine().trim());

        customer = customerSessionBeanRemote.createCustomer(customer);

        System.out.println("\nNew " + customer.toString() + " created\n");

    }

    private void customerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response;

        while (true) {
            System.out.println("*** CaRMSRC System :: Customer ***\n");
            System.out.println("1: Search Car");
            System.out.println("2: Reserve Car");
            System.out.println("3: Cancel Reservation");
            System.out.println("4: View Reservation Details");
            System.out.println("5: View All My Reservations");
            System.out.println("6: Logout");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    //doSearchCar();
                } else if (response == 2) {
                    //doReserveCar();
                } else if (response == 3) {
                    //doCancelReservation();
                } else if (response == 4) {
                    //doViewReservationDetails();
                } else if (response == 5) {
                    doViewAllMyReservations();
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
    
    private List<Reservation> getAllMyReservations() {
        return reservationSessionBeanRemote.retrieveAllMyReservations(currentCustomer.getId());
        
    }
    
    private void doViewAllMyReservations() {
        Scanner scanner = new Scanner(System.in);
        List<Reservation> reservations = getAllMyReservations();
        System.out.println("\n-----------------------------------");
        for (Reservation r : reservations) {
            System.out.println(r.toString());//"ID: " + r.getReservationId()+ ", Rate Name: " + r.get());
        }
        System.out.println("-----------------------------------\n");
        System.out.print("Press enter to continue>");
        scanner.nextLine();        
    }
}
