/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Uni
 */
@Remote
public interface PartnerSessionBeanRemote {

    Partner createPartner(Partner partner);

    Partner retrievePartner(long partnerId);

    List<Partner> retrieveAllPartners();
    
}
