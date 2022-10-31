/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
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
    public void cancelReservation(long reservationId) {
        Reservation reservation = retrieveReservation(reservationId);
        reservation.setIsCancelled(true);
    }
    
    

}
