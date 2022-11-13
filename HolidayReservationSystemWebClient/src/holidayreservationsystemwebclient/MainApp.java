/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemwebclient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.partner.CarCategory;
import ws.client.partner.CarModel;
import ws.client.partner.CreditCard;
import ws.client.partner.Customer;
import ws.client.partner.CustomerExistException_Exception;
import ws.client.partner.InputDataValidationException_Exception;
import ws.client.partner.InvalidLoginCredentialException_Exception;
import ws.client.partner.NoRentalRateAvailableException_Exception;
import ws.client.partner.Outlet;
import ws.client.partner.OutletIsClosedException_Exception;
import ws.client.partner.Partner;
import ws.client.partner.PartnerWebService;
import ws.client.partner.PartnerWebService_Service;
import ws.client.partner.Reservation;
import ws.client.partner.ReservationExistException_Exception;
import ws.client.partner.ReservationRecordNotFoundException_Exception;

/**
 *
 * @author Uni
 */
public class MainApp {

    private PartnerWebService_Service partnerWebService_Service;
    private PartnerWebService partnerWebServicePort;

    private Partner currentPartner;
    private Customer currentCustomer;

    private GregorianCalendar cal = new GregorianCalendar();
    private LocalDateTime globalPickup;
    private LocalDateTime globalReturn;
    private XMLGregorianCalendar globalGregPickup;
    private XMLGregorianCalendar globalGregReturn;

    public MainApp() {
        this.partnerWebService_Service = new PartnerWebService_Service();
        this.partnerWebServicePort = partnerWebService_Service.getPartnerWebServicePort();
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to the Holiday Reservation System (HRS) ***\n");
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
            System.out.println("6: Logout\n");
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
                    doViewAllMyActiveReservations();
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
                System.out.println("\nInvalid login credential!");
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
        String pickUpDateTime;
        String returnDateTime;
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
        pickUpDateTime = startDate + "T" + startTime;
        System.out.print("Enter return date in the format YYYY-MM-DD> ");
        endDate = scanner.nextLine().trim();
        System.out.print("Enter return time in the format HH:MM> ");
        endTime = scanner.nextLine().trim();
        returnDateTime = endDate + "T" + endTime;
        reservation.setPickupString(pickUpDateTime);
        reservation.setReturnString(returnDateTime);

        List<Outlet> outlets = partnerWebServicePort.retrieveAllOutlets();

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

        System.out.print("Enter return outlet ID> ");
        returnOutletId = scanner.nextLong();
        scanner.nextLine();

        try {
            available = partnerWebServicePort.searchCar(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId);
        } catch (OutletIsClosedException_Exception ex) {
            System.out.println("Outlet is closed during your pickup or return times!");
        }

        if (available) {
            System.out.println("A car is available for reservation\n");
        } else {
            System.out.println("No cars are available for the specified times and outlets\n");
            reservation = null;
        }

        return reservation;
    }

