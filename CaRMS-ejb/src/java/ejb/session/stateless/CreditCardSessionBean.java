/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import entity.Customer;
import entity.Reservation;
import exception.CreditCardExistException;
import exception.InputDataValidationException;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Uni
 */
@Stateless
public class CreditCardSessionBean implements CreditCardSessionBeanRemote, CreditCardSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CreditCardSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    public CreditCard createCreditCard(CreditCard creditCard, long customerId) throws CreditCardExistException, InputDataValidationException {
        Set<ConstraintViolation<CreditCard>> constraintViolations = validator.validate(creditCard);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(creditCard);
                Customer customer = customerSessionBeanLocal.retrieveCustomer(customerId);
                customer.setCreditCard(creditCard);

                em.flush();
                return creditCard;
            } catch (PersistenceException ex) {
                throw new CreditCardExistException();
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CreditCard>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
