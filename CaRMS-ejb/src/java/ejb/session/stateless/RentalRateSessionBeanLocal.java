/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import exception.InvalidIdException;
import javax.ejb.Local;

/**
 *
 * @author Uni
 */
@Local
public interface RentalRateSessionBeanLocal {

    RentalRate createRentalRate(RentalRate rentalRate, long carCategoryId) throws InvalidIdException;

    RentalRate retrieveRentalRate(long rentalRateId);

}
