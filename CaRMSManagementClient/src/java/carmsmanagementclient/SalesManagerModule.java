/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.RentalRate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 *
 * @author Uni
 */
public class SalesManagerModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;

    public SalesManagerModule() {
    }

    public SalesManagerModule(EmployeeSessionBeanRemote employeeSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
    }
    
    public void salesManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMSMC System :: Sales Manager ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateRentalRate();
                }
                else if(response == 2)
                {
                    //doVoidRefund();
                }
                else if(response == 3)
                {
                    //doViewMySaleTransactions();
                }
                else if(response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    
    public void doCreateRentalRate() {
        Scanner scanner = new Scanner(System.in);
        String rateName = "";
        BigDecimal ratePerDay;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        
        System.out.println("*** CaRMSMC System :: Sales Manager :: Create Rental Rate ***\n");
        System.out.print("Enter rate name> ");
        rateName = scanner.nextLine().trim();
        System.out.print("Enter rate per day> ");
        ratePerDay = new BigDecimal(scanner.nextDouble());
        scanner.nextLine();
        System.out.print("Enter start date and time> ");
        startDateTime = LocalDateTime.parse("14-10-2022, 12:00");
        System.out.print("Enter end date and time> ");
        endDateTime = LocalDateTime.parse("15-10-2022, 12:00");
        RentalRate rentalRate = new RentalRate(rateName, ratePerDay, startDateTime, endDateTime);
        
        

        
    }

}
