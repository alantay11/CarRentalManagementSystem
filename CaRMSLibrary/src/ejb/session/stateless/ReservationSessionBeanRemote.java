/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import exception.ReservationRecordNotFoundException;
import exception.UpdateReservationStatusFailException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Uni
 */
@Remote
public interface ReservationSessionBeanRemote {

    List<Reservation> retrieveAllMyReservations(long customerId);

    Reservation retrieveReservation(long reservationId) throws ReservationRecordNotFoundException;

    void cancelReservation(long reservationId, BigDecimal refundAmount);

    Reservation createReservation(Reservation reservation);

    public List<Reservation> retrieveAllReservations();

    public List<Reservation> retrieveReservationByDate(LocalDate currDate);

    public Reservation pickupCar(long reservationId) throws UpdateReservationStatusFailException;

    Reservation returnCar(long reservationId) throws UpdateReservationStatusFailException;
    
}
