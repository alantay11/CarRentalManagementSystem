/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import exception.InvalidIdException;
import exception.InvalidRentalRateNameException;
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
        Query query = em.createQuery("SELECT r FROM RentalRate r ORDER BY r.carCategory, r.startDateTime");
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
}
