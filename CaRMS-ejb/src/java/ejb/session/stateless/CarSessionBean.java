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
import exception.InvalidIdException;
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
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private CarModelSessionBeanLocal carModelSessionBeanLocal;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // usecase #18
    @Override
    public Car createCar(Car car, long carModelId, long outletId) {
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
    public Car updateCar(Car car) {
        em.merge(car);
        em.flush();

        return car;
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

        if (car.getCarStatus().equals(CarStatusEnum.INOUTLET)) {
            em.remove(car);

            return true;
        } else {
            car.setEnabled(false);
            em.merge(car);

            return false;
        }
    }

    @Override
    public boolean searchCarByMakeModel(long makeModelId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, long pickupOutletId, long returnOutletId) {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.enabled = TRUE AND c.model.carModelId = :makeModeId");
        query.setParameter("makeModeId", makeModelId);
        int totalAvailableCars = query.getResultList().size();
        
        
        Query reservationQuery = em.createQuery("SELECT r from Reservation r WHERE r.returnTime >= :pickupTime OR r.pickupTime BETWEEN :pickupTime AND :returnTime");
        reservationQuery.setParameter("pickupTime", pickupDateTime).setParameter("returnTime", returnDateTime);
        List<Reservation> clashingReservations = reservationQuery.getResultList();

        return totalAvailableCars > clashingReservations.size();
    }

    @Override
    public boolean searchCarByCategory(long categoryId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, long pickupOutletId, long returnOutletId) {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.enabled = TRUE AND c.model.carCategory = :categoryId");
        query.setParameter("categoryId", categoryId);
        int totalAvailableCars = query.getResultList().size();
        
        
        Query reservationQuery = em.createQuery("SELECT r from Reservation r WHERE r.returnTime >= :pickupTime OR r.pickupTime BETWEEN :pickupTime AND :returnTime");
        reservationQuery.setParameter("pickupTime", pickupDateTime).setParameter("returnTime", returnDateTime);
        List<Reservation> clashingReservations = reservationQuery.getResultList();

        return totalAvailableCars > clashingReservations.size();
    }
}
