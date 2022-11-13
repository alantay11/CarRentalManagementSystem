/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.CreditCard;
import entity.Customer;
import entity.Reservation;
import exception.CustomerNotFoundException;
import exception.ReservationRecordNotFoundException;
import exception.UpdateReservationStatusFailException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Uni
 */
public class CustomerServiceExecutiveModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;

    public CustomerServiceExecutiveModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public CustomerServiceExecutiveModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote,
            CarCategorySessionBeanRemote carCategorySessionBean, CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBean;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;

        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public void customerServiceExecutiveMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMSMC System :: Customer Service Executive ***\n");
            System.out.println("1: Pickup Car");
            System.out.println("2: Return car");
            System.out.println("3: Allocate Cars to Current Day Reservations");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doPickupCar();
                } else if (response == 2) {
                    doReturnCar();
                } else if (response == 3) {
                    allocateCars();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }

    }

    public void doPickupCar() {
        try {
            Scanner scanner = new Scanner(System.in);

            //List<Customer> customers = customerSessionBeanRemote.retrieveAllCustomers();

            /*System.out.println("\n-----------------------------------");
            for (Customer c : customers) {
                System.out.println(c.toString());
            }
            System.out.println("-----------------------------------\n");*/
            System.out.print("Enter customer username> ");
            String customerUsername = scanner.nextLine().trim();
            Customer customer = customerSessionBeanRemote.retrieveCustomerByUsername(customerUsername);
            long customerId = customer.getCustomerId();

            List<Reservation> reservations = reservationSessionBeanRemote.retrieveAllMyReservations(customerId);

            System.out.println("\n-----------------------------------");
            for (Reservation r : reservations) {
                System.out.println(r.toString());
            }
            System.out.println("-----------------------------------\n");

            System.out.print("Enter reservation ID> ");
            long reservationId = scanner.nextLong();
            scanner.nextLine();

            Reservation reservation = reservationSessionBeanRemote.retrieveReservation(reservationId);

            if (reservation.isPaid()) {
                reservation = reservationSessionBeanRemote.pickupCar(reservationId);
            } else {
                BigDecimal paymentAmount = reservation.getPaymentAmount();
                System.out.print("\nYou are required to pay $" + paymentAmount + " for the reservation");
                CreditCard cc = customer.getCreditCard();

                System.out.println("\nReservation of amount: $" + paymentAmount.toString() + " paid using " + cc.getCcNumber() + "\n");

                reservation = reservationSessionBeanRemote.pickupCar(reservationId);
            }

            System.out.println("Car picked up for " + reservation.toString());

        } catch (UpdateReservationStatusFailException ex) {
            System.out.println("Pickup has failed, please try again");
        } catch (CustomerNotFoundException ex) {
            System.out.println("You have input an invalid username");
        } catch (ReservationRecordNotFoundException ex) {
            System.out.println("Reservation does not exist");
        }
    }

    public void doReturnCar() {
        try {
            Scanner scanner = new Scanner(System.in);

            //List<Customer> customers = customerSessionBeanRemote.retrieveAllCustomers();

            /*System.out.println("\n-----------------------------------");
            for (Customer c : customers) {
                System.out.println(c.toString());
            }
            System.out.println("-----------------------------------\n");*/
            System.out.print("Enter customer username> ");
            String customerUsername = scanner.nextLine().trim();
            long customerId = customerSessionBeanRemote.retrieveCustomerByUsername(customerUsername).getCustomerId();

            List<Reservation> reservations = reservationSessionBeanRemote.retrieveAllMyReservations(customerId);

            System.out.println("\n-----------------------------------");
            for (Reservation r : reservations) {
                System.out.println(r.toString());
            }
            System.out.println("-----------------------------------\n");

            System.out.print("Enter reservation ID> ");
            long reservationId = scanner.nextLong();
            scanner.nextLine();

            // update reservation to reserved
            Reservation reservation = reservationSessionBeanRemote.returnCar(reservationId);

            System.out.println("Car picked up for " + reservation.toString());
        } catch (UpdateReservationStatusFailException ex) {
            System.out.println("Return has failed, please try again");
        } catch (CustomerNotFoundException ex) {
            System.out.println("You have input an invalid username");
        }
    }

    private void allocateCars() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter date in the format YYYY-MM-DD> ");
        String date = scanner.nextLine().trim();
        System.out.print("Enter time in the format HH:MM> ");
        String time = scanner.nextLine().trim();
        LocalDateTime dateTime = LocalDateTime.parse(date + "T" + time);

        reservationSessionBeanRemote.allocateCars(dateTime);

        System.out.println("Allocation complete");

    }
}
