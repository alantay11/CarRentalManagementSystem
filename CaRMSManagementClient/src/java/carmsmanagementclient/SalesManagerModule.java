/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
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
            System.out.println("1: Create New Model");
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
                    //doCheckout();
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

}
