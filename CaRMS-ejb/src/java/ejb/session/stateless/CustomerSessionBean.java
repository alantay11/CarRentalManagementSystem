/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import exception.CustomerNotFoundException;
import exception.InvalidLoginCredentialException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Uni
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException, CustomerNotFoundException {

        Customer customer = retrieveCustomerByUsername(username);

        if (customer.getPassword().equals(password)) {
            //employee.get().size();                
            return customer;
        } else {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    @Override
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = :username");
        query.setParameter("username", username);
        if (query.getResultList().isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Customer customer = (Customer) query.getResultList().get(0);
        return customer;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        em.persist(customer);
        em.flush();
        return customer;
    }

    @Override
    public Customer retrieveCustomer(long customerId) {
        return em.find(Customer.class, customerId);
    }

}
