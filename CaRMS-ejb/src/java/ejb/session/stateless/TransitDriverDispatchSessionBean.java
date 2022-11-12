/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Reservation;
import entity.TransitDriverDispatch;
import exception.UpdateDispatchRecordFailException;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Andrea
 */
@Stateless
public class TransitDriverDispatchSessionBean implements TransitDriverDispatchSessionBeanRemote, TransitDriverDispatchSessionBeanLocal {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

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

        if (record == null) {
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

    @Override
    public List<TransitDriverDispatch> retrieveCurrentDayDispatches(String addressName) {
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatch t WHERE t.pickupTime >= today AND t.dropoffTime <= tomorrow AND t.destinationOutlet.address = : addressName");
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime tomorrow = today.plusDays(1);
        query.setParameter("today", today).setParameter("tomorrow", tomorrow).setParameter("outletId", addressName);
        List<TransitDriverDispatch> dispatches = query.getResultList();

        return (List<TransitDriverDispatch>) dispatches;
    }

    @Override
    public TransitDriverDispatch assignTransitDriver(long employeeId, long dispatchId) {
        TransitDriverDispatch transitDriverDispatch = retrieveDispatchRecordById(dispatchId);
        Employee employee = employeeSessionBeanLocal.retrieveEmployee(employeeId);

        transitDriverDispatch.setEmployee(employee);
        employee.setTransitDriverDispatchRecord(transitDriverDispatch);

        em.flush();

        return transitDriverDispatch;
    }

    @Override
    public TransitDriverDispatch updateAsCompleted(long dispatchId) {
        TransitDriverDispatch transitDriverDispatch = retrieveDispatchRecordById(dispatchId);
        Employee employee = transitDriverDispatch.getEmployee();

        transitDriverDispatch.setEmployee(null);
        employee.setTransitDriverDispatchRecord(null);

        return transitDriverDispatch;
    }

}
