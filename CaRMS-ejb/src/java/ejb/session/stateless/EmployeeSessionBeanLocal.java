/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import exception.EmployeeNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Uni
 */
@Local
public interface EmployeeSessionBeanLocal {

    Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    Employee createEmployee(Employee employee, long outletId);

    List<Employee> retrieveEmployeesOfOutlet(String outletName);

    Employee retrieveEmployee(long employeeId);
    
}
