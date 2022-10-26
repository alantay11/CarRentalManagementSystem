/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import javax.ejb.Local;

/**
 *
 * @author Uni
 */
@Local
public interface OutletSessionBeanLocal {

    public Outlet createOutlet(Outlet outlet);

    public Outlet retrieveOutlet(long outletId);
    
}
