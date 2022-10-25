/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Andrea
 */
@Stateless
public class CarModelSessionBean implements CarModelSessionBeanRemote, CarModelSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    // usecase #14
    @Override
    public CarModel createCarModel(CarModel carModel) {
        em.persist(carModel);
        em.flush();

        return carModel;
    }

    // usecase #15 sorted in ascending order by carcategory, make and model
    @Override
    public List<CarModel> retrieveAllCarModels() {
        Query query = em.createQuery("SELECT m FROM CarModel m ORDER BY m.carCategory, m.make, m.model");
        return query.getResultList();
    }

    // usecase #16
    @Override
    public CarModel updateCarModel(CarModel carModel) {
        em.merge(carModel);
        em.flush();

        return carModel;
    }

    // usecase #17
    @Override
    public CarModel retrieveCarModel(long carModelId) {
        CarModel carModel = em.find(CarModel.class, carModelId);
        carModel.getCarList().size();
        return carModel;
    }

    @Override
    public boolean deleteCarModel(long carModelId) {
        CarModel carModel = retrieveCarModel(carModelId);

        if (carModel.getCarList().isEmpty()) {
            em.remove(carModel);

            return true;
        } else {
            carModel.setEnabled(false);
            em.merge(carModel);

            return false;
        }
    }

}
