/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import entity.Reservation;
import enumeration.RentalRateEnum;
import exception.InputDataValidationException;
import exception.InvalidCarCategoryNameException;
import exception.InvalidIdException;
import exception.InvalidRentalRateNameException;
import exception.NoRentalRateAvailableException;
import exception.RentalRateExistException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Uni
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RentalRateSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    public RentalRate createRentalRate(RentalRate rentalRate, long carCategoryId) throws InvalidIdException, RentalRateExistException, InputDataValidationException {
        Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(rentalRate);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(rentalRate);

                CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategory(carCategoryId);
                carCategory.getRentalRateList().add(rentalRate);

                //em.merge(carCategory);
                em.flush();

                return rentalRate;
            } catch (PersistenceException ex) {
                throw new RentalRateExistException();
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<RentalRate> retrieveAllRentalRates() {
        Query query = em.createQuery("SELECT r FROM RentalRate r ORDER BY r.carCategory, r.startDate");
        List<RentalRate> rentalRates = query.getResultList();

        for (RentalRate r : rentalRates) {
            r.getReservationList().size();
        }
        return rentalRates;
    }

    @Override
    public RentalRate retrieveRentalRateUsingName(String rateName) throws InvalidRentalRateNameException {
        Query query = em.createQuery("SELECT r FROM RentalRate r WHERE r.rateName = :name");
        query.setParameter("name", rateName);
        if (query.getResultList().isEmpty()) {
            throw new InvalidRentalRateNameException();
        }
        RentalRate rentalRate = (RentalRate) query.getResultList().get(0);
        return rentalRate;
    }

    @Override
    public RentalRate updateRentalRate(RentalRate rentalRate, long oldCarCategoryId, long newCarCategoryId) throws InvalidIdException, InputDataValidationException {
        Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(rentalRate);

        if (constraintViolations.isEmpty()) {
            CarCategory oldCarCategory = carCategorySessionBeanLocal.retrieveCarCategory(oldCarCategoryId);
            oldCarCategory.getRentalRateList().remove(rentalRate);

            CarCategory newCarCategory = carCategorySessionBeanLocal.retrieveCarCategory(newCarCategoryId);
            newCarCategory.getRentalRateList().add(rentalRate);

            em.merge(rentalRate);
            em.flush();

            return rentalRate;

        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public RentalRate retrieveRentalRate(long rentalRateId) {
        RentalRate rentalRate = em.find(RentalRate.class, rentalRateId);
        rentalRate.getReservationList().size();
        return rentalRate;
    }

    @Override
    public boolean deleteRentalRate(long rentalRateId) {
        RentalRate rentalRate = retrieveRentalRate(rentalRateId);
        CarCategory carCategory = rentalRate.getCarCategory();
        List<RentalRate> rentalRateList = carCategory.getRentalRateList();
        rentalRateList.remove(rentalRate);
        carCategory.setRentalRateList(rentalRateList);

        //em.merge(carCategory);
        if (rentalRate.getReservationList().isEmpty()) {
            em.remove(rentalRate);
            return true;
        } else {
            rentalRate.setEnabled(false);
            //em.merge(rentalRate);
            return false;
        }
    }

    @Override
    public BigDecimal calculateTotalCost(Reservation reservation) throws NoRentalRateAvailableException {
        LocalDateTime pickupTime = reservation.getPickupTime();
        LocalDateTime returnTime = reservation.getReturnTime();
        CarCategory carCategory = reservation.getCarCategory();
        long carCategoryId = 0;
        try {
            carCategoryId = carCategorySessionBeanLocal.retrieveCarCategoryUsingName(carCategory.getCarCategoryName()).getCarCategoryId();
        } catch (InvalidCarCategoryNameException ex) {
            System.out.println("Somehow you got an invalid car category");
        }

        List<RentalRate> bestRates = retrieveApplicableRentalRates(pickupTime, returnTime, carCategoryId);
        System.out.println("bestRates = " + bestRates);

        BigDecimal total = new BigDecimal("0.00");
        for (RentalRate rate : bestRates) {
            total = total.add(rate.getRatePerDay());
            System.out.println("total =" + total + " rate perday = " + rate.getRatePerDay());
        }

        return total;
    }

    @Override
    public List<RentalRate> retrieveApplicableRentalRates(LocalDateTime pickupTime, LocalDateTime returnTime,
            long carCategoryId) throws NoRentalRateAvailableException {
        Duration duration = Duration.between(pickupTime, returnTime);
        System.out.println("pickup = " + pickupTime + " return = " + returnTime);
        System.out.println("duration = " + duration);

        long hours = duration.toHours();
        long days = Math.round(Math.ceil(hours / 24.0));
        System.out.println("duration in days = " + duration.toDays());
        System.out.println("days = " + days);
        List<RentalRate> bestRentalRates = new ArrayList<>();
        LocalDateTime time = pickupTime;

        for (int i = 0; i < days; i++) {
            bestRentalRates.add(findBestRentalRateFor24Hours(time, carCategoryId));
            time = time.plusDays(1);
        }

        // get cheapest rates for the reservation somehow
        // only 1 rate applies per day
        // find cheapest for the first 24h
        // and continue
        System.out.println("bestRentalRates = " + bestRentalRates);
        return bestRentalRates;
    }

    private RentalRate findBestRentalRateFor24Hours(LocalDateTime time, long carCategoryId) throws NoRentalRateAvailableException {
        Query query = em.createQuery("SELECT r FROM RentalRate r WHERE r.carCategory.carCategoryId = :carCategoryId AND (r.endDate >= :time OR r.endDate = null) ORDER BY r.ratePerDay");
        query.setParameter("carCategoryId", carCategoryId).setParameter("time", time);
        System.out.println(query.getResultList());//

        List<RentalRate> bestRates = query.getResultList();
        if (bestRates.isEmpty()) {
            throw new NoRentalRateAvailableException();
        }

        boolean promoAvailable = false;
        boolean peakForced = false;

        for (RentalRate rate : bestRates) { // checking what kinds of rates are available
            if (rate.getRateType().equals(RentalRateEnum.Peak)) {
                peakForced = true;
            }
            if (rate.getRateType().equals(RentalRateEnum.Promotion)) {
                promoAvailable = true;
            }
        }

        if (peakForced && !promoAvailable) { // no promo but got peak
            for (RentalRate rate : bestRates) {
                if (rate.getRateType().equals(RentalRateEnum.Peak)) {
                    return rate;
                }
            }
        }

        if (promoAvailable || (promoAvailable && peakForced)) { // if promo it overrides everything
            for (RentalRate rate : bestRates) {
                if (rate.getRateType().equals(RentalRateEnum.Promotion)) {
                    return rate;
                }
            }
        }

        // only default rate available
        return bestRates.get(0);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRate>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
