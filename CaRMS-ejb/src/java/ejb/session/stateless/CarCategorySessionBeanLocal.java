/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import exception.InvalidCarCategoryNameException;
import javax.ejb.Local;

/**
 *
 * @author Uni
 */
@Local
public interface CarCategorySessionBeanLocal {

    CarCategory createCarCategory(CarCategory carCategory);

    CarCategory retrieveCarCategoryUsingName(String name) throws InvalidCarCategoryNameException;

}