    private Pair<Reservation, Long> doSearchCarForReservation() {
        Reservation reservation = new Reservation();
        long makeModelId = -1;
        long categoryId = -1;
        reservation.setPartner(currentPartner);

        try {
            //System.out.println("*** CaRMSRC System :: Customer :: Search Car ***\n");
            Scanner scanner = new Scanner(System.in);
            String startDate = "";
            String startTime = "";
            String endDate = "";
            String endTime = "";
            String pickUpDateTime;
            String returnDateTime;
            long pickupOutletId;
            long returnOutletId;
            boolean available = false;

            // cannot copy paste, need manual coding
            System.out.print("Enter pickup date in the format YYYY-MM-DD> ");
            startDate = scanner.nextLine().trim();
            System.out.print("Enter pickup time in the format HH:MM> ");
            startTime = scanner.nextLine().trim();
            pickUpDateTime = startDate + "T" + startTime;
            System.out.print("Enter return date in the format YYYY-MM-DD> ");
            endDate = scanner.nextLine().trim();
            System.out.print("Enter return time in the format HH:MM> ");
            endTime = scanner.nextLine().trim();
            returnDateTime = endDate + "T" + endTime;
            reservation.setPickupString(pickUpDateTime);
            reservation.setReturnString(returnDateTime);

            Date pickupDate = Date.from(LocalDateTime.parse(pickUpDateTime).atZone(ZoneId.systemDefault()).toInstant());
            Date returnDate = Date.from(LocalDateTime.parse(returnDateTime).atZone(ZoneId.systemDefault()).toInstant());

            cal.setTime(pickupDate);
            XMLGregorianCalendar pickupGreg = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            globalGregPickup = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            cal.setTime(returnDate);
            XMLGregorianCalendar returnGreg = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            globalGregReturn = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

            List<Outlet> outlets = partnerWebServicePort.retrieveAllOutlets();

            System.out.println("\nOutlets");
            System.out.println("-----------------------------------");
            for (Outlet o : outlets) {
                System.out.println("ID: " + o.getOutletId() + ", address: " + o.getAddress());
                //+ " , opening time: " + o.getOpeningTime() + " , closing time: " + o.getClosingTime());
            }
            System.out.println("-----------------------------------\n");

            System.out.print("Enter pickup outlet ID> ");
            pickupOutletId = scanner.nextLong();
            scanner.nextLine();
            Outlet departureOutlet = partnerWebServicePort.retrieveOutlet(pickupOutletId);
            reservation.setDepartureOutlet(departureOutlet);

            System.out.print("Enter return outlet ID> ");
            returnOutletId = scanner.nextLong();
            scanner.nextLine();
            Outlet returnOutlet = partnerWebServicePort.retrieveOutlet(returnOutletId);
            reservation.setDestinationOutlet(returnOutlet);

            System.out.print("Do you want to search by Make and Model? (Y/N)> ");
            String searchByMakeModel = scanner.nextLine().trim().toLowerCase();
            if (searchByMakeModel.equals("y")) {
                try {
                    List<CarModel> carModels = partnerWebServicePort.retrieveAllCarModels();

                    System.out.println("-----------------------------------\n");
                    for (CarModel carModel : carModels) {
                        System.out.println("CarModel ID: " + carModel.getCarModelId()
                                + ", Make/Model: " + carModel.getMake() + ", " + carModel.getModel()
                                + ", Category: " + carModel.getCarCategory().getCarCategoryName());
                    }
                    System.out.println("-----------------------------------\n");
                    System.out.print("Enter Make and Model ID> ");
                    makeModelId = scanner.nextLong();
                    scanner.nextLine();

                    available = partnerWebServicePort.searchCarByMakeModel(makeModelId, pickupGreg, returnGreg, pickupOutletId, returnOutletId);
                } catch (OutletIsClosedException_Exception ex) {
                    System.out.println("Outlet is closed during your pickup or return times!");
                }

            } else {
                try {
                    List<CarCategory> carCategories = partnerWebServicePort.retrieveAllCarCategories();

                    System.out.println("-----------------------------------\n");
                    for (CarCategory carCategory : carCategories) {
                        System.out.println("CarCategory ID: " + carCategory.getCarCategoryId()
                                + ", Name: " + carCategory.getCarCategoryName());
                    }
                    System.out.println("-----------------------------------\n");
                    System.out.print("Enter Category ID> ");
                    categoryId = scanner.nextLong();
                    scanner.nextLine();

                    available = partnerWebServicePort.searchCarByCategory(categoryId, pickupGreg, returnGreg, pickupOutletId, returnOutletId);
                } catch (OutletIsClosedException_Exception ex) {
                    System.out.println("Outlet is closed during your pickup or return times!");
                }
            }

            if (available) {
                System.out.println("A car is available for reservation\n");
            } else {
                System.out.println("No cars are available for the specified times and outlets\n");
                reservation = null;
            }

        } catch (DatatypeConfigurationException ex) {
            System.out.println("You have input invalid dates or times!");
        } catch (ReservationRecordNotFoundException_Exception ex) {
            System.out.println("No reservation found!");
        }
        if (makeModelId != -1 && categoryId == -1) {
            categoryId = partnerWebServicePort.retrieveCategoryIdOfModel(makeModelId);
        }

        CarCategory carCategory = partnerWebServicePort.retrieveCategory(categoryId);
        reservation.setCarCategory(carCategory);
        return new Pair<Reservation, Long>(reservation, categoryId);
    }

