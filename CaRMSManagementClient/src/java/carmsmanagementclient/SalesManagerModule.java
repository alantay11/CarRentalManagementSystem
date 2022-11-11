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
import enumeration.RentalRateEnum;
import exception.InvalidIdException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Uni
 */
public class SalesManagerModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;

    public SalesManagerModule() {
    }

    public SalesManagerModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBean) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBean;
    }

    public void salesManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMSMC System :: Sales Manager ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View Rental Rate Details");
            System.out.println("4: Update Rental Rate");
            System.out.println("5: Delete Rental Rate");
            System.out.println("6: Logout\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateRentalRate();
                } else if (response == 2) {
                    doViewAllRentalRates();
                } else if (response == 3) {
                    doViewRentalRateDetails();
                } else if (response == 4) {
                    doUpdateRentalRate();
                } else if (response == 5) {
                    doDeleteRentalRate();
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

    public void doCreateRentalRate() {
        try {
            Scanner scanner = new Scanner(System.in);
            RentalRate rentalRate = new RentalRate();
            String startDate = "";
            String startTime = "";
            String endDate = "";
            String endTime = "";
            String type = "";

            System.out.println("*** CaRMSMC System :: Sales Manager :: Create Rental Rate ***\n");
            System.out.print("Enter rate name> ");
            rentalRate.setRateName(scanner.nextLine().trim());

            List<CarCategory> carCategoryList = carCategorySessionBeanRemote.retrieveAllCarCategories();

            int counter = 1;
            System.out.println("\n-----------------------------------");
            for (CarCategory c : carCategoryList) {
                System.out.println(counter + ": " + c.toString());
                counter++;
            }
            System.out.println("-----------------------------------\n");
            System.out.print("Enter ID of car category> ");
            long carCategoryId = scanner.nextLong();
            scanner.nextLine();
            rentalRate.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategory(carCategoryId));

            System.out.print("Enter type (Default/Promotion/Peak)> ");
            type = scanner.nextLine().trim();
            if (type.toLowerCase().equals("default")) {
                rentalRate.setRateType(RentalRateEnum.Default);
            } else if (type.toLowerCase().equals("promotion")) {
                rentalRate.setRateType(RentalRateEnum.Promotion);
            } else if (type.toLowerCase().equals("peak")) {
                rentalRate.setRateType(RentalRateEnum.Peak);
            }

            System.out.print("Enter rate per day> ");
            rentalRate.setRatePerDay(new BigDecimal(scanner.nextDouble()));
            scanner.nextLine();
            System.out.print("Enter start date in the format YYYY-MM-DD> ");
            startDate = scanner.nextLine().trim();
            System.out.print("Enter start time in the format HH:MM> ");
            startTime = scanner.nextLine().trim();
            rentalRate.setStartDate(LocalDateTime.parse(startDate + "T" + startTime));
            System.out.print("Enter end date in the format YYYY-MM-DD> ");
            endDate = scanner.nextLine().trim();
            System.out.print("Enter end time in the format HH:MM> ");
            endTime = scanner.nextLine().trim();
            rentalRate.setEndDate(LocalDateTime.parse(endDate + "T" + endTime));

            rentalRate = rentalRateSessionBeanRemote.createRentalRate(rentalRate, carCategoryId);

            System.out.println("\nNew " + rentalRate.toString() + " created\n");
        } catch (InvalidIdException ex) {
            System.out.println("You have entered an invalid ID!\n");
        } catch (DateTimeParseException ex) {
            System.out.println("You have entered an invalid date or time!\n");
        }
    }

    public void doViewAllRentalRates() {
        Scanner scanner = new Scanner(System.in);
        List<RentalRate> rentalRateList = getAllRentalRates();
        System.out.println("\n-----------------------------------");
        for (RentalRate r : rentalRateList) {
            System.out.println(r.toString());
        }
        System.out.println("-----------------------------------\n");
        System.out.print("Press enter to continue>");
        scanner.nextLine();
    }

    private List<RentalRate> getAllRentalRates() {
        List<RentalRate> rentalRateList = rentalRateSessionBeanRemote.retrieveAllRentalRates();
        return rentalRateList;
    }

    public void doViewRentalRateDetails() {
        Scanner scanner = new Scanner(System.in);
        List<RentalRate> rentalRates = getAllRentalRates();
        System.out.println("\n-----------------------------------");
        for (RentalRate r : rentalRates) {
            System.out.println("ID: " + r.getRentalRateRecordId() + ", Rate Name: " + r.getRateName());
        }
        System.out.println("-----------------------------------\n");

        System.out.print("Enter ID of rental rate you want to view> ");
        long rentalRateId = scanner.nextLong();
        scanner.nextLine();
        RentalRate rentalRate = rentalRateSessionBeanRemote.retrieveRentalRate(rentalRateId);
        System.out.println("\n" + rentalRate.toString() + "\n");
        System.out.print("Press enter to continue>");
        scanner.nextLine();
        System.out.println();
    }

    public void doUpdateRentalRate() {
        System.out.println("*** CaRMSMC System :: Sales Manager :: Update Rental Rate ***\n");
        Scanner scanner = new Scanner(System.in);
        Integer response;

        List<RentalRate> rentalRateList = getAllRentalRates();
        doViewAllRentalRates();
        System.out.print("Enter ID of rental rate you want to update> ");
        long rentalRateId = scanner.nextLong();
        RentalRate rentalRate = rentalRateSessionBeanRemote.retrieveRentalRate(rentalRateId);
        long oldCarCategoryId = rentalRate.getCarCategory().getCarCategoryId();
        long newCarCategoryId = oldCarCategoryId;

        String startDate = "";
        String startTime = "";
        String endDate = "";
        String endTime = "";

        while (true) {
            System.out.println("-----------------------------------");
            System.out.println("1: Edit Rate Name: " + rentalRate.getRateName());
            System.out.println("2: Edit Car Category: " + rentalRate.getCarCategory());
            System.out.println("3: Edit Rate per Day: $" + rentalRate.getRatePerDay());
            System.out.println("4: Edit Start Date & Time: " + rentalRate.getStartDate().toString().replace("T", " "));
            System.out.println("5: Edit End Date & Time: " + rentalRate.getEndDate().toString().replace("T", " "));
            System.out.println("6: Finish");
            System.out.println("-----------------------------------\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                try {
                    if (response == 1) {
                        System.out.print("Enter rate name> ");
                        rentalRate.setRateName(scanner.nextLine().trim());
                    } else if (response == 2) {
                        List<CarCategory> carCategoryList = carCategorySessionBeanRemote.retrieveAllCarCategories();
                        int counter = 1;
                        System.out.println("\n-----------------------------------");
                        for (CarCategory c : carCategoryList) {
                            System.out.println(counter + ": " + c.toString());
                            counter++;
                        }
                        System.out.println("-----------------------------------\n");
                        System.out.print("Enter ID of car category> ");
                        long carCategoryId = scanner.nextLong();
                        newCarCategoryId = carCategoryId;
                        scanner.nextLine();
                        rentalRate.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategory(carCategoryId));
                    } else if (response == 3) {
                        System.out.print("Enter rate per day> ");
                        rentalRate.setRatePerDay(new BigDecimal(scanner.nextDouble()));
                        scanner.nextLine();
                    } else if (response == 4) {
                        System.out.print("Enter start date in the format YYYY-MM-DD> ");
                        startDate = scanner.nextLine().trim();
                        System.out.print("Enter start time in the format HH:MM> ");
                        startTime = scanner.nextLine().trim();
                        rentalRate.setStartDate(LocalDateTime.parse(startDate + "T" + startTime));
                        System.out.print("Enter end date in the format YYYY-MM-DD> ");
                        endDate = scanner.nextLine().trim();
                        System.out.print("Enter end time in the format HH:MM> ");
                        endTime = scanner.nextLine().trim();
                        rentalRate.setEndDate(LocalDateTime.parse(endDate + "T" + endTime));
                    } else if (response == 6) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                } catch (InvalidIdException ex) {
                    System.out.println("\nYou have entered an invalid ID!\n");
                } catch (DateTimeParseException ex) {
                    System.out.println("\nYou have entered an invalid date or time!");
                }
            }
            if (response == 6) {
                break;
            }
        }

        System.out.print("\nConfirm update of " + rentalRate.toString() + "? (Y/N)> ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (confirmation.equals("y")) {
            try {
                rentalRate = rentalRateSessionBeanRemote.updateRentalRate(rentalRate, oldCarCategoryId, newCarCategoryId);
                System.out.println("\n" + rentalRate.toString() + " updated\n");
            } catch (InvalidIdException exception) {
                System.out.println("\nYou have entered an invalid ID!\n");
            }
        } else {
            System.out.println("\nUpdate cancelled\n");
        }
    }

    public void doDeleteRentalRate() {
        System.out.println("*** CaRMSMC System :: Sales Manager :: Delete Rental Rate ***\n");
        Scanner scanner = new Scanner(System.in);

        doViewAllRentalRates();
        System.out.print("Enter ID of rental rate you want to delete> ");
        long rentalRateId = scanner.nextLong();
        RentalRate rentalRate = rentalRateSessionBeanRemote.retrieveRentalRate(rentalRateId);
        scanner.nextLine();
        System.out.print("\nConfirm deletion of " + rentalRate.toString() + "? (Y/N)> ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (confirmation.equals("y")) {
            boolean deleted = rentalRateSessionBeanRemote.deleteRentalRate(rentalRateId);
            if (deleted) {
                System.out.println("\n" + rentalRate.toString() + " deleted\n");
            } else {
                System.out.println("\n" + rentalRate.toString() + " disabled\n");
            }
        } else {
            System.out.println("\nDeletion cancelled\n");
        }
    }

}
