/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import exception.InvalidLoginCredentialException;
import exception.EmployeeNotFoundException;
import javax.ejb.Remote;

/**
 *
 * @author Uni
 */
@Remote
public interface EmployeeSessionBeanRemote {

    Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    long createEmployee(Employee employee);
    
}
