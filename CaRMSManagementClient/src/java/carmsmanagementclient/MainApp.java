/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchSessionBeanRemote;
import entity.Employee;
import enumeration.EmployeeAccessRightEnum;
import exception.InvalidLoginCredentialException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Uni
 */
public class MainApp {

    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote;

    private SalesManagerModule salesManagerModule;
    private CustomerServiceExecutiveModule customerServiceExecutiveModule;
    private OperationsManagerModule operationsManagerModule;

    private Employee currentEmployee;

    public MainApp() {
    }

    public MainApp(CarModelSessionBeanRemote carModelSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote,
            OutletSessionBeanRemote outletSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote) {
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.transitDriverDispatchSessionBeanRemote = transitDriverDispatchSessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response;

        try {
            while (true) {
                System.out.println("*** Welcome to CaRMS Management Client (CaRMSMC) ***\n");
                System.out.println("1: Login");
                System.out.println("2: Exit\n");
                response = 0;

                while (response < 1 || response > 2) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        try {
                            doLogin();
                            System.out.println("Login successful!\n");

                            if (currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.SALESMANAGER)) {
                                salesManagerModule = new SalesManagerModule(employeeSessionBeanRemote, rentalRateSessionBeanRemote, carCategorySessionBeanRemote);
                                salesManagerModule.salesManagerMenu();
                            } else if (currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.OPERATIONSMANAGER)) {
                                operationsManagerModule = new OperationsManagerModule(employeeSessionBeanRemote, rentalRateSessionBeanRemote, carCategorySessionBeanRemote, carModelSessionBeanRemote, carSessionBeanRemote, outletSessionBeanRemote, transitDriverDispatchSessionBeanRemote);
                                operationsManagerModule.operationsManagerMenu();
                            } else if (currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.CUSTOMERSERVICEEXECUTIVE)) {
                                customerServiceExecutiveModule = new CustomerServiceExecutiveModule(employeeSessionBeanRemote, rentalRateSessionBeanRemote, carCategorySessionBeanRemote, customerSessionBeanRemote);
                                customerServiceExecutiveModule.customerServiceExecutiveMenu();
                            } else if (currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.SYSTEMADMINISTRATOR)) {
                                sysAdminMenu();
                            }

                        } catch (InvalidLoginCredentialException ex) {
                            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                        }
                    } else if (response == 2) {
                        break;
                    } else {
                        System.out.print("Invalid option, please try again!\n");
                    }
                }

                if (response == 2) {
                    break;
                }
            }
        } catch (InputMismatchException ex) {
            System.out.print("Invalid option, please try again!\n");
        }
    }

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** CaRMSMC System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        try {
            currentEmployee = employeeSessionBeanRemote.employeeLogin(username, password);
        } catch (InvalidLoginCredentialException exception) {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void sysAdminMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response;

        try {
            while (true) {
                System.out.println("*** CaRMSMC System :: System Administrator ***\n");
                System.out.println("1: Create New Outlet");
                System.out.println("2: Create New Employee");
                System.out.println("3: Create New Partner");
                System.out.println("4: Create New Category");
                System.out.println("5: Sales Manager Menu");
                System.out.println("6: Operations Manager Menu");
                System.out.println("7: Customer Service Executive Menu");
                System.out.println("8: Logout\n");
                response = 0;

                while (response < 1 || response > 8) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        //doCreateOutlet();
                    } else if (response == 2) {
                        //doCreateEmployee();
                    } else if (response == 3) {
                        //doCreatePartner();
                    } else if (response == 4) {
                        //doCreateCategory();
                    } else if (response == 5) {
                        salesManagerModule = new SalesManagerModule(employeeSessionBeanRemote, rentalRateSessionBeanRemote, carCategorySessionBeanRemote);
                        salesManagerModule.salesManagerMenu();
                    } else if (response == 6) {
                        operationsManagerModule = new OperationsManagerModule(employeeSessionBeanRemote, rentalRateSessionBeanRemote, carCategorySessionBeanRemote, carModelSessionBeanRemote, carSessionBeanRemote, outletSessionBeanRemote, transitDriverDispatchSessionBeanRemote);
                        operationsManagerModule.operationsManagerMenu();
                    } else if (response == 7) {
                        customerServiceExecutiveModule = new CustomerServiceExecutiveModule(employeeSessionBeanRemote, rentalRateSessionBeanRemote, carCategorySessionBeanRemote, customerSessionBeanRemote);
                        customerServiceExecutiveModule.customerServiceExecutiveMenu();
                    } else if (response == 8) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }
                if (response == 8) {
                    break;
                }
            }

        } catch (InputMismatchException ex) {
            System.out.print("Invalid option, please try again!\n");
        }
    }

}