    private void doReserveCar() {
        System.out.println("*** HRS :: Partner :: Reserve Car ***\n");
        Scanner scanner = new Scanner(System.in);

        try {
            Pair<Reservation, Long> pair = doSearchCarForReservation();
            Reservation reservation = pair.first();
            long carCategoryId = pair.second();

            if (reservation != null) {
                doRegisterCustomer();
                if (currentCustomer != null) {
                    reservation.setCustomer(currentCustomer);

                    CreditCard creditCard = doReadCreditCard();
                    currentCustomer.setCreditCard(creditCard);
                    BigDecimal paymentAmount = new BigDecimal("0.00");

                    paymentAmount = partnerWebServicePort.calculateTotalCost(globalGregPickup, globalGregReturn, carCategoryId);
                    reservation.setPrice(paymentAmount);

                    System.out.print("\nDo you want to pay $" + paymentAmount + " for the reservation now? (Y/N)> ");
                    String confirmation = scanner.nextLine().trim().toLowerCase();

                    if (confirmation.equals("y")) {
                        // calculate rentalrate costs and request payment then set reservation and create
                        System.out.println("\nReservation paid using " + creditCard.getCcNumber() + "\n");
                        reservation.setPaid(true);

                        try {
                            reservation = partnerWebServicePort.createReservation(reservation, globalGregPickup, globalGregReturn);
                        } catch (ReservationExistException_Exception ex) {
                            System.out.println("Reservation already exists!\n");
                        } catch (InputDataValidationException_Exception ex) {
                            System.out.println(ex.getMessage() + "\n");
                        }
                    }
                } else {
                    System.out.println("\nPayment will be required upon pickup\n");

                    try {
                        reservation = partnerWebServicePort.createReservation(reservation, globalGregPickup, globalGregReturn);
                    } catch (ReservationExistException_Exception ex) {
                        System.out.println("Reservation already exists!\n");
                    } catch (InputDataValidationException_Exception ex) {
                        System.out.println(ex.getMessage() + "\n");
                    }
                    System.out.println("\nNew reservation created with ID: " + reservation.getReservationId() + "\n");
                }
            }
        } catch (NoRentalRateAvailableException_Exception ex) {
            System.out.println("\nNo rental rates are available for your reservation, please try again with a different reservation\n");
        }
    }

    private Customer doRegisterCustomer() {
        Scanner scanner = new Scanner(System.in);
        Customer customer = new Customer();

        System.out.println("*** HRS :: Register Customer ***\n");
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
        customer.setPartner(currentPartner);

        try {
            currentCustomer = partnerWebServicePort.createCustomer(customer);
        } catch (CustomerExistException_Exception ex) {
            System.out.println("Customer already exists!\n");
        } catch (InputDataValidationException_Exception ex) {
            System.out.println(ex.getMessage() + "\n");
        }

        System.out.println("\nNew customer created with ID: " + currentCustomer.getCustomerId() + "\n");
        return currentCustomer;
    }

    private CreditCard doReadCreditCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease enter credit card details");
        CreditCard creditCard = new CreditCard();
        String nameOnCC = "";
        String ccNumber = "";
        String cvv = "";

