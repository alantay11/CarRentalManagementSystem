/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import exception.InvalidCarCategoryNameException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Uni
 */
@Remote
public interface CarCategorySessionBeanRemote {

    CarCategory createCarCategory(CarCategory carCategory);

    CarCategory retrieveCarCategoryUsingName(String name) throws InvalidCarCategoryNameException;

    List<CarCategory> retrieveAllCarCategories();
    
}
