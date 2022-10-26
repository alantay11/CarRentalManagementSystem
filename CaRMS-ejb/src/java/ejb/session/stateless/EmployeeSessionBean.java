/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import exception.InvalidLoginCredentialException;
import exception.EmployeeNotFoundException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Uni
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    

    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Employee employee = retrieveEmployeeByUsername(username);

            if (employee.getPassword().equals(password)) {
                //employee.get().size();                
                return employee;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (EmployeeNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    @Override
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username");
        query.setParameter("username", username);
        if (query.getResultList().isEmpty()) {
            throw new EmployeeNotFoundException();
        }
        Employee employee = (Employee) query.getResultList().get(0);
        return employee;
    }

    @Override
    public Employee createEmployee(Employee employee, long outletId) {
        em.persist(employee);
        
        Outlet outlet = outletSessionBeanLocal.retrieveOutlet(outletId);
        outlet.getEmployeeList().add(employee);
        
        
        em.flush();
        return employee;
    }

}
