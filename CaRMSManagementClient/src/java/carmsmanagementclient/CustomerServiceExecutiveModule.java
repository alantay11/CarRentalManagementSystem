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
import entity.Car;
import entity.Customer;
import entity.Reservation;
import enumeration.CarStatusEnum;
import exception.CustomerNotFoundException;
import exception.ReservationRecordNotFoundException;
import exception.UpdateReservationStatusFailException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            CarCategorySessionBeanRemote carCategorySessionBean, CustomerSessionBeanRemote customerSessionBeanRemote) {
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
            System.out.println("3: Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doPickupCar();
                } else if (response == 2) {
                    doReturnCar();
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
            Reservation reservation = reservationSessionBeanRemote.pickupCar(reservationId);

            System.out.println("Car picked up for " + reservation.toString());

        } catch (UpdateReservationStatusFailException ex) {
            System.out.println("Pickup has failed, please try again");
        } catch (CustomerNotFoundException ex) {
            System.out.println("You have input an invalid username");
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
}
