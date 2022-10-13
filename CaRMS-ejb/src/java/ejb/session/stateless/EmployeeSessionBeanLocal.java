/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import exception.EmployeeNotFoundException;
import javax.ejb.Local;

/**
 *
 * @author Uni
 */
@Local
public interface EmployeeSessionBeanLocal {

    Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    long createEmployee(Employee employee);
    
}
