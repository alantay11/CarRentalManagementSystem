/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Customer;
import entity.Partner;
import exception.InvalidLoginCredentialException;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Uni
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @WebMethod(operationName = "partnerLogin")
    public Partner partnerLogin(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        Partner partner = partnerSessionBeanLocal.partnerLogin(username, password);
        
        em.detach(partner);
        for (Customer c : partner.getCustomerList()) {
            em.detach(c);
            c.setPartner(null);
        }
        
        //partner.setCustomerList(null); don't need?
        
        
        return partner;
    }

}
