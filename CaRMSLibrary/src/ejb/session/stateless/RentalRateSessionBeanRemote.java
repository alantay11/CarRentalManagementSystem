/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import entity.Reservation;
import exception.InvalidIdException;
import exception.InvalidRentalRateNameException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Uni
 */
@Remote
public interface RentalRateSessionBeanRemote {

    RentalRate createRentalRate(RentalRate rentalRate, long carCategoryId) throws InvalidIdException ;

    List<RentalRate> retrieveAllRentalRates();

    RentalRate retrieveRentalRateUsingName(String rateName) throws InvalidRentalRateNameException;

    RentalRate updateRentalRate(RentalRate rentalRate, long oldCarCategoryId, long newCarCategoryId) throws InvalidIdException;

    RentalRate retrieveRentalRate(long rentalRateId);

    boolean deleteRentalRate(long rentalRateId);

    BigDecimal calculateTotalCost(Reservation reservation);

    List<RentalRate> retrieveApplicableRentalRates(LocalDateTime pickupTime, LocalDateTime returnTime, long carCategoryId);
    
}
