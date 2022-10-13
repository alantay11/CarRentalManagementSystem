/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.Employee;
import entity.Outlet;
import enumeration.EmployeeAccessRightEnum;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Uni
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
        CarCategory sampleCarCategory = new CarCategory("Category");
        List<Car> sampleCarList = new ArrayList<>();
        Outlet sampleOutlet = new Outlet("address", LocalTime.parse("09:00"), LocalTime.parse("17:00"), sampleCarList);
        Car sampleCar = new Car("Make", "Model", "Color", sampleCarCategory, sampleOutlet);
        
        sampleCarList.add(sampleCar);
        
        Employee sampleSalesManager = new Employee("sales", "manager", "sales", "password", EmployeeAccessRightEnum.SALESMANAGER);//, sampleOutlet);

        if (em.find(Employee.class, 1l) == null) {
            employeeSessionBeanLocal.createEmployee(sampleSalesManager);
        }
    }
}
