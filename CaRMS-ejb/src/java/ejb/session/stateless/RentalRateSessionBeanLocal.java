/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import entity.Reservation;
import exception.InputDataValidationException;
import exception.InvalidIdException;
import exception.NoRentalRateAvailableException;
import exception.RentalRateExistException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.ejb.Local;

/**
 *
 * @author Uni
 */
@Local
public interface RentalRateSessionBeanLocal {

    RentalRate createRentalRate(RentalRate rentalRate, long carCategoryId) throws InvalidIdException, RentalRateExistException, InputDataValidationException;

    RentalRate retrieveRentalRate(long rentalRateId);

    BigDecimal calculateTotalCost(Reservation reservation) throws NoRentalRateAvailableException;

    BigDecimal calculateTotalCostWeb(LocalDateTime pickupTime, LocalDateTime returnTime, long carCategoryId) throws NoRentalRateAvailableException ;

}
