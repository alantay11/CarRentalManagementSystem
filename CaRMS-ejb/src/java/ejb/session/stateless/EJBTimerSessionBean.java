/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.CarModel;
import entity.Outlet;
import entity.Reservation;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Uni
 */
@Stateless
public class EJBTimerSessionBean implements EJBTimerSessionBeanRemote, EJBTimerSessionBeanLocal {

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;
   
    @EJB
    private CarSessionBeanLocal carSessionBean;
    
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    

    @Schedule(dayOfMonth = "*", hour = "2", info = "allocateCarsTimer")
    @Override
    public void allocateCarsTimer() {
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        String timestamp = formatter.format(new Date());
        
        System.out.println("***** Car Allocation Timer : Timeout At " + timestamp);
        
        LocalDate currDate = LocalDateTime.now().toLocalDate();
        List<Reservation> currDayReservationList = reservationSessionBean.retrieveReservationByDate(currDate);
        
        List<Reservation> finalReservationList = new ArrayList<>();
        for (Reservation r : currDayReservationList) {
            if (r.isCancelled() == false) {
                finalReservationList.add(r);
            }
        }
        triggerCarAllocation(finalReservationList);
    }
    
    
    private void triggerCarAllocation(List<Reservation> currDayReservationList) {
        for (Reservation reservation : currDayReservationList) {
            // requires a car of a specific make and model, (can be null)
            CarModel carModel = reservation.getCarModel();
            
            // or just any car from a particular category
            CarCategory carCategory = reservation.getCarCategory();
            
            // allocation of cars that are not currently in the particular outlet physically
            Outlet pickUpOutlet = reservation.getDepartureOutlet();
            
            if () {
                
            }
        }
    }

}
