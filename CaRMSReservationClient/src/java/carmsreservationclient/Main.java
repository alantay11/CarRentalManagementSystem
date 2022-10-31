/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CreditCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Andrea
 */
public class Main {

    @EJB
    private static CreditCardSessionBeanRemote creditCardSessionBeanRemote;
    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;
    @EJB
    private static CarModelSessionBeanRemote carModelSessionBeanRemote; 
    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    @EJB
    private static CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerSessionBeanRemote, rentalRateSessionBeanRemote, carCategorySessionBeanRemote,
                reservationSessionBeanRemote, outletSessionBeanRemote, carModelSessionBeanRemote, carSessionBeanRemote, creditCardSessionBeanRemote);
        mainApp.runApp();
    }
}
