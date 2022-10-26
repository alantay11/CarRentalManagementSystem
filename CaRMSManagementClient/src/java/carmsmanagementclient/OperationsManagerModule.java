/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Outlet;
import enumeration.CarStatusEnum;
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
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;

    public OperationsManagerModule() {
    }

    public OperationsManagerModule(EmployeeSessionBeanRemote employeeSessionBeanRemote,
            RentalRateSessionBeanRemote rentalRateSessionBeanRemote,
            CarCategorySessionBeanRemote carCategorySessionBeanRemote,
            CarModelSessionBeanRemote carModelSessionBeanRemote,
            CarSessionBeanRemote carSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
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
                    doCreateCarModel();
                } else if (response == 2) {
                    doViewAllCarModels();
                } else if (response == 3) {
                    doUpdateCarModel();
                } else if (response == 4) {
                    doDeleteCarModel();
                } else if (response == 5) {
                    doCreateNewCar();
                } else if (response == 6) {
                    doViewAllCars();
                } else if (response == 7) {
                    doViewCarDetails();
                } else if (response == 8) {
                    doUpdateCar();
                } else if (response == 9) {
                    doDeleteCar();
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

    // case #1
    public void doCreateCarModel() {
        try {
            Scanner scanner = new Scanner(System.in);
            CarModel carModel = new CarModel();
            String make = "";
            String model = "";

            System.out.println("*** CaRMSMC System :: Operations Manager :: Create New Car Model ***\n");

            // retrieve car category first
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
            carModel.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategory(carCategoryId));
            scanner.nextLine();

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

    // case #2
    private List<CarModel> getAllCarModels() {
        List<CarModel> carModelList = carModelSessionBeanRemote.retrieveAllCarModels();
        return carModelList;
    }

    // case #3
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

    // case #3
    public void doUpdateCarModel() {
        System.out.println("*** CaRMSMC System :: Operations Manager :: Update Car Model ***\n");
        Scanner scanner = new Scanner(System.in);
        Integer response;

        // List<CarModel> carModelList = getAllCarModels();
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
                        carModel.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategory(carCategoryId));
                    } else if (response == 4) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                } catch (InvalidIdException ex) {
                    System.out.println("\nYou have entered an invalid ID!\n");
                }
            }
            if (response == 4) {
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

    // case #4
    public void doDeleteCarModel() {
        System.out.println("*** CaRMSMC System :: Operations Manager :: Delete Car Model ***\n");
        Scanner scanner = new Scanner(System.in);

        doViewAllCarModels();
        System.out.print("Enter ID of car model you want to delete> ");
        long carModelId = scanner.nextLong();
        CarModel carModel = carModelSessionBeanRemote.retrieveCarModel(carModelId);
        scanner.nextLine();

        System.out.print("\nConfirm deletion of " + carModel.toString() + "? (Y/N)> ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("y")) {
            boolean deleted = carModelSessionBeanRemote.deleteCarModel(carModelId);
            if (deleted) {
                System.out.println("\n" + carModel.toString() + " deleted\n");
            } else {
                System.out.println("\n" + carModel.toString() + " disabled\n");
            }
        } else {
            System.out.println("\nDeletion cancelled\n");
        }
    }

    // case #5
    public void doCreateNewCar() {
        try {
            Scanner scanner = new Scanner(System.in);
            Car car = new Car();
            String licensePlateNum = "";
            String color = "";
            String carStatus;

            System.out.println("*** CaRMSMC System :: Operations Manager :: Create Car ***\n");

            // create car for particular make and model 
            List<CarModel> carModelList = carModelSessionBeanRemote.retrieveAllCarModels();

            int counter = 1;
            System.out.println("\n-----------------------------------");
            for (CarModel cm : carModelList) {
                System.out.println(counter + ": " + cm.toString());
                counter++;
            }
            System.out.println("-----------------------------------\n");
            System.out.print("Enter ID of car model> ");
            long carModelId = scanner.nextLong();
            car.setModel(carModelSessionBeanRemote.retrieveCarModel(carModelId));
            scanner.nextLine();

            // create new car with license plate number, colour, status 
            // (in outlet or on rental) and location (specific customer or outlet).
            System.out.print("Enter license plate number> ");
            licensePlateNum = scanner.nextLine().trim();
            car.setLicensePlateNum(licensePlateNum);

            System.out.print("Enter color> ");
            color = scanner.nextLine().trim();
            car.setColor(color);

            System.out.print("Enter status (Outlet/Rented)> ");
            carStatus = scanner.nextLine().trim();
            if (carStatus.toLowerCase().equals("outlet")) {
                car.setCarStatus(CarStatusEnum.INOUTLET);
            } else if (carStatus.toLowerCase().equals("rented")) {
                car.setCarStatus(CarStatusEnum.ONRENTAL);
            }

            List<Outlet> outletList = outletSessionBeanRemote.retrieveAllOutlets();
            counter = 1;
            System.out.println("\n-----------------------------------");
            for (Outlet o : outletList) {
                System.out.println(counter + ": " + o.toString());
                counter++;
            }
            System.out.println("-----------------------------------\n");
            System.out.print("Enter ID of outlet> ");
            long outletId = scanner.nextLong();
            car.setModel(carModelSessionBeanRemote.retrieveCarModel(carModelId));
            scanner.nextLine();

            car = carSessionBeanRemote.createCar(car, carModelId, outletId);

            System.out.println("\nNew " + car.toString() + " created\n");
        } catch (Exception ex) {
            System.out.println("You have entered an invalid field!\n");
        }
    }

    // case #6
    private List<Car> getAllCars() {
        List<Car> carList = carSessionBeanRemote.retrieveAllCars();
        return carList;
    }

    public void doViewAllCars() {
        Scanner scanner = new Scanner(System.in);
        List<Car> carList = getAllCars();
        System.out.println("\n-----------------------------------");
        for (Car c : carList) {
            System.out.println(c.toString());
        }
        System.out.println("-----------------------------------\n");
        System.out.print("Press enter to continue>");
        scanner.nextLine();
    }

    // case #7
    private void doViewCarDetails() {
        Scanner scanner = new Scanner(System.in);
        List<Car> carList = getAllCars();

        System.out.println("\n-----------------------------------");
        for (Car c : carList) {
            System.out.println("ID: " + c.getCarId() + ", Car Model: " + c.getModel());
        }
        System.out.println("-----------------------------------\n");

        System.out.print("Enter ID of car you want to view> ");
        long carId = scanner.nextLong();
        scanner.nextLine();
        Car car;
        try {
            car = carSessionBeanRemote.retrieveCar(carId);
            System.out.println("\n" + car.toString() + "\n");
            System.out.print("Press enter to continue>");
            scanner.nextLine();
            System.out.println();
        } catch (InvalidIdException ex) {
            System.out.println("You have entered an invalid ID!\n");
        }
    }

    // case #8
    private void doUpdateCar() {
        System.out.println("*** CaRMSMC System :: Operations Manager :: Update Car ***\n");
        Scanner scanner = new Scanner(System.in);
        Integer response;

        doViewAllCars();
        System.out.print("Enter ID of car you want to update> ");
        long carId = scanner.nextLong();
        Car car;
        try {
            car = carSessionBeanRemote.retrieveCar(carId);

            // attributes: license plate number, colour, status 
            // (in outlet or on rental) and location (specific customer or outlet).
            String licensePlateNum = "";
            String color = "";
            String carStatus = "";
            String location = "";

            while (true) {
                System.out.println("-----------------------------------");
                System.out.println("1: Edit Car License Plate Number: " + car.getLicensePlateNum());
                System.out.println("2: Edit Car Color: " + car.getColor());
                System.out.println("3: Edit Car Status: $" + car.getCarStatus());
                System.out.println("4: Edit Car Location: " + car.getCurrentOutlet());
                System.out.println("5: Finish");
                System.out.println("-----------------------------------\n");
                response = 0;

                while (response < 1 || response > 5) {
                    System.out.print("> ");

                    response = scanner.nextInt();
                    scanner.nextLine();

                    try {
                        if (response == 1) {
                            System.out.println("Enter Car License Plate Number> ");
                            car.setLicensePlateNum(scanner.nextLine().trim());
                        } else if (response == 2) {
                            System.out.println("Enter Car Color> ");
                            car.setColor(scanner.nextLine().trim());
                        } else if (response == 3) {
                            System.out.println("Enter Car Status> ");
                            carStatus = scanner.nextLine().trim();
                            if (CarStatusEnum.INOUTLET.equals(carStatus)) {
                                car.setCarStatus(CarStatusEnum.INOUTLET);
                            } else if (carStatus.equals(CarStatusEnum.ONRENTAL)) {
                                car.setCarStatus(CarStatusEnum.ONRENTAL);
                            }
                        } else if (response == 4) {
                            System.out.println("Enter Car Location> ");
                            // fill in here

                        } else if (response == 5) {
                            break;
                        } else {
                            System.out.println("Invalid option, please try again!\n");
                        }
                    } catch (Exception ex) {
                        System.out.println("\nYou have entered an invalid field!\n");
                    }
                }

                if (response == 5) {
                    break;
                }

            }

            System.out.print("\nConfirm update of " + car.toString() + "? (Y/N)> ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (confirmation.equals("y")) {
                car = carSessionBeanRemote.updateCar(car);
                System.out.println("\n" + car.toString() + " updated\n");
            } else {
                System.out.println("\nUpdate cancelled\n");
            }
        } catch (InvalidIdException ex) {
            System.out.println("You have entered an invalid ID!\n");
        }
    }

    // case #9
    public void doDeleteCar() {
        System.out.println("*** CaRMSMC System :: Operations Manager :: Delete Car ***\n");
        Scanner scanner = new Scanner(System.in);

        doViewAllCars();
        System.out.print("Enter ID of car you want to delete> ");
        long carId = scanner.nextLong();
        try {
            Car car = carSessionBeanRemote.retrieveCar(carId);
            scanner.nextLine();
            System.out.print("\nConfirm deletion of " + car.toString() + "? (Y/N)> ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (confirmation.equals("y")) {
                boolean deleted = carSessionBeanRemote.deleteCar(carId);
                if (deleted) {
                    System.out.println("\n" + car.toString() + " deleted\n");
                } else {
                    System.out.println("\n" + car.toString() + " disabled\n");
                }
            } else {
                System.out.println("\nDeletion cancelled\n");
            }
        } catch (InvalidIdException ex) {
            System.out.println("You have entered an invalid ID!\n");
        }
    }

}
