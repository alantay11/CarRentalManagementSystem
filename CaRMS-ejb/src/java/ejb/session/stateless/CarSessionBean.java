/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import enumeration.CarStatusEnum;
import exception.InvalidIdException;
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
    private CarModelSessionBeanLocal carModelSessionBeanLocal;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    
    
    // usecase #18
    @Override
    public Car createCar(Car car, long carModelId) {
        CarModel carModel = carModelSessionBeanLocal.retrieveCarModel(carModelId);
        em.persist(car);
        carModel.getCarList().add(car);
        
        CarCategory carCategory = carModel.getCarCategory();
        carCategory.getCarList().add(car);
        em.merge(carCategory);        
        em.merge(carModel);        
        
        em.flush();

        return car;
    }

    // usecase #19: category, make, model and license plate number
    @Override
    public List<Car> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM Car c ORDER BY c.model.carCategory, c.model.make, c.model.model, c.licensePlateNum");
        return query.getResultList();
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
        CarModel carModel = car.getModel();
        List<Car> carList = carModel.getCarList();
        carList.remove(car);
        carModel.setCarList(carList);
        em.merge(carModel);
        
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
}
