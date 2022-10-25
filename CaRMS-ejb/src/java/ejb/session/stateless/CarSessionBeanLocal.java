/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import exception.InvalidIdException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Andrea
 */
@Local
public interface CarSessionBeanLocal {
    
    public Car createCar(Car car);
    
    public List<Car> retrieveAllCars();

    public Car retrieveCar(long carId) throws InvalidIdException;

    public Car updateCar(Car car);

    public boolean deleteCar(long carId) throws InvalidIdException;

}
