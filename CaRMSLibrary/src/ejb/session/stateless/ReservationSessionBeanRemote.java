/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Uni
 */
@Remote
public interface ReservationSessionBeanRemote {

    List<Reservation> retrieveAllMyReservations(long customerId);

    Reservation retrieveReservation(long reservationid);

    void cancelReservation(long reservationId);

    Reservation createReservation(Reservation reservation);
    
}
