/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import exception.CarModelExistException;
import exception.InputDataValidationException;
import java.util.List;
import java.util.Set;
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
 * @author Andrea
 */
@Stateless
public class CarModelSessionBean implements CarModelSessionBeanRemote, CarModelSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CarModelSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    // usecase #14
    @Override
    public CarModel createCarModel(CarModel carModel) throws CarModelExistException, InputDataValidationException {
        Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(carModel);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(carModel);
                em.flush();

                return carModel;
            } catch (PersistenceException ex) {
                throw new CarModelExistException();
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    // usecase #15 sorted in ascending order by carcategory, make and model
    @Override
    public List<CarModel> retrieveAllCarModels() {
        Query query = em.createQuery("SELECT m FROM CarModel m ORDER BY m.carCategory, m.make, m.model");
        List<CarModel> carModels = query.getResultList();

        for (CarModel cm : carModels) {
            cm.getCarList().size();
        }

        return carModels;
    }

    // usecase #16
    @Override
    public CarModel updateCarModel(CarModel carModel) throws InputDataValidationException {
        Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(carModel);

        if (constraintViolations.isEmpty()) {
            em.merge(carModel);
            em.flush();

            return carModel;
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    // usecase #17
    @Override
    public CarModel retrieveCarModel(long carModelId
    ) {
        CarModel carModel = em.find(CarModel.class, carModelId);
        carModel.getCarList().size();
        return carModel;
    }

    @Override
    public boolean deleteCarModel(long carModelId
    ) {
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

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarModel>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
