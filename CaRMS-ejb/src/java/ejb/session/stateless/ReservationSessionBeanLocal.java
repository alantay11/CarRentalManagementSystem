/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import exception.InputDataValidationException;
import exception.ReservationExistException;
import exception.ReservationRecordNotFoundException;
import exception.UpdateReservationStatusFailException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Uni
 */
@Local
public interface ReservationSessionBeanLocal {

    List<Reservation> retrieveAllMyReservations(long customerId);

    Reservation retrieveReservation(long reservationId) throws ReservationRecordNotFoundException;

    public List<Reservation> retrieveAllReservations();

    public List<Reservation> retrieveReservationByDate(LocalDate currDate);

    public Reservation pickupCar(long reservationId) throws UpdateReservationStatusFailException;

    Reservation createReservation(Reservation reservation) throws ReservationExistException, InputDataValidationException;

    void cancelReservation(long reservationId, BigDecimal refundAmount) throws ReservationRecordNotFoundException;

    void allocateCars(LocalDateTime dateTime);

}
