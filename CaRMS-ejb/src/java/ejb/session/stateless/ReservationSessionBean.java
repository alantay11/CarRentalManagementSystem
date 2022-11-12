/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Reservation;
import enumeration.CarStatusEnum;
import exception.ReservationRecordNotFoundException;
import exception.UpdateReservationStatusFailException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Uni
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public Reservation createReservation(Reservation reservation) {
        em.persist(reservation);

        em.flush();
        return reservation;
    }

    @Override
    public List<Reservation> retrieveAllMyReservations(long customerId) {
        Query query = em.createQuery("SELECT r from Reservation r where r.customer.customerId = :customerId");
        query.setParameter("customerId", customerId);

        List<Reservation> reservations = query.getResultList();

        for (Reservation r : reservations) {
            r.getRentalRateList().size();
        }

        return reservations;
    }

    @Override
    public Reservation retrieveReservation(long reservationId) throws ReservationRecordNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        reservation.getRentalRateList().size();

        if (reservation != null) {
            return reservation;
        } else {
            throw new ReservationRecordNotFoundException(("Reservation ID " + reservationId + " does not exist!"));
        }
    }

    @Override
    public List<Reservation> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r FROM Reservation r");
        return query.getResultList();
    }

    @Override
    public List<Reservation> retrieveReservationByDate(LocalDate currDate) {
        List<Reservation> allReservations = retrieveAllReservations();
        List<Reservation> currDayReservations = new ArrayList<>();

        for (Reservation r : allReservations) {
            if (r.getPickupTime().toLocalDate().isEqual(currDate)) {
                currDayReservations.add(r);
            }
        }
        return currDayReservations;
    }

    @Override
    public void cancelReservation(long reservationId, BigDecimal refundAmount) {
        try {
            Reservation reservation = retrieveReservation(reservationId);
            reservation.setCancelled(true);
            reservation.setPaid(true); // whether paid before or not it is considered done when cancelled
            reservation.setRefundAmount(refundAmount);
        } catch (ReservationRecordNotFoundException ex) {
            Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Reservation pickupCar(long reservationId) throws UpdateReservationStatusFailException {

        try {
            Reservation reservation = retrieveReservation(reservationId);

            // set as reserved
            //reservation.getCar().setCarStatus(CarStatusEnum.RESERVED);
            reservation.getCar().setCurrentOutlet(null);
            em.flush();
            return reservation;
        } catch (ReservationRecordNotFoundException ex) {
            throw new UpdateReservationStatusFailException("Reservation Id " + reservationId + " does not exist.");
        }
    }

    @Override
    public Reservation returnCar(long reservationId) throws UpdateReservationStatusFailException {
        try {
            Reservation reservation = retrieveReservation(reservationId);

            Car car = reservation.getCar();
            car.setCarStatus(CarStatusEnum.AVAILABLE);
            car.setCurrentOutlet(reservation.getDestinationOutlet());

            car.setReservation(null);
            reservation.setCar(null);
            
            em.flush();
            return reservation;
        } catch (ReservationRecordNotFoundException ex) {
            throw new UpdateReservationStatusFailException("Reservation Id " + reservationId + " does not exist.");
        }        
    }

}
