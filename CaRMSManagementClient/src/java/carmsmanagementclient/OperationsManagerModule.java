/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.CarCategory;
import entity.CarModel;
import exception.InvalidIdException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Uni
 */
public class OperationsManagerModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBean;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;

    public OperationsManagerModule() {
    }

    public OperationsManagerModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, 
            RentalRateSessionBeanRemote rentalRateSessionBeanRemote, 
            CarCategorySessionBeanRemote carCategorySessionBean,
            CarModelSessionBeanRemote carModelSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBean = carCategorySessionBean;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
    }

    public void operationsManagerMenu() throws InvalidIdException {
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
                    doCreateCarModel();
                } else if (response == 2) {
                    doViewAllCarModels();
                } else if (response == 3) {
                    doUpdateCarModel();
                } else if (response == 4) {
                    //  doDeleteCarModel();
                } else if (response == 5) {
                    // doCreateNewCar();
                } else if (response == 6) {
                    //  doViewAllCars();
                } else if (response == 7) {
                    //  doViewCarDetails();
                } else if (response == 8) {
                    // doUpdateCar();
                } else if (response == 9) {
                    // doDeleteCar();
                } else if (response == 10) {
                    // doViewCurrentDayTransitDriverDispatchRecords();
                } else if (response == 11) {
                    // doAssignTransitDriver();
                } else if (response == 12) {
                    //doUpdateTransitDriverDispatchRecord();
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
    
    public void doCreateCarModel() {
        try {
            Scanner scanner = new Scanner(System.in);
            CarModel carModel = new CarModel();
            String make = "";
            String model = "";
            
            System.out.println("*** CaRMSMC System :: Operations Manager :: Create New Car Model ***\n");
            System.out.print("Enter name of car model> ");
            carModel.setModel(scanner.nextLine().trim());
            
            // retrieve car category first
            List<CarCategory> carCategoryList = carCategorySessionBean.retrieveAllCarCategories();
            
            int counter = 1;
            System.out.println("\n-----------------------------------");
            for (CarCategory c : carCategoryList) {
                System.out.println(counter + ": " + c.toString());
                counter++;
            }
            System.out.println("-----------------------------------\n");
            System.out.print("Enter ID of car category> ");
            long carCategoryId = scanner.nextLong();
            carModel.setCarCategory(carCategorySessionBean.retrieveCarCategory(carCategoryId));
            
            // create new model w/ make and model attributes
            System.out.print("Enter make of car> ");
            make = scanner.nextLine().trim();
            carModel.setMake(make);
            
            System.out.print("Enter model of car> ");
            model = scanner.nextLine().trim();
            carModel.setModel(model);
            
            carModel = carModelSessionBeanRemote.createCarModel(carModel);
            
            System.out.println("\nNew " + carModel.toString() + " created\n");
            
        } catch (InvalidIdException ex) {
            System.out.println("You have entered an invalid ID!\n");
        }
    }
    
    private List<CarModel> getAllCarModels() {
        List<CarModel> carModelList = carModelSessionBeanRemote.retrieveAllCarModels();
        return carModelList;
    }
    
    public void doViewAllCarModels() {
        Scanner scanner = new Scanner(System.in);
        List<CarModel> carModelList = getAllCarModels();
        
        System.out.println("\n-----------------------------------");
        for (CarModel cm : carModelList) {
            System.out.println(cm.toString());
        }
        System.out.println("-----------------------------------\n");
        System.out.print("Press enter to continue>");
        scanner.nextLine();
    }
    
    public void doUpdateCarModel() throws InvalidIdException {
        System.out.println("*** CaRMSMC System :: Operations Manager :: Update Car Model ***\n");
        Scanner scanner = new Scanner(System.in);
        Integer response;
        
        List<CarModel> carModelList = getAllCarModels();
        doViewAllCarModels();
        System.out.print("Enter ID of car model you want to update> ");
        long carModelId = scanner.nextLong();
        CarModel carModel = carModelSessionBeanRemote.retrieveCarModel(carModelId);
        
        String make = "";
        String model = "";
        
        while (true) {
            System.out.println("-----------------------------------");
            System.out.println("1: Edit Car Make: " + carModel.getMake());
            System.out.println("2: Edit Car Model: " + carModel.getModel());
            System.out.println("3: Edit Car Category: " + carModel.getCarCategory());
            System.out.println("4: Finish");
            System.out.println("-----------------------------------\n");
            response = 0;
            
            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                try {
                    if (response == 1) {
                        System.out.print("Enter car make> ");
                        carModel.setMake(scanner.nextLine().trim());
                    } else if (response == 2) {
                        System.out.print("Enter car model> ");
                        carModel.setModel(scanner.nextLine().trim());
                    } else if (response == 3) {
                        List<CarCategory> carCategoryList = carCategorySessionBean.retrieveAllCarCategories();
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
                        carModel.setCarCategory(carCategorySessionBean.retrieveCarCategory(carCategoryId));
                    } else if (response == 4) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                } catch (InvalidIdException ex) {
                    System.out.println("\nYou have entered an invalid ID!\n");
                }
            } if (response == 4) {
                break;
            }
        }
        
        System.out.print("\nConfirm update of " + carModel.toString() + "? (Y/N)> ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (confirmation.equals("y")) {
            carModel = carModelSessionBeanRemote.updateCarModel(carModel);
            System.out.println("\n" + carModel.toString() + " updated\n");
        } else {
            System.out.println("\nUpdate cancelled\n");
        }
    }
    
    
}
