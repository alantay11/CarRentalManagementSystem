/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    public Reservation retrieveReservation(long reservationid) {
        Reservation reservation = em.find(Reservation.class, reservationid);
        reservation.getRentalRateList().size();
        return reservation;
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
        Reservation reservation = retrieveReservation(reservationId);
        reservation.setCancelled(true);
        reservation.setPaid(true); // whether paid before or not it is considered done when cancelled
        reservation.setRefundAmount(refundAmount);
    }

}