        while (true) {
            System.out.print("Enter name on credit card> ");
            nameOnCC = scanner.nextLine().trim();
            creditCard.setNameonCC(nameOnCC);
            System.out.print("Enter credit card number> ");
            ccNumber = scanner.nextLine().trim();
            creditCard.setCcNumber(ccNumber);
            System.out.print("Enter credit card cvv> ");
            cvv = scanner.nextLine().trim();
            creditCard.setCvv(cvv);

            System.out.print("Enter expiry date in the format YYYY-MM> ");
            String expiry = scanner.nextLine().trim() + "-01";
            /*
            try {
                return partnerWebServicePort.createCreditCard(creditCard, currentCustomer.getCustomerId(), expiry);

            } catch (DateTimeParseException ex) {
                System.out.println("Invalid date or time entered, please try again");
            } catch (CreditCardExistException_Exception ex) {
                System.out.println("Credit card already exists!");
            } catch (InputDataValidationException_Exception ex) {
                System.out.println(ex);*/

            return creditCard;
        }
    }

    private void doCancelReservation() {
        try {
            System.out.println("*** HRS :: Partner :: Cancel Reservation ***\n");
            Scanner scanner = new Scanner(System.in);

            boolean empty = doViewAllMyActiveReservations();
            if (empty) {
                return;
            }

            System.out.print("Enter ID of reservation you want to delete> ");
            long reservationId = scanner.nextLong();
            Reservation reservation = partnerWebServicePort.retrieveReservation(reservationId);
            scanner.nextLine();

            BigDecimal totalCost = reservation.getPrice();

            Date pickupDate = Date.from(LocalDateTime.parse(reservation.getPickupString()).atZone(ZoneId.systemDefault()).toInstant());
            Date returnDate = Date.from(LocalDateTime.parse(reservation.getReturnString()).atZone(ZoneId.systemDefault()).toInstant());

            cal.setTime(pickupDate);
            XMLGregorianCalendar pickupGreg = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            XMLGregorianCalendar gregPickup = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

            BigDecimal penalty = partnerWebServicePort.calculatePenalty(totalCost, gregPickup);
            BigDecimal refund = totalCost.subtract(penalty);
            //System.out.println("total " + totalCost + " pickup " + pickup + " penalty " + penalty);

            if (reservation.isPaid()) {
                System.out.println("\nConfirm cancellation of reservation with ID " + reservation.getReservationId() + "?"
                        + " A penalty of $" + penalty + " will be imposed.");
                System.out.print("You will be refunded $" + refund + " (Y/N)> ");

                String confirmation = scanner.nextLine().trim().toLowerCase();
                if (confirmation.equals("y")) {
                    partnerWebServicePort.cancelReservation(reservationId, refund);
                    System.out.println("\nReservation has been cancelled\n");
                    System.out.println("Penalty has been charged to your saved credit card\n");// + reservation.getCustomer().getCreditCard().getCcNumber() + "\n");

                } else {
                    System.out.println("\nCancellation cancelled\n");
                }
            } else {
                System.out.println("\nConfirm cancellation of reservation with ID " + reservation.getReservationId() + "?"
                        + " You will be charged a penalty of $" + penalty + " (Y/N)> ");

                String confirmation = scanner.nextLine().trim().toLowerCase();
                if (confirmation.equals("y")) {
                    partnerWebServicePort.cancelReservation(reservationId, refund);
                    System.out.println("\nReservation has been cancelled\n");
                    System.out.println("Penalty has been charged to your saved credit card\n");// + reservation.getCustomer().getCreditCard().getCcNumber() + "\n");

                } else {
                    System.out.println("\nCancellation cancelled\n");
                }
            }
        } catch (ReservationRecordNotFoundException_Exception ex) {
            System.out.println("You have input an invalid reservation");
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doViewReservationDetails() {
        try {
            System.out.println("*** HRS :: Customer :: View Reservation Details ***\n");
            Scanner scanner = new Scanner(System.in);
            List<Reservation> reservations = getAllMyReservations();

            if (reservations.isEmpty()) {
                System.out.println("No reservations have been made!");
                return;
            }
            System.out.println("\n-----------------------------------");
            for (Reservation reservation : reservations) {
                if (!reservation.isCancelled()) {
                    String reserv = partnerWebServicePort.reservationStringMaker(reservation.getReservationId());
                    System.out.println("ID: " + reservation.getReservationId() + reserv);
                }
            }
            System.out.println("-----------------------------------\n");
            System.out.print("Enter ID of reservation you want to view> ");
            long reservationId = scanner.nextLong();
            scanner.nextLine();
            Reservation reservation = partnerWebServicePort.retrieveReservation(reservationId);
            String specific = partnerWebServicePort.reservationStringMaker(reservation.getReservationId());
            System.out.println("ID: " + reservation.getReservationId() + specific);
            System.out.print("Press enter to continue>");
            scanner.nextLine();
            System.out.println();
        } catch (ReservationRecordNotFoundException_Exception ex) {
            System.out.println("You have input an invalid reservation");
        }
    }

    private List<Reservation> getAllMyReservations() {
        return partnerWebServicePort.retrieveAllPartnerReservations(currentPartner.getPartnerId());
    }

    private boolean doViewAllMyActiveReservations() {
        try {
            System.out.println("***HRS :: Partner :: View All Active Partner Reservations ***\n");
            Scanner scanner = new Scanner(System.in);
            List<Reservation> reservations = getAllMyReservations();

            if (reservations.isEmpty()) {
                System.out.println("No reservations have been made!");
                return true;
            }
            System.out.println("\n-----------------------------------");
            for (Reservation reservation : reservations) {
                if (!reservation.isCancelled()) {
                    String reserv = partnerWebServicePort.reservationStringMaker(reservation.getReservationId());
                    System.out.println("ID: " + reservation.getReservationId() + reserv);
                }
            }
            System.out.println("-----------------------------------\n");
            System.out.print("Press enter to continue>");
            scanner.nextLine();
        } catch (InputMismatchException ex) {
            System.out.println("Invalid input!");
        }
        return false;
    }

    class Pair<T, U> {

        private final T t;
        private final U u;

        public Pair(T t, U u) {
            this.t = t;
            this.u = u;
        }

        T first() {
            return this.t;
        }

        U second() {
            return this.u;
        }

        @Override
        public String toString() {
            return "(" + this.t + ", " + this.u + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj instanceof Pair) {
                Pair<?, ?> other = (Pair<?, ?>) obj;
                return this.first().equals(other.first())
                        && this.second().equals(other.second());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(t, u);
        }
    }
}
