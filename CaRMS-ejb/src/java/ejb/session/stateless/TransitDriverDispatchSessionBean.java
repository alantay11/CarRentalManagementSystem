/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.TransitDriverDispatch;
import exception.UpdateDispatchRecordFailException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Andrea
 */
@Stateless
public class TransitDriverDispatchSessionBean implements TransitDriverDispatchSessionBeanRemote, TransitDriverDispatchSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // c
    @Override
    public void createNewDispatchRecord(Reservation reservation, TransitDriverDispatch newDispatchRecord) {
        em.persist(newDispatchRecord);

        // set bi assoc
        newDispatchRecord.setReservation(reservation);
        reservation.setTransitDriverDispatch(newDispatchRecord);

        em.flush();
    }

    // r
    @Override
    public TransitDriverDispatch retrieveDispatchRecordById(Long transitDriverDispatchId) {
        TransitDriverDispatch dispatchRecord = em.find(TransitDriverDispatch.class, transitDriverDispatchId);
        return dispatchRecord;
    }

    // u
    @Override
    public void updateDispatchRecordCompleted(Long transitDriverDispatchId) throws UpdateDispatchRecordFailException {
        
        TransitDriverDispatch record = retrieveDispatchRecordById(transitDriverDispatchId);
        
        if(record == null) {
            throw new UpdateDispatchRecordFailException("Dispatch record Id " + transitDriverDispatchId + " does not exist!");
        }
        
        record.getReservation().getCar().setCurrentOutlet(record.getReservation().getDepartureOutlet());
        
        //set the outlet of the car to the current outlet
        // we don't have anything to mark tddr as completed yet
        
        em.flush();
    }

    // d
    @Override
    public void deleteDispatchRecord(Long transitDriverDispatchId) {
        
    }

}
