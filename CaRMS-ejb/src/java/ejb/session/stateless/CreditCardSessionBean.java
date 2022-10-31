/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import entity.Customer;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    @Override
    public CreditCard createCreditCard(CreditCard creditCard, long customerId) {
        em.persist(creditCard);
        Customer customer = customerSessionBeanLocal.retrieveCustomer(customerId);
        customer.setCreditCard(creditCard);

        em.flush();
        return creditCard;
    }

}
