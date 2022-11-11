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
import enumeration.CarStatusEnum;
import exception.CarExistException;
import exception.InputDataValidationException;
import exception.InvalidIdException;
import exception.OutletIsClosedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
 * @author Andrea
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private CarModelSessionBeanLocal carModelSessionBeanLocal;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CarSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // usecase #18
    @Override
    public Car createCar(Car car, long carModelId, long outletId) throws CarExistException, InputDataValidationException {
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(car);

                // since car belongs to car model, add to carlist within carmodel
                CarModel carModel = carModelSessionBeanLocal.retrieveCarModel(carModelId);
                carModel.getCarList().add(car);

                // since car has to be sorted by category, make sure it is connected
                CarCategory carCategory = carModel.getCarCategory();
                carCategory.getCarList().add(car);

                // since car belongs to outlet, add to carlist within outler
                Outlet outlet = outletSessionBean.retrieveOutlet(outletId);
                outlet.getCarList().add(car);
                //em.merge(carCategory);        
                //em.merge(carModel);        

                em.flush();

                return car;
            } catch (PersistenceException ex) {
                throw new CarExistException();
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }

    }

    // usecase #19: category, make, model and license plate number
    @Override
    public List<Car> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM Car c ORDER BY c.model.carCategory, c.model.make, c.model.model, c.licensePlateNum");

        List<Car> cars = query.getResultList();

        for (Car c : cars) {
            c.getReservationList().size();
        }
        return cars;
    }

    // usecase #20: view details of particular car record
    @Override
    public Car retrieveCar(long carId) throws InvalidIdException {
        Car car = em.find(Car.class, carId);
        car.getReservationList().size();

        return car;
    }

    // usecase #21
    @Override
    public Car updateCar(Car car) throws InputDataValidationException {
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

        if (constraintViolations.isEmpty()) {
            em.merge(car);
            em.flush();

            return car;
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    // usecase #22
    @Override
    public boolean deleteCar(long carId) throws InvalidIdException {
        Car car = retrieveCar(carId);

        // removing connection from carmodel to car
        CarModel carModel = car.getModel();
        List<Car> carList = carModel.getCarList();
        carList.remove(car);
        carModel.setCarList(carList);
        em.merge(carModel);

        // removing connextion from car category to car
        CarCategory carCategory = carModel.getCarCategory();
        carList = carCategory.getCarList();
        carList.remove(car);
        carModel.setCarList(carList);
        em.merge(carCategory);

        if (car.getCarStatus().equals(CarStatusEnum.AVAILABLE)) { // might need to fix this check
            em.remove(car);

            return true;
        } else {
            car.setEnabled(false);
            em.merge(car);

            return false;
        }
    }

    @Override
    public boolean searchCarByMakeModel(long makeModelId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, long pickupOutletId, long returnOutletId) throws OutletIsClosedException {
        Outlet pickupOutlet = outletSessionBean.retrieveOutlet(pickupOutletId);
        Outlet returnOutlet = outletSessionBean.retrieveOutlet(returnOutletId);

        if (pickupOutlet.getOpeningTime() != null) {
            if (pickupDateTime.toLocalTime().isBefore(pickupOutlet.getOpeningTime())) {
                throw new OutletIsClosedException("Your pickup time is before the outlet's opening time");
            }
        }

        if (returnOutlet.getClosingTime() != null) {
            if (returnDateTime.toLocalTime().isAfter(returnOutlet.getClosingTime())) {
                throw new OutletIsClosedException("Your return time is after the outlet's closing time");
            }
        }

        Query query = em.createQuery("SELECT c FROM Car c WHERE c.enabled = TRUE AND c.model.carModelId = :makeModelId");
        query.setParameter("makeModelId", makeModelId);
        int totalAvailableCars = query.getResultList().size();

        Query reservationQuery = em.createQuery("SELECT r FROM Reservation r WHERE r.carModel.carModelId = :makeModelId AND r.cancelled = false"); // all reservations that are for the same makemodel
        reservationQuery.setParameter("makeModelId", makeModelId);

        List<Reservation> reservations = query.getResultList();
        int clashingReservations = 0;

        for (Reservation reservation : reservations) {

            if (reservation.getPickupTime().isBefore(pickupDateTime)) { // starts before the new one
                if (reservation.getReturnTime().isAfter(pickupDateTime)) { // ends after the new one starts
                    clashingReservations++;
                } else if (reservation.getReturnTime().equals(pickupDateTime)) { // returns at the same time the new one starts
                    if (!(reservation.getDestinationOutlet().getOutletId().equals(pickupOutletId))) { // return for old one is at different outlet from new one pickup outlet
                        clashingReservations++;
                    }
                } else if (reservation.getReturnTime().isBefore(pickupDateTime) && reservation.getReturnTime().isAfter(pickupDateTime.minusHours(2))) { // cannot make it for dispatch
                    if (!(reservation.getDestinationOutlet().getOutletId().equals(pickupOutletId))) { // return for old one is at different outlet from new one pickup outlet
                        clashingReservations++;
                    }
                }
            } else if (reservation.getPickupTime().equals(pickupDateTime)) { // both start same time
                clashingReservations++;
            } else if (reservation.getPickupTime().isAfter(pickupDateTime) && reservation.getPickupTime().isBefore(returnDateTime)) { // old one starts after new one and end before new one ends
                clashingReservations++;
            } else if (reservation.getPickupTime().isEqual(returnDateTime)) { // old one starts when new one ends
                if (!(reservation.getDepartureOutlet().getOutletId().equals(returnOutletId))) { // pickup for old one is at different outlet from new one return outlet
                    clashingReservations++;
                }
            } else if (reservation.getPickupTime().isAfter(returnDateTime) && reservation.getPickupTime().isBefore(returnDateTime.plusHours(2))) { // old one starts after new one ends but starts before 2 hours elapsed
                if (!(reservation.getDepartureOutlet().getOutletId().equals(returnOutletId))) { // return for old one is at different outlet from new one pickup outlet, cannot make it for dispatch
                    clashingReservations++;
                }
            }

        }

        return totalAvailableCars > clashingReservations;

        /*
        Query reservationQuery = em.createQuery("SELECT r from Reservation r WHERE (r.cancelled = false AND r.departureOutlet.address = :address) AND (r.returnTime >= :pickupTime OR (r.pickupTime BETWEEN :pickupTime AND :returnTime))");
        reservationQuery.setParameter("pickupTime", pickupDateTime).setParameter("returnTime", returnDateTime).setParameter("address", pickupOutlet.getAddress());
        List<Reservation> clashingReservations = reservationQuery.getResultList();

        System.out.println(clashingReservations);
        System.out.println("numclashes = " + clashingReservations.size() + "numAvailCars = " + totalAvailableCars);
        if (totalAvailableCars > clashingReservations.size()) {
            return true;
        } else {
            return searchForDispatchableCarByMakeModel(makeModelId, pickupDateTime, returnDateTime, pickupOutletId, returnOutletId);
        }*/
    }

    /*
    private boolean searchForDispatchableCarByMakeModel(long makeModelId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, long pickupOutletId, long returnOutletId) {
        Outlet pickupOutlet = outletSessionBean.retrieveOutlet(pickupOutletId);

        Query query = em.createQuery("SELECT c FROM Car c WHERE c.enabled = TRUE AND c.model.carModelId = :makeModelId AND c.currentOutlet.address != :address"); //cars not in this outlet
        query.setParameter("makeModelId", makeModelId).setParameter("address", pickupOutlet.getAddress());
        int totalAvailableCars = query.getResultList().size();

        Query reservationQuery = em.createQuery("SELECT r from Reservation r WHERE (r.cancelled = false AND r.departureOutlet.address != :address) AND (r.returnTime - 2 >= :pickupTime OR (r.pickupTime BETWEEN :pickupTime AND :returnTime))");
        reservationQuery.setParameter("pickupTime", pickupDateTime).setParameter("returnTime", returnDateTime).setParameter("address", pickupOutlet.getAddress());
        List<Reservation> clashingReservations = reservationQuery.getResultList();

        System.out.println(clashingReservations);
        System.out.println("numclashes = " + clashingReservations.size() + "numAvailCars = " + totalAvailableCars);

        return totalAvailableCars > clashingReservations.size();
    }*/
    @Override
    public boolean searchCarByCategory(long categoryId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, long pickupOutletId, long returnOutletId) throws OutletIsClosedException {
        Outlet pickupOutlet = outletSessionBean.retrieveOutlet(pickupOutletId);
        Outlet returnOutlet = outletSessionBean.retrieveOutlet(returnOutletId);

        if (pickupOutlet.getOpeningTime() != null) {
            if (pickupDateTime.toLocalTime().isBefore(pickupOutlet.getOpeningTime())) {
                throw new OutletIsClosedException("Your pickup time is before the outlet's opening time");
            }
        }

        if (returnOutlet.getClosingTime() != null) {
            if (returnDateTime.toLocalTime().isAfter(returnOutlet.getClosingTime())) {
                throw new OutletIsClosedException("Your return time is after the outlet's closing time");
            }
        }

        Query query = em.createQuery("SELECT c FROM Car c WHERE c.enabled = TRUE AND c.model.carCategory.carCategoryId = :categoryId");
        query.setParameter("categoryId", categoryId);
        int totalAvailableCars = query.getResultList().size();

        Query reservationQuery = em.createQuery("SELECT r FROM Reservation r WHERE c.model.carCategory.carCategoryId = :categoryId AND r.cancelled = false"); // all reservations that are for the same category
        reservationQuery.setParameter("categoryId", categoryId);

        List<Reservation> reservations = query.getResultList();
        int clashingReservations = 0;

        for (Reservation reservation : reservations) {

            if (reservation.getPickupTime().isBefore(pickupDateTime)) { // starts before the new one
                if (reservation.getReturnTime().isAfter(pickupDateTime)) { // ends after the new one starts
                    clashingReservations++;
                } else if (reservation.getReturnTime().equals(pickupDateTime)) { // returns at the same time the new one starts
                    if (!(reservation.getDestinationOutlet().getOutletId().equals(pickupOutletId))) { // return for old one is at different outlet from new one pickup outlet
                        clashingReservations++;
                    }
                } else if (reservation.getReturnTime().isBefore(pickupDateTime) && reservation.getReturnTime().isAfter(pickupDateTime.minusHours(2))) { // cannot make it for dispatch
                    if (!(reservation.getDestinationOutlet().getOutletId().equals(pickupOutletId))) { // return for old one is at different outlet from new one pickup outlet
                        clashingReservations++;
                    }
                }
            } else if (reservation.getPickupTime().equals(pickupDateTime)) { // both start same time
                clashingReservations++;
            } else if (reservation.getPickupTime().isAfter(pickupDateTime) && reservation.getPickupTime().isBefore(returnDateTime)) { // old one starts after new one and end before new one ends
                clashingReservations++;
            } else if (reservation.getPickupTime().isEqual(returnDateTime)) { // old one starts when new one ends
                if (!(reservation.getDepartureOutlet().getOutletId().equals(returnOutletId))) { // pickup for old one is at different outlet from new one return outlet
                    clashingReservations++;
                }
            } else if (reservation.getPickupTime().isAfter(returnDateTime) && reservation.getPickupTime().isBefore(returnDateTime.plusHours(2))) { // old one starts after new one ends but starts before 2 hours elapsed
                if (!(reservation.getDepartureOutlet().getOutletId().equals(returnOutletId))) { // return for old one is at different outlet from new one pickup outlet, cannot make it for dispatch
                    clashingReservations++;
                }
            }

        }

        return totalAvailableCars > clashingReservations;

    }

    @Override
    public List<Car> retrieveAllCarsByModel(Long carModelId) {
        List<Car> allCars = retrieveAllCars();

        List<Car> carsWithThisModel = new ArrayList<>();
        for (Car car : allCars) {
            if (car.getModel().getCarModelId().equals(carModelId)) {
                carsWithThisModel.add(car);
            }
        }
        return carsWithThisModel;
    }

    @Override
    public List<Car> retrieveAllCarsByCategory(Long carCategoryId) {
        List<Car> allCars = retrieveAllCars();

        List<Car> carsWithThisCategory = new ArrayList<>();
        for (Car car : allCars) {
            if (car.getModel().getCarCategory().getCarCategoryId().equals(carCategoryId)) {
                carsWithThisCategory.add(car);
            }
        }
        return carsWithThisCategory;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Car>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}

/*
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.enabled = TRUE AND c.model.carCategory.carCategoryId = :categoryId");
        query.setParameter("categoryId", categoryId);
        int totalAvailableCars = query.getResultList().size();

        Query reservationQuery = em.createQuery("SELECT r from Reservation r WHERE r.cancelled = false AND (r.returnTime >= :pickupTime OR (r.pickupTime BETWEEN :pickupTime AND :returnTime))");
        reservationQuery.setParameter("pickupTime", pickupDateTime).setParameter("returnTime", returnDateTime);
        List<Reservation> clashingReservations = reservationQuery.getResultList();

        System.out.println(clashingReservations);
        System.out.println("numclashes = " + clashingReservations.size() + "numAvailCars = " + totalAvailableCars);

        if (totalAvailableCars > clashingReservations.size()) {
            return true;
        } else {
            return searchForDispatchableCarByCategory(categoryId, pickupDateTime, returnDateTime, pickupOutletId, returnOutletId);
        }
    }

    private boolean searchForDispatchableCarByCategory(long categoryId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, long pickupOutletId, long returnOutletId) {
        Outlet pickupOutlet = outletSessionBean.retrieveOutlet(pickupOutletId);

        Query query = em.createQuery("SELECT c FROM Car c WHERE c.enabled = TRUE AND c.model.carCategory.carCategoryId = :categoryId AND c.currentOutlet.address != :address");
        query.setParameter("categoryId", categoryId).setParameter("address", pickupOutlet.getAddress());
        int totalAvailableCars = query.getResultList().size();

        Query reservationQuery = em.createQuery("SELECT r from Reservation r WHERE (r.cancelled = false AND r.departureOutlet.address != :address) AND (r.returnTime - 2 >= :pickupTime OR (r.pickupTime BETWEEN :pickupTime AND :returnTime))");
        reservationQuery.setParameter("pickupTime", pickupDateTime).setParameter("returnTime", returnDateTime).setParameter("address", pickupOutlet.getAddress());
        List<Reservation> clashingReservations = reservationQuery.getResultList();

        System.out.println(clashingReservations);
        System.out.println("numclashes = " + clashingReservations.size() + "numAvailCars = " + totalAvailableCars);

        return totalAvailableCars > clashingReservations.size();
    }
}*/
