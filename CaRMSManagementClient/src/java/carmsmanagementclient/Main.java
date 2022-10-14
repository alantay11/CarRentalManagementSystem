/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Andrea
 */
public class Main {

    @EJB
    private static CarCategorySessionBeanRemote carCategorySessionBeanRemote;

    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
     public static void main(String[] args) {
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, rentalRateSessionBeanRemote, carCategorySessionBeanRemote);
        mainApp.runApp(); 
    }
    
}
