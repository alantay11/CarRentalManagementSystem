/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Outlet;
import entity.Reservation;
import entity.TransitDriverDispatch;
import enumeration.CarStatusEnum;
import exception.InputDataValidationException;
import exception.ReservationExistException;
import exception.ReservationRecordNotFoundException;
import exception.UpdateReservationStatusFailException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Uni
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private TransitDriverDispatchSessionBeanLocal transitDriverDispatchSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ReservationSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    public Reservation createReservation(Reservation reservation) throws ReservationExistException, InputDataValidationException {
        Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(reservation);
                long customerId = reservation.getCustomer().getCustomerId();
                customerSessionBean.retrieveCustomer(customerId).getReservationList().add(reservation);

                em.flush();
                return reservation;
            } catch (PersistenceException ex) {
                throw new ReservationExistException();
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<Reservation> retrieveAllMyReservations(long customerId) {
        Query query = em.createQuery("SELECT r from Reservation r where r.customer.customerId = :customerId AND r.cancelled = false");
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
    public void cancelReservation(long reservationId, BigDecimal refundAmount) throws ReservationRecordNotFoundException {
      
            Reservation reservation = retrieveReservation(reservationId);
            reservation.setCancelled(true);
            reservation.setPaid(true); // whether paid before or not it is considered done when cancelled
            reservation.setRefundAmount(refundAmount);
        
    }

    @Override
    public Reservation pickupCar(long reservationId) throws UpdateReservationStatusFailException {

        try {
            Reservation reservation = retrieveReservation(reservationId);

            // set as reserved
            //reservation.getCar().setCarStatus(CarStatusEnum.RESERVED);
            reservation.setPaid(true);
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
    
    
    ////// Car Allocation Manual
    
    
    
    @Override
    public void allocateCars(LocalDateTime dateTime) {
        
        System.out.println("Car Allocation Manual");
        
        LocalDate currDate = dateTime.toLocalDate();
        List<Reservation> currDayReservationList = retrieveReservationByDate(currDate);
        
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
            // requires a car of a specific make and model,
            CarModel carModel = reservation.getCarModel();
            
            // or just any car from a particular category
            CarCategory carCategory = reservation.getCarCategory();
            
            // allocation of cars that are not currently in the particular outlet physically
            Outlet pickUpOutlet = reservation.getDepartureOutlet();
            
            
            // since can be null 
            if (carModel != null) {
                // get all matching cars of this model 
                List<Car> carsOfMatchingMakeAndModel = carSessionBean.retrieveAllCarsByModel(carModel.getCarModelId());
                
                for (Car car : carsOfMatchingMakeAndModel) {
                    
                    // if car is active n unreserved
                    if (car.isEnabled() && !(car.getCarStatus() == CarStatusEnum.RESERVED)) {
                        
                        // prioritise those tt are avail in pickup outlet 
                        if (car.getCarStatus() == CarStatusEnum.AVAILABLE && 
                                car.getCurrentOutlet().getOutletId().equals(reservation.getDepartureOutlet().getOutletId())) {
                            car.setReservation(reservation);
                            car.setCarStatus(CarStatusEnum.RESERVED);
                            reservation.setCar(car);
                            break;
                        } 
                        
                        // those in pickup outlet tt come back on time
                        else if (car.getCarStatus()  == CarStatusEnum.RESERVED) {
                            if (car.getReservation().getReturnTime().isBefore(reservation.getPickupTime()) &&
                                    car.getReservation().getDestinationOutlet().getOutletId().equals(reservation.getDestinationOutlet().getOutletId())) {
                                car.setReservation(reservation);
                                car.setCarStatus(CarStatusEnum.RESERVED);
                                reservation.setCar(car);
                                break;
                            }
                        } 
                        
                        // else if it's not at desired outlet
                        else if (car.getCarStatus()  == CarStatusEnum.AVAILABLE &&
                                !(car.getCurrentOutlet().getOutletId().equals(reservation.getDepartureOutlet().getOutletId()))) {
                            
                            // generate dispatch record
                            transitDriverDispatchSessionBean.createNewDispatchRecord(reservation, new TransitDriverDispatch());
                            
                            car.setReservation(reservation);
                            car.setCarStatus(CarStatusEnum.RESERVED);
                            reservation.setCar(car);
                            break;
                        }
                        
                        // if on rental and car's destination outlet is NOT the reservation's next pickup outlet
                        else if (car.getCarStatus() == CarStatusEnum.RESERVED &&
                                !(car.getReservation().getDestinationOutlet().getOutletId().equals(reservation.getDepartureOutlet().getOutletId()))) {
                            
                            // check if it can make it to pickuptime factoring in 2h transit time
                            if (!(car.getReservation().getReturnTime().isAfter(reservation.getPickupTime().minusHours(2)))) {
                                
                                // generate dispatch record
                                transitDriverDispatchSessionBean.createNewDispatchRecord(reservation, new TransitDriverDispatch());

                                car.setReservation(reservation);
                                car.setCarStatus(CarStatusEnum.RESERVED);
                                reservation.setCar(car);
                                break;
                            }
                        }                    
                    }                
                }
            } 
            
            // car from category
            else {
                List<Car> carsMatchCategory = carSessionBean.retrieveAllCarsByCategory(carCategory.getCarCategoryId());
                
                for (Car car : carsMatchCategory) {
                    
                    // if car is active n unreserved
                    if (car.isEnabled() && !(car.getCarStatus() == CarStatusEnum.RESERVED)) {
                        
                        // prioritise those tt are avail in pickup outlet 
                        if (car.getCarStatus() == CarStatusEnum.AVAILABLE && 
                                car.getCurrentOutlet().getOutletId().equals(reservation.getDepartureOutlet().getOutletId())) {
                            car.setReservation(reservation);
                            car.setCarStatus(CarStatusEnum.RESERVED);
                            reservation.setCar(car);
                            break;
                        }
                        
                        // those in pickup outlet tt come back on time
                        else if (car.getCarStatus()  == CarStatusEnum.RESERVED) {
                            if (car.getReservation().getReturnTime().isBefore(reservation.getPickupTime()) &&
                                    car.getReservation().getDestinationOutlet().getOutletId().equals(reservation.getDestinationOutlet().getOutletId())) {
                                car.setReservation(reservation);
                                car.setCarStatus(CarStatusEnum.RESERVED);
                                reservation.setCar(car);
                                break;
                            }
                        }
                        
                        // else if it's not at desired outlet
                        else if (car.getCarStatus()  == CarStatusEnum.AVAILABLE &&
                                !(car.getCurrentOutlet().getOutletId().equals(reservation.getDepartureOutlet().getOutletId()))) {
                            
                            // generate dispatch record
                            transitDriverDispatchSessionBean.createNewDispatchRecord(reservation, new TransitDriverDispatch());
                            
                            car.setReservation(reservation);
                            car.setCarStatus(CarStatusEnum.RESERVED);
                            reservation.setCar(car);
                            break;
                        }
                        
                        // if on rental and car's destination outlet is NOT the reservation's next pickup outlet
                        else if (car.getCarStatus() == CarStatusEnum.RESERVED &&
                                !(car.getReservation().getDestinationOutlet().getOutletId().equals(reservation.getDepartureOutlet().getOutletId()))) {
                            
                            // check if it can make it to pickuptime factoring in 2h transit time
                            if (!(car.getReservation().getReturnTime().isAfter(reservation.getPickupTime().minusHours(2)))) {
                                
                                // generate dispatch record
                                transitDriverDispatchSessionBean.createNewDispatchRecord(reservation, new TransitDriverDispatch());

                                car.setReservation(reservation);
                                car.setCarStatus(CarStatusEnum.RESERVED);
                                reservation.setCar(car);
                                break;
                            }
                        }                       
                    }
                }
            }
            em.flush();
        }
    }
    

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Reservation>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    

}
