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
import exception.InvalidCarCategoryNameException;
import exception.InvalidIdException;
import exception.InvalidRentalRateNameException;
import exception.NoRentalRateAvailableException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

    @Override
    public RentalRate createRentalRate(RentalRate rentalRate, long carCategoryId) throws InvalidIdException {
        em.persist(rentalRate);

        CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategory(carCategoryId);
        carCategory.getRentalRateList().add(rentalRate);

        //em.merge(carCategory);
        em.flush();

        return rentalRate;
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
    public RentalRate updateRentalRate(RentalRate rentalRate, long oldCarCategoryId, long newCarCategoryId) throws InvalidIdException {
        CarCategory oldCarCategory = carCategorySessionBeanLocal.retrieveCarCategory(oldCarCategoryId);
        oldCarCategory.getRentalRateList().remove(rentalRate);

        CarCategory newCarCategory = carCategorySessionBeanLocal.retrieveCarCategory(newCarCategoryId);
        newCarCategory.getRentalRateList().add(rentalRate);

        em.merge(rentalRate);
        em.flush();

        return rentalRate;
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
            em.remove(rentalRateId);
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
    public List<RentalRate> retrieveApplicableRentalRates(LocalDateTime pickupTime, LocalDateTime returnTime, long carCategoryId) throws NoRentalRateAvailableException {
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
        Query query = em.createQuery("SELECT r FROM RentalRate r WHERE r.carCategory.carCategoryId = :carCategoryId AND r.endDate >= :time ORDER BY r.ratePerDay");
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
}
