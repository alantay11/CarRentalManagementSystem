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
import exception.InvalidLoginCredentialException;
import javax.ejb.Remote;

/**
 *
 * @author Uni
 */
@Remote
public interface CustomerSessionBeanRemote {

    Customer customerLogin(String username, String password) throws InvalidLoginCredentialException, CustomerNotFoundException ;

    Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException ;

    Customer createCustomer(Customer customer) throws CustomerExistException, InputDataValidationException;
    
}
