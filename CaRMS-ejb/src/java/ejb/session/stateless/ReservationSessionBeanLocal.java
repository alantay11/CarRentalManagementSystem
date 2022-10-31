/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Uni
 */
@Local
public interface ReservationSessionBeanLocal {

    List<Reservation> retrieveAllMyReservations(long customerId);

    Reservation retrieveReservation(long reservationid);
    
}
