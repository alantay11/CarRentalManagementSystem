/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Uni
 */
@Stateful
public class OutletSessionBean implements OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @Override
    public Outlet createOutlet(Outlet outlet) {
        em.persist(outlet);
        em.flush();
        return outlet;
    }

    @Override
    public Outlet retrieveOutlet(long outletId) {
        Outlet outlet = em.find(Outlet.class, outletId);
        outlet.getCarList().size();
        return outlet;

    }

}
