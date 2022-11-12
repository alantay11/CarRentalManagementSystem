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
import entity.RentalRate;
import enumeration.CarStatusEnum;
import enumeration.EmployeeAccessRightEnum;
import enumeration.RentalRateEnum;
import exception.CarExistException;
import exception.CarModelExistException;
import exception.InputDataValidationException;
import exception.InvalidIdException;
import exception.RentalRateExistException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private CarModelSessionBeanLocal carModelSessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;
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

        ////////// OUTLET
        Outlet outletA = new Outlet();
        outletA.setAddress("Outlet A");

        Outlet outletB = new Outlet();
        outletB.setAddress("Outlet B");

        Outlet outletC = new Outlet();
        outletC.setAddress("Outlet C");
        outletC.setOpeningTime(LocalDateTime.parse("2000-01-01T08:00"));
        outletC.setClosingTime(LocalTime.parse("22:00"));

        if (em.find(Outlet.class, 1l) == null) {
            outletSessionBean.createOutlet(outletA);
            outletSessionBean.createOutlet(outletB);
            outletSessionBean.createOutlet(outletC);
        }
        //////////

        ////////// Employee
        Employee systemAdmin = new Employee("system", "manager", "sys", "password", EmployeeAccessRightEnum.SYSTEMADMINISTRATOR, outletA);
        Employee a1 = new Employee("sales", "A", "a1", "password", EmployeeAccessRightEnum.SALESMANAGER, outletA);
        Employee a2 = new Employee("ops", "A", "a2", "password", EmployeeAccessRightEnum.OPERATIONSMANAGER, outletA);
        Employee a3 = new Employee("cse", "A", "a3", "password", EmployeeAccessRightEnum.CUSTOMERSERVICEEXECUTIVE, outletA);
        Employee a4 = new Employee("employee", "A4", "a4", "password", EmployeeAccessRightEnum.EMPLOYEE, outletA);
        Employee a5 = new Employee("employee", "A5", "a5", "password", EmployeeAccessRightEnum.EMPLOYEE, outletA);

        Employee b1 = new Employee("sales", "B", "b1", "password", EmployeeAccessRightEnum.SALESMANAGER, outletB);
        Employee b2 = new Employee("ops", "B", "b2", "password", EmployeeAccessRightEnum.OPERATIONSMANAGER, outletB);
        Employee b3 = new Employee("cse", "B", "b3", "password", EmployeeAccessRightEnum.CUSTOMERSERVICEEXECUTIVE, outletB);

        Employee c1 = new Employee("sales", "C", "c1", "password", EmployeeAccessRightEnum.SALESMANAGER, outletC);
        Employee c2 = new Employee("ops", "C", "c2", "password", EmployeeAccessRightEnum.OPERATIONSMANAGER, outletC);
        Employee c3 = new Employee("cse", "C", "c3", "password", EmployeeAccessRightEnum.CUSTOMERSERVICEEXECUTIVE, outletC);

        if (em.find(Employee.class, 1l) == null) {
            employeeSessionBeanLocal.createEmployee(systemAdmin, outletA.getOutletId());

            employeeSessionBeanLocal.createEmployee(a1, outletA.getOutletId());
            employeeSessionBeanLocal.createEmployee(a2, outletA.getOutletId());
            employeeSessionBeanLocal.createEmployee(a3, outletA.getOutletId());
            employeeSessionBeanLocal.createEmployee(a4, outletA.getOutletId());
            employeeSessionBeanLocal.createEmployee(a5, outletA.getOutletId());

            employeeSessionBeanLocal.createEmployee(b1, outletB.getOutletId());
            employeeSessionBeanLocal.createEmployee(b2, outletB.getOutletId());
            employeeSessionBeanLocal.createEmployee(b3, outletB.getOutletId());

            employeeSessionBeanLocal.createEmployee(c1, outletC.getOutletId());
            employeeSessionBeanLocal.createEmployee(c2, outletC.getOutletId());
            employeeSessionBeanLocal.createEmployee(c3, outletC.getOutletId());
        }
        //////////

        ////////// CarCategory
        CarCategory standardSedanCategory = new CarCategory("Standard Sedan");
        CarCategory familySedanCategory = new CarCategory("Family Sedan");
        CarCategory luxurySedanCategory = new CarCategory("Luxury Sedan");
        CarCategory suvMinivanCategory = new CarCategory("SUV and Minivan");

        if (em.find(CarCategory.class, 1l) == null) {
            standardSedanCategory = carCategorySessionBean.createCarCategory(standardSedanCategory);
            familySedanCategory = carCategorySessionBean.createCarCategory(familySedanCategory);
            luxurySedanCategory = carCategorySessionBean.createCarCategory(luxurySedanCategory);
            suvMinivanCategory = carCategorySessionBean.createCarCategory(suvMinivanCategory);
        }

        ////////// CarMODEL
        CarModel toyotaCorolla = new CarModel();
        toyotaCorolla.setCarCategory(standardSedanCategory);
        toyotaCorolla.setMake("Toyota");
        toyotaCorolla.setModel("Corolla");

        CarModel hondaCivic = new CarModel();
        hondaCivic.setCarCategory(standardSedanCategory);
        hondaCivic.setMake("Honda");
        hondaCivic.setModel("Civic");

        CarModel nissanSunny = new CarModel();
        nissanSunny.setCarCategory(standardSedanCategory);
        nissanSunny.setMake("Nissan");
        nissanSunny.setModel("Sunny");

        CarModel mercedesEClass = new CarModel();
        mercedesEClass.setCarCategory(luxurySedanCategory);
        mercedesEClass.setMake("Mercedes");
        mercedesEClass.setModel("E Class");

        CarModel bmw5Series = new CarModel();
        bmw5Series.setCarCategory(luxurySedanCategory);
        bmw5Series.setMake("BMW");
        bmw5Series.setModel("5 Series");

        CarModel audiA6 = new CarModel();
        audiA6.setCarCategory(luxurySedanCategory);
        audiA6.setMake("Audi");
        audiA6.setModel("A6");

        if (em.find(CarModel.class, 1l) == null) {
            try {
                toyotaCorolla = carModelSessionBean.createCarModel(toyotaCorolla);
                hondaCivic = carModelSessionBean.createCarModel(hondaCivic);
                nissanSunny = carModelSessionBean.createCarModel(nissanSunny);
                mercedesEClass = carModelSessionBean.createCarModel(mercedesEClass);
                bmw5Series = carModelSessionBean.createCarModel(bmw5Series);
                audiA6 = carModelSessionBean.createCarModel(audiA6);
            } catch (CarModelExistException ex) {
                System.out.println("Why does it exist");
            } catch (InputDataValidationException ex) {
                System.out.println("How can it be invalid carmodel");
            }
        }
        //////////

        ////////// Car
        Car SS00A1TC = new Car();
        SS00A1TC.setModel(toyotaCorolla);
        SS00A1TC.setCarStatus(CarStatusEnum.AVAILABLE);
        SS00A1TC.setColor("toyotaA");
        SS00A1TC.setCurrentOutlet(outletA);
        SS00A1TC.setLicensePlateNum("SS00A1TC");

        Car SS00A2TC = new Car();
        SS00A2TC.setModel(toyotaCorolla);
        SS00A2TC.setCarStatus(CarStatusEnum.AVAILABLE);
        SS00A2TC.setColor("toyotaB");
        SS00A2TC.setCurrentOutlet(outletA);
        SS00A2TC.setLicensePlateNum("SS00A2TC");

        Car SS00A3TC = new Car();
        SS00A3TC.setModel(toyotaCorolla);
        SS00A3TC.setCarStatus(CarStatusEnum.AVAILABLE);
        SS00A3TC.setColor("toyotaC");
        SS00A3TC.setCurrentOutlet(outletA);
        SS00A3TC.setLicensePlateNum("SS00A3TC");

        Car SS00B1HC = new Car();
        SS00B1HC.setModel(hondaCivic);
        SS00B1HC.setCarStatus(CarStatusEnum.AVAILABLE);
        SS00B1HC.setColor("civicA");
        SS00B1HC.setCurrentOutlet(outletB);
        SS00B1HC.setLicensePlateNum("SS00B1HC");

        Car SS00B2HC = new Car();
        SS00B2HC.setModel(hondaCivic);
        SS00B2HC.setCarStatus(CarStatusEnum.AVAILABLE);
        SS00B2HC.setColor("civicB");
        SS00B2HC.setCurrentOutlet(outletB);
        SS00B2HC.setLicensePlateNum("SS00B2HC");

        Car SS00B3HC = new Car();
        SS00B3HC.setModel(hondaCivic);
        SS00B3HC.setCarStatus(CarStatusEnum.AVAILABLE);
        SS00B3HC.setColor("civicC");
        SS00B3HC.setCurrentOutlet(outletB);
        SS00B3HC.setLicensePlateNum("SS00B3HC");

        Car SS00C1NS = new Car();
        SS00C1NS.setModel(nissanSunny);
        SS00C1NS.setCarStatus(CarStatusEnum.AVAILABLE);
        SS00C1NS.setColor("sunnyA");
        SS00C1NS.setCurrentOutlet(outletC);
        SS00C1NS.setLicensePlateNum("SS00C1NS");

        Car SS00C2NS = new Car();
        SS00C2NS.setModel(nissanSunny);
        SS00C2NS.setCarStatus(CarStatusEnum.AVAILABLE);
        SS00C2NS.setColor("sunnyB");
        SS00C2NS.setCurrentOutlet(outletC);
        SS00C2NS.setLicensePlateNum("SS00C2NS");

        Car SS00C3NS = new Car();
        SS00C3NS.setModel(nissanSunny);
        SS00C3NS.setCarStatus(CarStatusEnum.REPAIR);
        SS00C3NS.setColor("sunnyRepair");
        SS00C3NS.setCurrentOutlet(outletC);
        SS00C3NS.setLicensePlateNum("SS00C3NS");

        Car LS00A4ME = new Car();
        LS00A4ME.setModel(mercedesEClass);
        LS00A4ME.setCarStatus(CarStatusEnum.AVAILABLE);
        LS00A4ME.setColor("merceclass");
        LS00A4ME.setCurrentOutlet(outletA);
        LS00A4ME.setLicensePlateNum("LS00A4ME");

        Car LS00B4B5 = new Car();
        LS00B4B5.setModel(bmw5Series);
        LS00B4B5.setCarStatus(CarStatusEnum.AVAILABLE);
        LS00B4B5.setColor("bmw5series");
        LS00B4B5.setCurrentOutlet(outletB);
        LS00B4B5.setLicensePlateNum("LS00B4B5");

        Car LS00C4A6 = new Car();
        LS00C4A6.setModel(audiA6);
        LS00C4A6.setCarStatus(CarStatusEnum.AVAILABLE);
        LS00C4A6.setColor("audia6");
        LS00C4A6.setCurrentOutlet(outletC);
        LS00C4A6.setLicensePlateNum("LS00C4A6");

        if (em.find(Car.class, 1l) == null) {
            try {
                carSessionBean.createCar(SS00A1TC, SS00A1TC.getModel().getCarModelId(), SS00A1TC.getCurrentOutlet().getOutletId());
                carSessionBean.createCar(SS00A2TC, SS00A2TC.getModel().getCarModelId(), SS00A2TC.getCurrentOutlet().getOutletId());
                carSessionBean.createCar(SS00A3TC, SS00A3TC.getModel().getCarModelId(), SS00A3TC.getCurrentOutlet().getOutletId());
                carSessionBean.createCar(SS00B1HC, SS00B1HC.getModel().getCarModelId(), SS00B1HC.getCurrentOutlet().getOutletId());
                carSessionBean.createCar(SS00B2HC, SS00B2HC.getModel().getCarModelId(), SS00B2HC.getCurrentOutlet().getOutletId());
                carSessionBean.createCar(SS00B3HC, SS00B3HC.getModel().getCarModelId(), SS00B3HC.getCurrentOutlet().getOutletId());
                carSessionBean.createCar(SS00C1NS, SS00C1NS.getModel().getCarModelId(), SS00C1NS.getCurrentOutlet().getOutletId());
                carSessionBean.createCar(SS00C2NS, SS00C2NS.getModel().getCarModelId(), SS00C2NS.getCurrentOutlet().getOutletId());
                carSessionBean.createCar(SS00C3NS, SS00C3NS.getModel().getCarModelId(), SS00C3NS.getCurrentOutlet().getOutletId());
                carSessionBean.createCar(LS00A4ME, LS00A4ME.getModel().getCarModelId(), LS00A4ME.getCurrentOutlet().getOutletId());
                carSessionBean.createCar(LS00B4B5, LS00B4B5.getModel().getCarModelId(), LS00B4B5.getCurrentOutlet().getOutletId());
                carSessionBean.createCar(LS00C4A6, LS00C4A6.getModel().getCarModelId(), LS00C4A6.getCurrentOutlet().getOutletId());
            } catch (CarExistException ex) {
                System.out.println("why does car already exist");
            } catch (InputDataValidationException ex) {
                System.out.println("why is car invalid");
            }
        }
        ////////// 

        ////////// RentalRate
        RentalRate defaultStandardSedan = new RentalRate();
        defaultStandardSedan.setRateName("Default");
        defaultStandardSedan.setCarCategory(standardSedanCategory);
        defaultStandardSedan.setRatePerDay(new BigDecimal("100"));
        defaultStandardSedan.setRateType(RentalRateEnum.Default);

        RentalRate weekendPromoStandardSedan = new RentalRate();
        weekendPromoStandardSedan.setRateName("Weekend Promo");
        weekendPromoStandardSedan.setCarCategory(standardSedanCategory);
        weekendPromoStandardSedan.setRatePerDay(new BigDecimal("80"));
        weekendPromoStandardSedan.setRateType(RentalRateEnum.Promotion);
        weekendPromoStandardSedan.setStartDate(LocalDateTime.parse("2022-12-09T12:00"));
        weekendPromoStandardSedan.setEndDate(LocalDateTime.parse("2022-12-11T00:00"));

        RentalRate defaultFamilySedan = new RentalRate();
        defaultFamilySedan.setRateName("Default");
        defaultFamilySedan.setCarCategory(familySedanCategory);
        defaultFamilySedan.setRatePerDay(new BigDecimal("200"));
        defaultFamilySedan.setRateType(RentalRateEnum.Default);

        RentalRate defaultLuxurySedan = new RentalRate();
        defaultLuxurySedan.setRateName("Default");
        defaultLuxurySedan.setCarCategory(luxurySedanCategory);
        defaultLuxurySedan.setRatePerDay(new BigDecimal("300"));
        defaultLuxurySedan.setRateType(RentalRateEnum.Default);

        RentalRate mondayLuxurySedan = new RentalRate();
        mondayLuxurySedan.setRateName("Monday");
        mondayLuxurySedan.setCarCategory(luxurySedanCategory);
        mondayLuxurySedan.setRatePerDay(new BigDecimal("310"));
        mondayLuxurySedan.setRateType(RentalRateEnum.Peak);
        mondayLuxurySedan.setStartDate(LocalDateTime.parse("2022-12-05T00:00"));
        mondayLuxurySedan.setEndDate(LocalDateTime.parse("2022-12-05T23:59"));

        RentalRate tuesdayLuxurySedan = new RentalRate();
        tuesdayLuxurySedan.setRateName("Tuesday");
        tuesdayLuxurySedan.setCarCategory(luxurySedanCategory);
        tuesdayLuxurySedan.setRatePerDay(new BigDecimal("320"));
        tuesdayLuxurySedan.setRateType(RentalRateEnum.Peak);
        tuesdayLuxurySedan.setStartDate(LocalDateTime.parse("2022-12-06T00:00"));
        tuesdayLuxurySedan.setEndDate(LocalDateTime.parse("2022-12-06T23:59"));

        RentalRate wedLuxurySedan = new RentalRate();
        wedLuxurySedan.setRateName("Wednesday");
        wedLuxurySedan.setCarCategory(luxurySedanCategory);
        wedLuxurySedan.setRatePerDay(new BigDecimal("330"));
        wedLuxurySedan.setRateType(RentalRateEnum.Peak);
        wedLuxurySedan.setStartDate(LocalDateTime.parse("2022-12-07T00:00"));
        wedLuxurySedan.setEndDate(LocalDateTime.parse("2022-12-07T23:59"));

        RentalRate weekdayPromoLuxurySedan = new RentalRate();
        weekdayPromoLuxurySedan.setRateName("Weekday Promo");
        weekdayPromoLuxurySedan.setCarCategory(luxurySedanCategory);
        weekdayPromoLuxurySedan.setRatePerDay(new BigDecimal("250"));
        weekdayPromoLuxurySedan.setRateType(RentalRateEnum.Promotion);
        weekdayPromoLuxurySedan.setStartDate(LocalDateTime.parse("2022-12-07T12:00"));
        weekdayPromoLuxurySedan.setEndDate(LocalDateTime.parse("2022-12-08T12:00"));

        RentalRate defaultSUVMinivan = new RentalRate();
        defaultSUVMinivan.setRateName("Default");
        defaultSUVMinivan.setCarCategory(suvMinivanCategory);
        defaultSUVMinivan.setRatePerDay(new BigDecimal("400"));
        defaultSUVMinivan.setRateType(RentalRateEnum.Default);

        try {
            if (em.find(RentalRate.class, 1l) == null) {
                rentalRateSessionBean.createRentalRate(defaultStandardSedan, defaultStandardSedan.getCarCategory().getCarCategoryId());
                rentalRateSessionBean.createRentalRate(weekendPromoStandardSedan, weekendPromoStandardSedan.getCarCategory().getCarCategoryId());
                rentalRateSessionBean.createRentalRate(defaultFamilySedan, defaultFamilySedan.getCarCategory().getCarCategoryId());
                rentalRateSessionBean.createRentalRate(defaultLuxurySedan, defaultLuxurySedan.getCarCategory().getCarCategoryId());
                rentalRateSessionBean.createRentalRate(mondayLuxurySedan, mondayLuxurySedan.getCarCategory().getCarCategoryId());
                rentalRateSessionBean.createRentalRate(tuesdayLuxurySedan, tuesdayLuxurySedan.getCarCategory().getCarCategoryId());
                rentalRateSessionBean.createRentalRate(wedLuxurySedan, wedLuxurySedan.getCarCategory().getCarCategoryId());
                rentalRateSessionBean.createRentalRate(weekdayPromoLuxurySedan, weekdayPromoLuxurySedan.getCarCategory().getCarCategoryId());
                rentalRateSessionBean.createRentalRate(defaultSUVMinivan, defaultSUVMinivan.getCarCategory().getCarCategoryId());

            }
        } catch (InvalidIdException ex) {
            System.out.println("Invalid id probably not persisted properly");
        } catch (RentalRateExistException ex) {
            System.out.println("Why does it exist");
        } catch (InputDataValidationException ex) {
            System.out.println("How can this happen");
        }
        //////////

        ////////// Partner
        Partner partner = new Partner();
        partner.setName("Holiday.com");
        partner.setUsername("partner");
        partner.setPassword("password");

        if (em.find(Partner.class, 1l) == null) {
            partnerSessionBean.createPartner(partner);
        }
        //////////
    }
}
