/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import exception.CreditCardExistException;
import exception.InputDataValidationException;
import javax.ejb.Local;

/**
 *
 * @author Uni
 */
@Local
public interface CreditCardSessionBeanLocal {

    CreditCard createCreditCard(CreditCard creditCard, long customerId) throws CreditCardExistException, InputDataValidationException;

}
