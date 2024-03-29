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
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Andrea
 */
public class Main {

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    @EJB
    private static TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote;
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;
    @EJB
    private static CarModelSessionBeanRemote carModelSessionBeanRemote;
    @EJB
    private static CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(carModelSessionBeanRemote, employeeSessionBeanRemote, rentalRateSessionBeanRemote, carCategorySessionBeanRemote, carSessionBeanRemote,
                outletSessionBeanRemote, customerSessionBeanRemote, transitDriverDispatchSessionBeanRemote, reservationSessionBeanRemote);
        mainApp.runApp();
    }

}
