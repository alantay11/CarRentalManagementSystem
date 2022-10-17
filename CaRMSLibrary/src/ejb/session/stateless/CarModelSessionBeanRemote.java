/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;


import entity.CarModel;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Andrea
 */

@Remote
public interface CarModelSessionBeanRemote {
    
    // create new model
    CarModel createCarModel(CarModel carModel);
    
    // view all models
    List<CarModel> retrieveAllCarModels;
    
    // update model
    
    
    
    // delete model
    
    
}
