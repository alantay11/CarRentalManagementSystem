/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import exception.InvalidLoginCredentialException;
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
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public Partner createPartner(Partner partner) {
        em.persist(partner);
        em.flush();
        return partner;
    }

    @Override
    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialException {

        Partner partner = retrievePartnerByUsername(username);

        if (partner.getPassword().equals(password)) {
            return partner;
        } else {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    @Override
    public Partner retrievePartnerByUsername(String username) {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :username");
        query.setParameter("username", username);
        if (query.getResultList().isEmpty()) {
            //throw new CustomerNotFoundException();
        }
        Partner partner = (Partner) query.getResultList().get(0);
        return partner;
    }

    @Override
    public Partner retrievePartner(long partnerId) {
        Partner partner = em.find(Partner.class, partnerId);
        partner.getCustomerList().size();
        return partner;
    }

    @Override
    public List<Partner> retrieveAllPartners() {
        Query query = em.createQuery("SELECT p FROM Partner p");

        List<Partner> partners = query.getResultList();

        for (Partner p : partners) {
            p.getCustomerList().size();
        }
        return partners;
    }

}
