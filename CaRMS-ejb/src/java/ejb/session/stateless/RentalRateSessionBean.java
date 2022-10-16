/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import exception.InvalidRentalRateNameException;
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
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public RentalRate createRentalRate(RentalRate rentalRate) {
        em.persist(rentalRate);
        em.flush();

        return rentalRate;
    }

    @Override
    public List<RentalRate> retrieveAllRentalRates() {
        Query query = em.createQuery("SELECT r FROM RentalRate r ORDER BY r.carCategory, r.startDateTime");
        return query.getResultList();
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
    public RentalRate updateRentalRate(RentalRate rentalRate) {
        em.merge(rentalRate);
        em.flush();

        return rentalRate;
    }

    @Override
    public RentalRate retrieveRentalRate(long rentalRateId) {
        return em.find(RentalRate.class, rentalRateId);
    }

    @Override
    public boolean deleteRentalRate(long rentalRateId) {
        RentalRate rentalRate = retrieveRentalRate(rentalRateId);
        if (rentalRate.getCarCategory() == null) {
            em.remove(rentalRateId);
            return true;
        } else {
            rentalRate.setEnabled(false);
            em.merge(rentalRate);
            return false;
        }
    }
}
