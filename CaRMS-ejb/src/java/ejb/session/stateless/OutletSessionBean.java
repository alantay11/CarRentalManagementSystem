/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Uni
 */
@Stateful
public class OutletSessionBean implements OutletSessionBeanLocal, OutletSessionBeanRemote {

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

    @Override
    public List<Outlet> retrieveAllOutlets() {
        Query query = em.createQuery("SELECT o FROM Outlet o");

        List<Outlet> outlets = query.getResultList();

        for (Outlet o : outlets) {
            o.getCarList().size();
            o.getEmployeeList().size();
            o.getInboundTransitDriverDispatchList().size();
        }
        return outlets;
    }
    
    
    
    

}
