/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import exception.CarExistException;
import exception.InputDataValidationException;
import exception.InvalidIdException;
import exception.OutletIsClosedException;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Andrea
 */
@Remote
public interface CarSessionBeanRemote {

    public Car createCar(Car car, long carModelId, long outletId) throws CarExistException, InputDataValidationException;

    public List<Car> retrieveAllCars();

    public Car retrieveCar(long carId) throws InvalidIdException;

    public Car updateCar(Car car) throws InputDataValidationException;

    public boolean deleteCar(long carId) throws InvalidIdException;

    boolean searchCarByMakeModel(long makeModelId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, long pickupOutletId, long returnOutletId) throws OutletIsClosedException;

    boolean searchCarByCategory(long makeModelId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, long pickupOutletId, long returnOutletId) throws OutletIsClosedException;

    public List<Car> retrieveAllCarsByModel(Long carModelId);
    
    public List<Car> retrieveAllCarsByCategory(Long carCategoryId);

    Car retrieveCarByLicensePlate(String plateNum) throws InvalidIdException;

    boolean deleteCarByLicensePlate(String plateNum) throws InvalidIdException;

    boolean searchCar(LocalDateTime pickupDateTime, LocalDateTime returnDateTime, long pickupOutletId, long returnOutletId) throws OutletIsClosedException;


}
