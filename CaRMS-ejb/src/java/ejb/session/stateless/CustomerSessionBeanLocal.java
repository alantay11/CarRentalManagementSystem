/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import exception.CustomerExistException;
import exception.CustomerNotFoundException;
import exception.InputDataValidationException;
import javax.ejb.Local;

/**
 *
 * @author Uni
 */
@Local
public interface CustomerSessionBeanLocal {

    Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    Customer retrieveCustomer(long customerId);

    Customer createCustomer(Customer customer) throws CustomerExistException, InputDataValidationException;

}
