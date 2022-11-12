/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CreditCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.CarCategory;
import entity.CarModel;
import entity.CreditCard;
import entity.Customer;
import entity.Outlet;
import entity.Reservation;
import exception.CreditCardExistException;
import exception.CustomerExistException;
import exception.CustomerNotFoundException;
import exception.InputDataValidationException;
import exception.InvalidIdException;
import exception.InvalidLoginCredentialException;
import exception.NoRentalRateAvailableException;
import exception.OutletIsClosedException;
import exception.ReservationExistException;
import exception.ReservationRecordNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Uni
 */
public class MainApp {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private Customer currentCustomer;
    private CarSessionBeanRemote carSessionBeanRemote;
    private CreditCardSessionBeanRemote creditCardSessionBeanRemote;

    public MainApp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote,
            CarCategorySessionBeanRemote carCategorySessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote,
            OutletSessionBeanRemote outletSessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote,
            CarSessionBeanRemote carSessionBeanRemote, CreditCardSessionBeanRemote creditCardSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.creditCardSessionBeanRemote = creditCardSessionBeanRemote;
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
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

        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);
        if (constraintViolations.isEmpty()) {
            try {
                customer = customerSessionBeanRemote.createCustomer(customer);
            } catch (CustomerExistException ex) {
                System.out.println("Customer already exists!\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForCustomer(constraintViolations);
        }

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

        while (true) {
            try {
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
                break;
            } catch (DateTimeParseException ex) {
                System.out.println("Invalid date or time entered, please try again");
            } catch (OutletIsClosedException ex) {
                System.out.println("Outlet is closed during your pickup or return times!");
            }
        }
        return reservation;
        // NOT DONE
    }

    private void doReserveCar() {
        System.out.println("*** CaRMSRC System :: Customer :: Reserve Car ***\n");
        Scanner scanner = new Scanner(System.in);

        try {
            Reservation reservation = doSearchCar();

            if (reservation != null) {
                CreditCard creditCard = doSaveCreditCard();
                currentCustomer.setCreditCard(creditCard);
                BigDecimal paymentAmount = new BigDecimal("0.00");
                paymentAmount = rentalRateSessionBeanRemote.calculateTotalCost(reservation);
                reservation.setPaymentAmount(paymentAmount);

                System.out.print("\nDo you want to pay $" + paymentAmount + " for the reservation now? (Y/N)> ");
                String confirmation = scanner.nextLine().trim().toLowerCase();

                if (confirmation.equals("y")) {
                    // calculate rentalrate costs and request payment then set reservation and create
                    System.out.println("\nReservation of amount: $" + paymentAmount.toString() + " paid using " + creditCard.getCcNumber() + "\n");
                    reservation.setPaid(true);
                    Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);
                    if (constraintViolations.isEmpty()) {
                        try {
                            reservation = reservationSessionBeanRemote.createReservation(reservation);
                        } catch (ReservationExistException ex) {
                            System.out.println("Reservation already exists!\n");
                        } catch (InputDataValidationException ex) {
                            System.out.println(ex.getMessage() + "\n");
                        }
                    } else {
                        showInputDataValidationErrorsForReservation(constraintViolations);
                    }
                } else {
                    System.out.println("\nPayment will be required upon pickup\n");
                    Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);
                    if (constraintViolations.isEmpty()) {
                        try {
                            reservation = reservationSessionBeanRemote.createReservation(reservation);
                        } catch (ReservationExistException ex) {
                            System.out.println("Reservation already exists!\n");
                        } catch (InputDataValidationException ex) {
                            System.out.println(ex.getMessage() + "\n");
                        }
                    } else {
                        showInputDataValidationErrorsForReservation(constraintViolations);
                    }

                    System.out.println("\nNew " + reservation.toString() + " created\n");
                }
            }
        } catch (NoRentalRateAvailableException ex) {
            System.out.println("\nNo rental rates are available for your reservation, please try again with a different reservation\n");
        }
        // NOT DONE
    }

    private CreditCard doSaveCreditCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease enter credit card details");
        CreditCard creditCard = new CreditCard();
        String nameOnCC = "";
        String ccNumber = "";
        String cvv = "";
        LocalDate expiryDate;

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
            expiryDate = LocalDate.parse(expiry);
            expiryDate = YearMonth.from(expiryDate).atEndOfMonth();
            //System.out.println(expiryDate);
            creditCard.setExpiryDate(expiryDate);

            Set<ConstraintViolation<CreditCard>> constraintViolations = validator.validate(creditCard);
            if (constraintViolations.isEmpty()) {
                try {
                    return creditCardSessionBeanRemote.createCreditCard(creditCard, currentCustomer.getCustomerId());

                } catch (DateTimeParseException ex) {
                    System.out.println("Invalid date or time entered, please try again");
                } catch (CreditCardExistException ex) {
                    System.out.println("Credit card already exists!");
                } catch (InputDataValidationException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    private void doCancelReservation() {
        try {
            System.out.println("*** CaRMSRC System :: Customer :: Cancel Reservation ***\n");
            Scanner scanner = new Scanner(System.in);

            doViewAllMyActiveReservations();
            System.out.print("Enter ID of reservation you want to delete> ");
            long reservationId = scanner.nextLong();
            Reservation reservation = reservationSessionBeanRemote.retrieveReservation(reservationId);
            scanner.nextLine();

            BigDecimal totalCost = reservation.getPaymentAmount();
            LocalDateTime pickup = reservation.getPickupTime();

            BigDecimal penalty = calculatePenalty(totalCost, pickup);
            BigDecimal refund = totalCost.subtract(penalty);
            //System.out.println("total " + totalCost + " pickup " + pickup + " penalty " + penalty);

            if (reservation.isPaid()) {
                System.out.println("\nConfirm cancellation of reservation with ID " + reservation.getReservationId() + "?"
                        + " A penalty of $" + penalty + " will be imposed.");
                System.out.print("You will be refunded $" + refund + " (Y/N)> ");

                String confirmation = scanner.nextLine().trim().toLowerCase();
                if (confirmation.equals("y")) {
                    reservationSessionBeanRemote.cancelReservation(reservationId, refund);
                    System.out.println("\n" + reservation.toString() + " cancelled\n");
                    System.out.println("Penalty has been charged to your saved credit card\n");// + reservation.getCustomer().getCreditCard().getCcNumber() + "\n");

                } else {
                    System.out.println("\nCancellation cancelled\n");
                }
            } else {
                System.out.println("\nConfirm cancellation of reservation with ID " + reservation.getReservationId() + "?"
                        + " You will be charged a penalty of $" + penalty + " (Y/N)> ");

                String confirmation = scanner.nextLine().trim().toLowerCase();
                if (confirmation.equals("y")) {
                    reservationSessionBeanRemote.cancelReservation(reservationId, refund);
                    System.out.println("\n" + reservation.toString() + " cancelled\n");
                    System.out.println("Penalty has been charged to your saved credit card\n");// + reservation.getCustomer().getCreditCard().getCcNumber() + "\n");

                } else {
                    System.out.println("\nCancellation cancelled\n");
                }
            }
        } catch (ReservationRecordNotFoundException ex) {
            System.out.println("You have input an invalid reservation");
        }

    }

    private BigDecimal calculatePenalty(BigDecimal totalCost, LocalDateTime pickup) {
        BigDecimal penalty = new BigDecimal("0.00");

        if (LocalDateTime.now().isBefore(pickup.minusDays(14))) {
            //System.out.println("more than 14 before");
        } else if (LocalDateTime.now().isBefore(pickup.minusDays(7))) {
            penalty = totalCost.multiply(new BigDecimal("0.2"));
            //System.out.println("14 to 7 before");
        } else if (LocalDateTime.now().isBefore(pickup.minusDays(3))) {
            penalty = totalCost.multiply(new BigDecimal("0.5"));
            //System.out.println("7 to 3 before");
        } else if (LocalDateTime.now().isBefore(pickup)) {
            penalty = totalCost.multiply(new BigDecimal("0.7"));
            //System.out.println("3 or less before");
        }

        return penalty.setScale(2);
    }

    private List<Reservation> getAllMyReservations() {
        return reservationSessionBeanRemote.retrieveAllMyReservations(currentCustomer.getId());
    }

    private void doViewReservationDetails() {
        try {
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
        } catch (ReservationRecordNotFoundException ex) {
            System.out.println("You have input an invalid reservation");
        }
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

    private void doViewAllMyActiveReservations() {
        System.out.println("*** CaRMSRC System :: Customer :: View All My Active Reservations ***\n");
        Scanner scanner = new Scanner(System.in);
        List<Reservation> reservations = getAllMyReservations();
        System.out.println("\n-----------------------------------");
        for (Reservation r : reservations) {
            if (!r.isCancelled()) {
                System.out.println(r.toString());//"ID: " + r.getReservationId()+ ", Rate Name: " + r.get());
            }
        }
        System.out.println("-----------------------------------\n");
        System.out.print("Press enter to continue>");
        scanner.nextLine();
    }

    private void showInputDataValidationErrorsForCustomer(Set<ConstraintViolation<Customer>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void showInputDataValidationErrorsForReservation(Set<ConstraintViolation<Reservation>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void showInputDataValidationErrorsForCreditCard(Set<ConstraintViolation<CreditCard>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

}
