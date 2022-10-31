/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import exception.InvalidLoginCredentialException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Uni
 */
@Local
public interface PartnerSessionBeanLocal {

    Partner createPartner(Partner partner);

    Partner retrievePartner(long partnerId);

    List<Partner> retrieveAllPartners();
    
    Partner partnerLogin(String username, String password) throws InvalidLoginCredentialException;

    Partner retrievePartnerByUsername(String username);

}
