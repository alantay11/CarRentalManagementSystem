/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
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
    private CarCategorySessionBeanLocal carCategorySessionBean;
    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

   
    @PostConstruct
    public void postConstruct() {
        CarCategory standardSedanCategory = new CarCategory("Standard Sedan");
        CarCategory familySedanCategory = new CarCategory("Family Sedan");
        CarCategory luxurySedanCategory = new CarCategory("Luxury Sedan");
        CarCategory suvMinivanCategory = new CarCategory("SUV/Minivan");

        if (em.find(CarCategory.class, 1l) == null) {
            carCategorySessionBean.createCarCategory(standardSedanCategory);
            carCategorySessionBean.createCarCategory(familySedanCategory);
            carCategorySessionBean.createCarCategory(luxurySedanCategory);
            carCategorySessionBean.createCarCategory(suvMinivanCategory);
        }

        List<Car> sampleCarList = new ArrayList<>();
        //Outlet sampleOutlet = new Outlet("address", LocalTime.parse("09:00"), LocalTime.parse("17:00"), sampleCarList);
        //Car sampleCar = new Car("Make", "Model", "Color", standardSedanCategory, sampleOutlet);

        //sampleCarList.add(sampleCar);

        Employee systemAdmin = new Employee("system", "manager", "sys", "password", EmployeeAccessRightEnum.SYSTEMADMINISTRATOR);
        Employee salesManager = new Employee("sales", "manager", "sales", "password", EmployeeAccessRightEnum.SALESMANAGER);//, sampleOutlet);
        Employee operationsManager = new Employee("ops", "manager", "ops", "password", EmployeeAccessRightEnum.OPERATIONSMANAGER);
        Employee customerServiceExec = new Employee("cse", "manager", "cse", "password", EmployeeAccessRightEnum.CUSTOMERSERVICEEXECUTIVE);
        if (em.find(Employee.class, 1l) == null) {
            employeeSessionBeanLocal.createEmployee(salesManager);
            employeeSessionBeanLocal.createEmployee(operationsManager);
            employeeSessionBeanLocal.createEmployee(customerServiceExec);
            employeeSessionBeanLocal.createEmployee(systemAdmin);
        }
    }
}
