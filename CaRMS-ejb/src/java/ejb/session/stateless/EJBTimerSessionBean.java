/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author Uni
 */
@Stateless
public class EJBTimerSessionBean implements EJBTimerSessionBeanRemote, EJBTimerSessionBeanLocal {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Schedule(dayOfMonth = "*", hour = "0", info = "allocateCarsToCurrentDayReservations")
    @Override
    public void allocateCarsToCurrentDayReservations() {
    }

}
