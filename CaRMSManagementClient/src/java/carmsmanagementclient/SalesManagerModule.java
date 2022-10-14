/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.CarCategory;
import entity.RentalRate;
import exception.InvalidCarCategoryNameException;
import exception.InvalidRentalRateNameException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Uni
 */
public class SalesManagerModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBean;

    public SalesManagerModule() {
    }

    public SalesManagerModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBean) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBean = carCategorySessionBean;
    }

    public void salesManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMSMC System :: Sales Manager ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: Update Rental Rate");
            System.out.println("3: Delete Rental Rate");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateRentalRate();
                } else if (response == 2) {
                    doUpdateRentalRate();
                } else if (response == 3) {
                    doDeleteRentalRate();
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

    public void doCreateRentalRate() {
        try {
            Scanner scanner = new Scanner(System.in);
            RentalRate rentalRate = new RentalRate();
            String categoryName = "";
            String startDate = "";
            String startTime = "";
            String endDate = "";
            String endTime = "";
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;

            System.out.println("*** CaRMSMC System :: Sales Manager :: Create Rental Rate ***\n");
            System.out.print("Enter rate name> ");
            rentalRate.setRateName(scanner.nextLine().trim());

            List<CarCategory> carCategoryList = carCategorySessionBean.retrieveAllCarCategories();

            System.out.println("\n-----------------------------------");
            for (CarCategory c : carCategoryList) {
                System.out.println(c.toString());
            }
            System.out.println("-----------------------------------\n");
            System.out.print("Enter name of car category verbatim> ");
            String carCategoryName = scanner.nextLine().trim();
            rentalRate.setCarCategory(carCategorySessionBean.retrieveCarCategoryUsingName(carCategoryName));

            System.out.print("Enter rate per day> ");
            rentalRate.setRatePerDay(new BigDecimal(scanner.nextDouble()));
            scanner.nextLine();
            System.out.print("Enter start date in the format YYYY-MM-DD> ");
            startDate = scanner.nextLine().trim();
            System.out.print("Enter start time in the format HH:MM> ");
            startTime = scanner.nextLine().trim();
            rentalRate.setStartDateTime(LocalDateTime.parse(startDate + "T" + startTime));
            System.out.print("Enter end date in the format YYYY-MM-DD> ");
            endDate = scanner.nextLine().trim();
            System.out.print("Enter end time in the format HH:MM> ");
            endTime = scanner.nextLine().trim();
            rentalRate.setEndDateTime(LocalDateTime.parse(endDate + "T" + endTime));

            rentalRate = rentalRateSessionBeanRemote.createRentalRate(rentalRate);

            System.out.println("\nNew " + rentalRate.toString() + " created\n");
        } catch (InvalidCarCategoryNameException ex) {
            System.out.println("You have entered an invalid car category!");
        }
    }

    public void doUpdateRentalRate() {
        try {// all placeholder
            System.out.println("*** CaRMSMC System :: Sales Manager :: Update Rental Rate ***\n");
            
            Scanner scanner = new Scanner(System.in);
            List<RentalRate> rentalRateList = rentalRateSessionBeanRemote.retrieveAllRentalRates();

            System.out.println("\n-----------------------------------");
            for (RentalRate r : rentalRateList) {
                System.out.println(r.toString());
            }
            System.out.println("-----------------------------------\n");
            System.out.print("Enter name of rental rate you want to update verbatim> ");
            String rentalRateName = scanner.nextLine().trim();
            RentalRate rentalRate = rentalRateSessionBeanRemote.retrieveRentalRateUsingName(rentalRateName);
                               
                    
            ///////////////////// copy pasted from above
            String categoryName = "";
            String startDate = "";
            String startTime = "";
            String endDate = "";
            String endTime = "";
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;

            
            System.out.print("Enter rate name> ");
            rentalRate.setRateName(scanner.nextLine().trim());

            List<CarCategory> carCategoryList = carCategorySessionBean.retrieveAllCarCategories();

            System.out.println("\n-----------------------------------");
            for (CarCategory c : carCategoryList) {
                System.out.println(c.toString());
            }
            System.out.println("-----------------------------------\n");
            System.out.print("Enter name of car category verbatim> ");
            String carCategoryName = scanner.nextLine().trim();
            rentalRate.setCarCategory(carCategorySessionBean.retrieveCarCategoryUsingName(carCategoryName));

            System.out.print("Enter rate per day> ");
            rentalRate.setRatePerDay(new BigDecimal(scanner.nextDouble()));
            scanner.nextLine();
            System.out.print("Enter start date in the format YYYY-MM-DD> ");
            startDate = scanner.nextLine().trim();
            System.out.print("Enter start time in the format HH:MM> ");
            startTime = scanner.nextLine().trim();
            rentalRate.setStartDateTime(LocalDateTime.parse(startDate + "T" + startTime));
            System.out.print("Enter end date in the format YYYY-MM-DD> ");
            endDate = scanner.nextLine().trim();
            System.out.print("Enter end time in the format HH:MM> ");
            endTime = scanner.nextLine().trim();
            rentalRate.setEndDateTime(LocalDateTime.parse(endDate + "T" + endTime));

            /// create this method
            rentalRate = rentalRateSessionBeanRemote.updateRentalRate(rentalRate);

            System.out.println("\n" + rentalRate.toString() + " updated\n");
        } catch (InvalidRentalRateNameException ex) {
            System.out.println("You have entered an invalid rental rate name!");
        } catch (InvalidCarCategoryNameException ex) {
            System.out.println("You have entered an invalid car category!");
        }
    }

    public void doDeleteRentalRate() {
        try {// all placeholder
            Scanner scanner = new Scanner(System.in);
            RentalRate rentalRate = new RentalRate();
            String categoryName = "";
            String startDate = "";
            String startTime = "";
            String endDate = "";
            String endTime = "";
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;

            System.out.println("*** CaRMSMC System :: Sales Manager :: Create Rental Rate ***\n");
            System.out.print("Enter rate name> ");
            rentalRate.setRateName(scanner.nextLine().trim());

            List<CarCategory> carCategoryList = carCategorySessionBean.retrieveAllCarCategories();

            System.out.println("\n-----------------------------------");
            for (CarCategory c : carCategoryList) {
                System.out.println(c.toString());
            }
            System.out.println("-----------------------------------\n");
            System.out.print("Enter name of car category verbatim> ");
            String carCategoryName = scanner.nextLine().trim();
            rentalRate.setCarCategory(carCategorySessionBean.retrieveCarCategoryUsingName(carCategoryName));

            System.out.print("Enter rate per day> ");
            rentalRate.setRatePerDay(new BigDecimal(scanner.nextDouble()));
            scanner.nextLine();
            System.out.print("Enter start date in the format YYYY-MM-DD> ");
            startDate = scanner.nextLine().trim();
            System.out.print("Enter start time in the format HH:MM> ");
            startTime = scanner.nextLine().trim();
            rentalRate.setStartDateTime(LocalDateTime.parse(startDate + "T" + startTime));
            System.out.print("Enter end date in the format YYYY-MM-DD> ");
            endDate = scanner.nextLine().trim();
            System.out.print("Enter end time in the format HH:MM> ");
            endTime = scanner.nextLine().trim();
            rentalRate.setEndDateTime(LocalDateTime.parse(endDate + "T" + endTime));

            rentalRate = rentalRateSessionBeanRemote.createRentalRate(rentalRate);

            System.out.println("\nNew " + rentalRate.toString() + " created\n");
        } catch (InvalidCarCategoryNameException ex) {
            System.out.println("You have entered an invalid car category!");
        }

    }

}
