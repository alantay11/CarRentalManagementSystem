/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import java.util.Scanner;

/**
 *
 * @author Uni
 */
public class OperationsManagerModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBean;

    public OperationsManagerModule() {
    }

    public OperationsManagerModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBean) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBean = carCategorySessionBean;
    }

    public void operationsManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMSMC System :: Operations Manager ***\n");
            System.out.println("1: Create New Model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Cars");
            System.out.println("7: View Car Details");
            System.out.println("8: Update Car");
            System.out.println("9: Delete Car");
            System.out.println("10: View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("11: Assign Transit Driver");
            System.out.println("12: Update Transit As Completed");

            System.out.println("13: Logout\n");
            response = 0;

            while (response < 1 || response > 13) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    //  doCreateRentalRate();
                } else if (response == 2) {
                    //  doViewAllRentalRates();
                } else if (response == 3) {
                    //  doViewRentalRateDetails();
                } else if (response == 4) {
                    //  doUpdateRentalRate();
                } else if (response == 5) {
                    // doDeleteRentalRate();
                } else if (response == 6) {
                    //  doViewAllRentalRates();
                } else if (response == 7) {
                    //  doViewRentalRateDetails();
                } else if (response == 8) {
                    // doUpdateRentalRate();
                } else if (response == 9) {
                    // doDeleteRentalRate();
                } else if (response == 10) {
                    // doViewAllRentalRates();
                } else if (response == 11) {
                    // doViewRentalRateDetails();
                } else if (response == 12) {
                    //doUpdateRentalRate();
                } else if (response == 13) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 13) {
                break;
            }
        }
    }
}
