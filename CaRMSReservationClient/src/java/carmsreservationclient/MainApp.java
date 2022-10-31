/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.CarCategory;
import entity.CarModel;
import entity.Customer;
import entity.Outlet;
import entity.Reservation;
import exception.CustomerNotFoundException;
import exception.InvalidIdException;
import exception.InvalidLoginCredentialException;
import java.time.LocalDateTime;
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
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private Customer currentCustomer;
    private CarSessionBeanRemote carSessionBeanRemote;

    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote,
            CarCategorySessionBeanRemote carCategorySessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote,
            OutletSessionBeanRemote outletSessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote,
            CarSessionBeanRemote carSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
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
                System.out.println("4: Exit\n");
                response = 0;

                while (response < 1 || response > 4) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        doRegisterCustomer();
                    } else if (response == 2) {
                        doLogin();
                    } else if (response == 3) {
                        doSearchCar();
                    } else if (response == 4) {
                        break;
                    } else {
                        System.out.print("Invalid option, please try again!\n");
                    }
                }

                if (response == 4) {
                    break;
                }
            }
        } catch (InputMismatchException ex) {
            System.out.print("Invalid option, please try again!\n");
        }
    }

    private void doLogin() {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** CaRMSRC System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            try {
                currentCustomer = customerSessionBeanRemote.customerLogin(username, password);
                System.out.println("Login successful!\n");
                customerMenu();
            } catch (InvalidLoginCredentialException ex) {
                System.out.println("\nInvalid login credential: " + ex.getMessage() + "\n");
            } catch (CustomerNotFoundException ex) {
                System.out.println("\nUser does not exist!\n");
            }
        } else {
            System.out.println("Invalid input, please try again!\n");
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
                    doSearchCar();
                } else if (response == 2) {
                    doReserveCar();
                } else if (response == 3) {
                    doCancelReservation();
                } else if (response == 4) {
                    doViewReservationDetails();
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
        reservation.setCustomer(currentCustomer);

        System.out.print("Enter pickup date in the format YYYY-MM-DD> ");
        startDate = scanner.nextLine().trim();
        System.out.print("Enter pickup time in the format HH:MM> ");
        startTime = scanner.nextLine().trim();
        pickUpDateTime = LocalDateTime.parse(startDate + "T" + startTime);
        reservation.setPickupTime(pickUpDateTime);

        System.out.print("Enter return date in the format YYYY-MM-DD> ");
        endDate = scanner.nextLine().trim();
        System.out.print("Enter return time in the format HH:MM> ");
        endTime = scanner.nextLine().trim();
        returnDateTime = LocalDateTime.parse(endDate + "T" + endTime);
        reservation.setReturnTime(returnDateTime);

        List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();

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

        if (available) {
            System.out.println("A car is available for reservation\n");
        } else {
            System.out.println("No cars are available for the specified times and outlets\n");
            reservation = null;
        }
        return reservation;
        // NOT DONE
    }

    private void doReserveCar() {
        System.out.println("*** CaRMSRC System :: Customer :: Reserve Car ***\n");
        Scanner scanner = new Scanner(System.in);

        Reservation reservation = doSearchCar();

        if (reservation != null) {
            reservation = reservationSessionBeanRemote.createReservation(reservation);

            System.out.println("\nNew " + reservation.toString() + " created\n");
        }
        // NOT DONE
    }

    private void doCancelReservation() {
        System.out.println("*** CaRMSRC System :: Customer :: Cancel Reservation ***\n");
        Scanner scanner = new Scanner(System.in);

        doViewAllMyReservations();
        System.out.print("Enter ID of reservation you want to delete> ");
        long reservationId = scanner.nextLong();
        Reservation reservation = reservationSessionBeanRemote.retrieveReservation(reservationId);
        scanner.nextLine();
        System.out.print("\nConfirm cancellation of " + reservation.toString() + "? (Y/N)> ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (confirmation.equals("y")) {
            reservationSessionBeanRemote.cancelReservation(reservationId);
            System.out.println("\n" + reservation.toString() + " cancelled\n");
        } else {
            System.out.println("\nCancellation cancelled\n");
        }
    }

    private List<Reservation> getAllMyReservations() {
        return reservationSessionBeanRemote.retrieveAllMyReservations(currentCustomer.getId());
    }

    private void doViewReservationDetails() {
        System.out.println("*** CaRMSRC System :: Customer :: View Reservation Details ***\n");
        Scanner scanner = new Scanner(System.in);
        List<Reservation> reservations = getAllMyReservations();
        System.out.println("\n-----------------------------------");
        for (Reservation r : reservations) {
            System.out.println("ID: " + r.getReservationId() + ", Pickup at: " + r.getPickupTime() + " from " + r.getDepartureOutlet());
        }
        System.out.println("-----------------------------------\n");
        System.out.print("Enter ID of reservation you want to view> ");
        long reservationId = scanner.nextLong();
        scanner.nextLine();
        Reservation reservation = reservationSessionBeanRemote.retrieveReservation(reservationId);
        System.out.println("\n" + reservation.toString() + "\n");
        System.out.print("Press enter to continue>");
        scanner.nextLine();
        System.out.println();
    }

    private void doViewAllMyReservations() {
        System.out.println("*** CaRMSRC System :: Customer :: View All My Reservations ***\n");
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
