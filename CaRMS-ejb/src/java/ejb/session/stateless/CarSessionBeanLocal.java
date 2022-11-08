/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import exception.InvalidIdException;
import exception.OutletIsClosedException;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Andrea
 */
@Local
public interface CarSessionBeanLocal {

    public Car createCar(Car car, long carModelId, long outletId);

    public List<Car> retrieveAllCars();

    public Car retrieveCar(long carId) throws InvalidIdException;

    public Car updateCar(Car car);

    public boolean deleteCar(long carId) throws InvalidIdException;

    boolean searchCarByMakeModel(long makeModelId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, long pickupOutletId, long returnOutletId) throws OutletIsClosedException;

    boolean searchCarByCategory(long makeModelId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, long pickupOutletId, long returnOutletId) throws OutletIsClosedException;

}
