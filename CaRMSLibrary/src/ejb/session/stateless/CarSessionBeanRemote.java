/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Andrea
 */
@Remote
public interface CarSessionBeanRemote {

    public Car createCar(Car car);

    public List<Car> retrieveAllCars();
    
    public Car retrieveCar(long carId);

    public Car updateCar(Car car);
   
    public boolean deleteCar(long carId);

}
