/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Employee;
import entity.Outlet;
import enumeration.CarStatusEnum;
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

    @EJB
    private CarModelSessionBeanLocal carModelSessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB
    private CarSessionBeanLocal carSessionBean;
    @EJB
    private OutletSessionBeanLocal outletSessionBean;
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
            standardSedanCategory = carCategorySessionBean.createCarCategory(standardSedanCategory);
            carCategorySessionBean.createCarCategory(familySedanCategory);
            carCategorySessionBean.createCarCategory(luxurySedanCategory);
            carCategorySessionBean.createCarCategory(suvMinivanCategory);
        }

        List<Car> sampleCarList = new ArrayList<>();
        Outlet sampleOutlet = new Outlet();
        sampleOutlet.setAddress("NUS");
        sampleOutlet.setOpeningTime(LocalTime.parse("09:00"));
        sampleOutlet.setClosingTime(LocalTime.parse("17:00"));
        
        if (em.find(Outlet.class, 1l) == null) {
            outletSessionBean.createOutlet(sampleOutlet);
        }
        
        CarModel sampleCarModel = new CarModel();        
        sampleCarModel.setCarCategory(standardSedanCategory);
        sampleCarModel.setMake("Ferrari");
        sampleCarModel.setModel("FML");
        
        if (em.find(CarModel.class, 1l) == null) {
            carModelSessionBean.createCarModel(sampleCarModel);
        }
        
        
        Car sampleCar = new Car();
        sampleCar.setModel(sampleCarModel);
        sampleCar.setCarStatus(CarStatusEnum.ONRENTAL);
        sampleCar.setColor("DEATH");
        sampleCar.setCurrentOutlet(sampleOutlet);
        sampleCar.setLicensePlateNum("999999");
        
        if (em.find(Car.class, 1l) == null) {
            carSessionBean.createCar(sampleCar, sampleCarModel.getCarModelId(), sampleOutlet.getOutletId());
        }
        
        sampleCarList.add(sampleCar);                
        sampleOutlet.setCarList(sampleCarList);
        
        

        List<Employee> employees = new ArrayList<>();
        Employee systemAdmin = new Employee("system", "manager", "sys", "password", EmployeeAccessRightEnum.SYSTEMADMINISTRATOR, sampleOutlet);
        Employee salesManager = new Employee("sales", "manager", "sales", "password", EmployeeAccessRightEnum.SALESMANAGER, sampleOutlet);
        Employee operationsManager = new Employee("ops", "manager", "ops", "password", EmployeeAccessRightEnum.OPERATIONSMANAGER, sampleOutlet);
        Employee customerServiceExec = new Employee("cse", "manager", "cse", "password", EmployeeAccessRightEnum.CUSTOMERSERVICEEXECUTIVE, sampleOutlet);
        employees.add(systemAdmin);
        employees.add(salesManager);
        employees.add(operationsManager);
        employees.add(customerServiceExec);
        
        sampleOutlet.setEmployeeList(employees);
        
        if (em.find(Outlet.class, 1l) == null) {
            outletSessionBean.createOutlet(sampleOutlet);
        }
        
        if (em.find(Employee.class, 1l) == null) {
            employeeSessionBeanLocal.createEmployee(salesManager);
            employeeSessionBeanLocal.createEmployee(operationsManager);
            employeeSessionBeanLocal.createEmployee(customerServiceExec);
            employeeSessionBeanLocal.createEmployee(systemAdmin);
        }
    }
}
