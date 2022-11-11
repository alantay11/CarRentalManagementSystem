/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Car;
import entity.Reservation;
import enumeration.CarStatusEnum;
import exception.ReservationRecordNotFoundException;
import exception.UpdateReservationStatusFailException;
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

    public CustomerServiceExecutiveModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public CustomerServiceExecutiveModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBean) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBean;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
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
        System.out.print("Enter reservation ID> ");
        Long reservationId = scanner.nextLong();
        
        // update reservation to reserved
        reservationSessionBeanRemote.updateReservationStatus(reservationId);
        
        Reservation reservation = reservationSessionBeanRemote.retrieveReservation(reservationId);
        }
        
        catch (UpdateReservationStatusFailException ex) {
            System.out.println(ex.getMessage());
        } catch (ReservationRecordNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
      
    }
    
    public void doReturnCar() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter reservation ID> ");
            Long reservationId = scanner.nextLong();
            
            Reservation reservation = reservationSessionBeanRemote.retrieveReservation(reservationId);
            
            // set all back to normal
            Car car = reservation.getCar();
            car.setCarStatus(CarStatusEnum.AVAILABLE);
            car.setCurrentOutlet(reservation.getDestinationOutlet());
            
            car.setReservation(null);
            reservation.setCar(null);
        } catch (ReservationRecordNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
