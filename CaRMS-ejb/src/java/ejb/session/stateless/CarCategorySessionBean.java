/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import exception.InvalidCarCategoryNameException;
import exception.InvalidIdException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Uni
 */
@Stateless
public class CarCategorySessionBean implements CarCategorySessionBeanRemote, CarCategorySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public CarCategory createCarCategory(CarCategory carCategory) {
        em.persist(carCategory);
        em.flush();
        return carCategory;
    }

    @Override
    public List<CarCategory> retrieveAllCarCategories() {
        Query query = em.createQuery("SELECT c FROM CarCategory c");
        return query.getResultList();
    }

    @Override
    public CarCategory retrieveCarCategoryUsingName(String name) throws InvalidCarCategoryNameException {
        Query query = em.createQuery("SELECT c FROM CarCategory c WHERE c.carCategoryName = :name");
        query.setParameter("name", name);
        if (query.getResultList().isEmpty()) {
            throw new InvalidCarCategoryNameException();
        }
        CarCategory carCategory = (CarCategory) query.getResultList().get(0);
        return carCategory;
    }

    @Override
    public CarCategory retrieveCarCategory(long carCategoryId) throws InvalidIdException {
        CarCategory carCategory = em.find(CarCategory.class, carCategoryId);
        carCategory.getRentalRateList().size();
        return carCategory;
    }

}
