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
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Employee;
import entity.Outlet;
import entity.Partner;
import enumeration.CarStatusEnum;
import enumeration.EmployeeAccessRightEnum;
import java.time.LocalTime;
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
    private PartnerSessionBeanLocal partnerSessionBean;

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

        Outlet sampleOutlet = new Outlet();
        sampleOutlet.setAddress("Outlet1");
        sampleOutlet.setOpeningTime(LocalTime.parse("09:00"));
        sampleOutlet.setClosingTime(LocalTime.parse("17:00"));

        Outlet sampleOutlet2 = new Outlet();
        sampleOutlet2.setAddress("Outlet2");
        sampleOutlet2.setOpeningTime(LocalTime.parse("08:01"));
        sampleOutlet2.setClosingTime(LocalTime.parse("20:01"));

        if (em.find(Outlet.class, 1l) == null) {
            outletSessionBean.createOutlet(sampleOutlet);
            outletSessionBean.createOutlet(sampleOutlet2);
        }

        CarModel sampleCarModelSedan = new CarModel();
        sampleCarModelSedan.setCarCategory(standardSedanCategory);
        sampleCarModelSedan.setMake("SedanMake1");
        sampleCarModelSedan.setModel("SM1");

        CarModel sampleCarModelSedan2 = new CarModel();
        sampleCarModelSedan2.setCarCategory(standardSedanCategory);
        sampleCarModelSedan2.setMake("SedanMake2");
        sampleCarModelSedan2.setModel("SM2");

        CarModel sampleCarModelFamily = new CarModel();
        sampleCarModelFamily.setCarCategory(familySedanCategory);
        sampleCarModelFamily.setMake("familyMake1");
        sampleCarModelFamily.setModel("FM1");

        CarModel sampleCarModelFamily2 = new CarModel();
        sampleCarModelFamily2.setCarCategory(familySedanCategory);
        sampleCarModelFamily2.setMake("familyMake2");
        sampleCarModelFamily2.setModel("FM2");

        CarModel sampleCarLux = new CarModel();
        sampleCarLux.setCarCategory(luxurySedanCategory);
        sampleCarLux.setMake("LuxMake1");
        sampleCarLux.setModel("LM1");

        CarModel sampleCarLux2 = new CarModel();
        sampleCarLux2.setCarCategory(luxurySedanCategory);
        sampleCarLux2.setMake("LuxMake2");
        sampleCarLux2.setModel("LM2");

        CarModel sampleCarSUV = new CarModel();
        sampleCarSUV.setCarCategory(suvMinivanCategory);
        sampleCarSUV.setMake("SUVMake1");
        sampleCarSUV.setModel("SU1");

        CarModel sampleCarSUV2 = new CarModel();
        sampleCarSUV2.setCarCategory(suvMinivanCategory);
        sampleCarSUV2.setMake("SUVMake2");
        sampleCarSUV2.setModel("SU2");

        if (em.find(CarModel.class, 1l) == null) {
            carModelSessionBean.createCarModel(sampleCarModelSedan);
            carModelSessionBean.createCarModel(sampleCarModelSedan2);
            carModelSessionBean.createCarModel(sampleCarModelFamily);
            carModelSessionBean.createCarModel(sampleCarModelFamily2);
            carModelSessionBean.createCarModel(sampleCarLux);
            carModelSessionBean.createCarModel(sampleCarLux2);
            carModelSessionBean.createCarModel(sampleCarSUV);
            carModelSessionBean.createCarModel(sampleCarSUV2);
        }

        Car sampleSedan = new Car();
        sampleSedan.setModel(sampleCarModelSedan);
        sampleSedan.setCarStatus(CarStatusEnum.INOUTLET);
        sampleSedan.setColor("sedan1Color");
        sampleSedan.setCurrentOutlet(sampleOutlet);
        sampleSedan.setLicensePlateNum("999999");

        Car sampleSedan2 = new Car();
        sampleSedan2.setModel(sampleCarModelSedan2);
        sampleSedan2.setCarStatus(CarStatusEnum.INOUTLET);
        sampleSedan2.setColor("sedan2Color");
        sampleSedan2.setCurrentOutlet(sampleOutlet);
        sampleSedan2.setLicensePlateNum("1123");

        Car sampleSedan3 = new Car();
        sampleSedan3.setModel(sampleCarModelSedan);
        sampleSedan3.setCarStatus(CarStatusEnum.INOUTLET);
        sampleSedan3.setColor("sedan1car2Color");
        sampleSedan3.setCurrentOutlet(sampleOutlet2);
        sampleSedan3.setLicensePlateNum("2");

        Car sampleFamily = new Car();
        sampleFamily.setModel(sampleCarModelFamily);
        sampleFamily.setCarStatus(CarStatusEnum.INOUTLET);
        sampleFamily.setColor("family1Color");
        sampleFamily.setCurrentOutlet(sampleOutlet);
        sampleFamily.setLicensePlateNum("999999");

        Car sampleFamily2 = new Car();
        sampleFamily2.setModel(sampleCarModelFamily2);
        sampleFamily2.setCarStatus(CarStatusEnum.INOUTLET);
        sampleFamily2.setColor("family2Color");
        sampleFamily2.setCurrentOutlet(sampleOutlet);
        sampleFamily2.setLicensePlateNum("1123");

        Car sampleFamily3 = new Car();
        sampleFamily3.setModel(sampleCarModelFamily);
        sampleFamily3.setCarStatus(CarStatusEnum.INOUTLET);
        sampleFamily3.setColor("family1car2Color");
        sampleFamily3.setCurrentOutlet(sampleOutlet2);
        sampleFamily3.setLicensePlateNum("2");

        Car sampleLux = new Car();
        sampleLux.setModel(sampleCarLux);
        sampleLux.setCarStatus(CarStatusEnum.INOUTLET);
        sampleLux.setColor("lux1Color");
        sampleLux.setCurrentOutlet(sampleOutlet);
        sampleLux.setLicensePlateNum("999999");

        Car sampleLux2 = new Car();
        sampleLux2.setModel(sampleCarLux2);
        sampleLux2.setCarStatus(CarStatusEnum.INOUTLET);
        sampleLux2.setColor("lux2Color");
        sampleLux2.setCurrentOutlet(sampleOutlet);
        sampleLux2.setLicensePlateNum("1123");

        Car sampleLux3 = new Car();
        sampleLux3.setModel(sampleCarLux2);
        sampleLux3.setCarStatus(CarStatusEnum.INOUTLET);
        sampleLux3.setColor("lux1car2Color");
        sampleLux3.setCurrentOutlet(sampleOutlet2);
        sampleLux3.setLicensePlateNum("2");

        Car sampleSUV = new Car();
        sampleSUV.setModel(sampleCarSUV);
        sampleSUV.setCarStatus(CarStatusEnum.INOUTLET);
        sampleSUV.setColor("SUV1Color");
        sampleSUV.setCurrentOutlet(sampleOutlet);
        sampleSUV.setLicensePlateNum("999999");

        Car sampleSUV2 = new Car();
        sampleSUV2.setModel(sampleCarSUV2);
        sampleSUV2.setCarStatus(CarStatusEnum.INOUTLET);
        sampleSUV2.setColor("SUV2Color");
        sampleSUV2.setCurrentOutlet(sampleOutlet);
        sampleSUV2.setLicensePlateNum("1123");

        Car sampleSUV3 = new Car();
        sampleSUV3.setModel(sampleCarSUV);
        sampleSUV3.setCarStatus(CarStatusEnum.INOUTLET);
        sampleSUV3.setColor("SUV1car2Color");
        sampleSUV3.setCurrentOutlet(sampleOutlet2);
        sampleSUV3.setLicensePlateNum("2");

        if (em.find(Car.class, 1l) == null) {
            carSessionBean.createCar(sampleSedan, sampleSedan.getModel().getCarModelId(), sampleSedan.getCurrentOutlet().getOutletId());
            carSessionBean.createCar(sampleSedan2, sampleSedan2.getModel().getCarModelId(), sampleSedan2.getCurrentOutlet().getOutletId());
            carSessionBean.createCar(sampleSedan3, sampleSedan3.getModel().getCarModelId(), sampleSedan3.getCurrentOutlet().getOutletId());
            carSessionBean.createCar(sampleFamily, sampleFamily.getModel().getCarModelId(), sampleFamily.getCurrentOutlet().getOutletId());
            carSessionBean.createCar(sampleFamily2, sampleFamily2.getModel().getCarModelId(), sampleFamily2.getCurrentOutlet().getOutletId());
            carSessionBean.createCar(sampleFamily3, sampleFamily3.getModel().getCarModelId(), sampleFamily3.getCurrentOutlet().getOutletId());
            carSessionBean.createCar(sampleLux, sampleLux.getModel().getCarModelId(), sampleLux.getCurrentOutlet().getOutletId());
            carSessionBean.createCar(sampleLux2, sampleLux2.getModel().getCarModelId(), sampleLux2.getCurrentOutlet().getOutletId());
            carSessionBean.createCar(sampleLux3, sampleLux3.getModel().getCarModelId(), sampleLux3.getCurrentOutlet().getOutletId());
            carSessionBean.createCar(sampleSUV, sampleSUV.getModel().getCarModelId(), sampleSUV.getCurrentOutlet().getOutletId());
            carSessionBean.createCar(sampleSUV2, sampleSUV2.getModel().getCarModelId(), sampleSUV2.getCurrentOutlet().getOutletId());
            carSessionBean.createCar(sampleSUV3, sampleSUV3.getModel().getCarModelId(), sampleSUV3.getCurrentOutlet().getOutletId());
        }
        
        
        
        Employee systemAdmin = new Employee("system", "manager", "sys", "password", EmployeeAccessRightEnum.SYSTEMADMINISTRATOR, sampleOutlet);
        Employee salesManager = new Employee("sales", "manager", "sales", "password", EmployeeAccessRightEnum.SALESMANAGER, sampleOutlet);
        Employee operationsManager = new Employee("ops", "manager", "ops", "password", EmployeeAccessRightEnum.OPERATIONSMANAGER, sampleOutlet);
        Employee customerServiceExec = new Employee("cse", "manager", "cse", "password", EmployeeAccessRightEnum.CUSTOMERSERVICEEXECUTIVE, sampleOutlet);

        if (em.find(Employee.class, 1l) == null) {
            employeeSessionBeanLocal.createEmployee(salesManager, sampleOutlet.getOutletId());
            employeeSessionBeanLocal.createEmployee(operationsManager, sampleOutlet.getOutletId());
            employeeSessionBeanLocal.createEmployee(customerServiceExec, sampleOutlet.getOutletId());
            employeeSessionBeanLocal.createEmployee(systemAdmin, sampleOutlet.getOutletId());
        }
        
        Partner partner = new Partner();
        partner.setUsername("partner");
        partner.setPassword("password");
        
        if (em.find(Partner.class, 1l) == null) {
            partnerSessionBean.createPartner(partner);
        }
    }
}
