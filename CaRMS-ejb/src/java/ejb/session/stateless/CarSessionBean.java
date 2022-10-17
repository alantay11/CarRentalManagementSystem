/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
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

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    // usecase #18
    @Override
    public Car createCar(Car car)
    {
        em.persist(car);
        em.flush();
        
        return car;
    }
    
    // usecase #19: category, make, model and license plate number
    @Override
    public List<Car> retrieveAllCars()
    {
        Query query = em.createQuery("SELECT c FROM Car c ORDER BY c.category, c.model, c.licensePlateNum");
        return query.getResultList();
    }
    
    // usecase #20: view details of particular car record
    @Override
    public Car retrieveCar(long carId)
    {
        Car car = em.find(Car.class, carId);
        car.getCarList().size();
       
        return car;
    }
    
    // usecase #21
    @Override
    public Car updateCar(Car car)
    {
        em.merge(car);
        em.flush();
        
        return car;
    }
    
    // usecase #22
    @Override
    public boolean deleteCar(long carId)
    {
        Car car = retrieveCar(carId);
        
        if(car.getCarList().isEmpty()) 
        {
            em.remove(car);
            
            return true;
        }
        else
        {
            car.setEnabled(false);
            em.merge(car);
            
            return false;
        }
    }
}
