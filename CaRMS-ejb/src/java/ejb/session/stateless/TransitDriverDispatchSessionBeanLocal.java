/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.TransitDriverDispatch;
import exception.UpdateDispatchRecordFailException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Andrea
 */
@Local
public interface TransitDriverDispatchSessionBeanLocal {

    public void createNewDispatchRecord(Reservation reservation, TransitDriverDispatch newDispatchRecord);

    public TransitDriverDispatch retrieveDispatchRecordById(Long transitDriverDispatchId);

    public void updateDispatchRecordCompleted(Long transitDriverDispatchId) throws UpdateDispatchRecordFailException;

    public void deleteDispatchRecord(Long transitDriverDispatchId);   

    List<TransitDriverDispatch> retrieveCurrentDayDispatches(String ddressName);

    TransitDriverDispatch updateAsCompleted(long dispatch);
    
}
