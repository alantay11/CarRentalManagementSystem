/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.CreditCardSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.CreditCard;
import entity.Customer;
import entity.Outlet;
import entity.Partner;
import entity.Reservation;
import exception.CreditCardExistException;
import exception.CustomerExistException;
import exception.InputDataValidationException;
import exception.InvalidLoginCredentialException;
import exception.NoRentalRateAvailableException;
import exception.OutletIsClosedException;
import exception.ReservationExistException;
import exception.ReservationRecordNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Uni
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    @EJB
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;
    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;
    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;
    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;
    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;
    @EJB
    private CarModelSessionBeanLocal carModelSessionBeanLocal;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    public PartnerWebService() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @WebMethod(operationName = "partnerLogin")
    public Partner partnerLogin(@WebParam(name = "username") String username,
            @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        Partner partner;
        try {
            partner = partnerSessionBeanLocal.partnerLogin(username, password);
            em.detach(partner);
            System.out.println("********** PartnerWebService.partnerLogin");

            em.flush();

        } catch (InvalidLoginCredentialException ex) {
            throw ex;
        }
        return partner;
    }

    @WebMethod(operationName = "retrieveAllOutlets")
    public List<Outlet> retrieveAllOutlets() {
        List<Outlet> outlets = outletSessionBeanLocal.retrieveAllOutlets();

        for (Outlet o : outlets) {
            em.detach(o);
            o.setCarList(null);
            o.setEmployeeList(null);
            o.setInboundTransitDriverDispatchList(null);
        }
        return outlets;
    }

    @WebMethod(operationName = "retrieveAllCarModels")
    public List<CarModel> retrieveAllCarModels() {
        List<CarModel> models = carModelSessionBeanLocal.retrieveAllCarModels();

        for (CarModel c : models) {
            em.detach(c);
            c.setCarList(null);
        }
        return models;
    }

    @WebMethod(operationName = "searchCarByMakeModel")
    public boolean searchCarByMakeModel(@WebParam(name = "makeModelId") long makeModelId, @WebParam(name = "pickupGreg") Date pickupGreg,
            @WebParam(name = "returnGreg") Date returnGreg, @WebParam(name = "pickupOutletId") long pickupOutletId,
            @WebParam(name = "returnOutletId") long returnOutletId) throws OutletIsClosedException {

        LocalDateTime pickUpDateTime = pickupGreg.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime returnDateTime = returnGreg.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return carSessionBeanLocal.searchCarByMakeModel(makeModelId, pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId);
    }

    @WebMethod(operationName = "searchCarByCategory")
    public boolean searchCarByCategory(@WebParam(name = "categoryId") long categoryId, @WebParam(name = "pickupGreg") Date pickupGreg,
            @WebParam(name = "returnGreg") Date returnGreg, @WebParam(name = "pickupOutletId") long pickupOutletId,
            @WebParam(name = "returnOutletId") long returnOutletId) throws OutletIsClosedException {

        LocalDateTime pickUpDateTime = pickupGreg.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime returnDateTime = returnGreg.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return carSessionBeanLocal.searchCarByCategory(categoryId, pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId);
    }

    @WebMethod(operationName = "retrieveAllCarCategories")
    public List<CarCategory> retrieveAllCarCategories() {
        List<CarCategory> categories = carCategorySessionBeanLocal.retrieveAllCarCategories();

        for (CarCategory c : categories) {
            em.detach(c);
            c.setCarList(null);
            c.setRentalRateList(null);
        }
        return categories;
    }

    @WebMethod(operationName = "retrieveAllPartnerReservations")
    public List<Reservation> retrieveAllPartnerReservations(@WebParam(name = "partnerId") long partnerId) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.partner.partnerId = :partnerId AND r.cancelled = false");
        query.setParameter("partnerId", partnerId);
        System.out.println("Query created");
        List<Reservation> reservations = query.getResultList();

        for (Reservation r : reservations) {
            em.detach(r);
            r.setRentalRateList(null);
            r.setPartner(null);
            System.out.println("detaching");
            r.setCustomer(null);
            r.setCar(null);
            r.setCarModel(null);
            r.setCarCategory(null);
            r.setDepartureOutlet(null);
            r.setDestinationOutlet(null);
        }
        System.out.println("Detached");
        em.flush();
        System.out.println("reservations: " + reservations.size());
        return reservations;
    }

    @WebMethod(operationName = "retrieveCategoryIdOfModel")
    public long retrieveCategoryIdOfModel(@WebParam(name = "makemodelId") long makemodelId) {
        CarModel carModel = em.find(CarModel.class, makemodelId);

        return carModel.getCarCategory().getCarCategoryId();
    }

    @WebMethod(operationName = "retrieveCategory")
    public CarCategory retrieveCategory(@WebParam(name = "categoryId") long categoryId) {
        CarCategory carCategory = em.find(CarCategory.class, categoryId);

        em.detach(carCategory);
        carCategory.setCarList(null);
        carCategory.setRentalRateList(null);
        return carCategory;
    }

    @WebMethod(operationName = "searchCar")
    public boolean searchCar(@WebParam(name = "pickUpDateTime") String pickUpDateTime,
            @WebParam(name = "returnDateTime") String returnDateTime, @WebParam(name = "pickupOutletId") long pickupOutletId,
            @WebParam(name = "returnOutletId") long returnOutletId) throws OutletIsClosedException {
        return carSessionBeanLocal.searchCar(LocalDateTime.parse(pickUpDateTime), LocalDateTime.parse(returnDateTime), pickupOutletId, returnOutletId);
    }

    @WebMethod(operationName = "calculateTotalCost")
    public BigDecimal calculateTotalCost(@WebParam(name = "pickupGreg") Date pickupGreg,
            @WebParam(name = "returnGreg") Date returnGreg, @WebParam(name = "categoryId") long categoryId) throws NoRentalRateAvailableException {
        LocalDateTime pickUpDateTime = pickupGreg.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime returnDateTime = returnGreg.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return rentalRateSessionBeanLocal.calculateTotalCostWeb(pickUpDateTime, returnDateTime, categoryId);
    }

    @WebMethod(operationName = "createReservation")
    public Reservation createReservation(@WebParam(name = "reservation") Reservation reservation,
            @WebParam(name = "pickupGreg") Date pickupGreg,
            @WebParam(name = "returnGreg") Date returnGreg) throws ReservationExistException, InputDataValidationException {
        LocalDateTime pickUpDateTime = pickupGreg.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime returnDateTime = returnGreg.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        reservation.setPickupTime(pickUpDateTime);
        reservation.setReturnTime(returnDateTime);

        Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);

        if (constraintViolations.isEmpty()) {
            return reservationSessionBeanLocal.createReservation(reservation);
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessageForReservation(constraintViolations));
        }
    }

    @WebMethod(operationName = "createCustomer")
    public Customer createCustomer(@WebParam(name = "customer") Customer customer) throws InputDataValidationException, CustomerExistException {
        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(customer);

                em.flush();
                em.detach(customer);
                customer.setPartner(null);
                List<Reservation> reservations = customer.getReservationList();
                for (Reservation r : reservations) {
                    r.setCustomer(null);
                }
                em.flush();
                return customer;
            } catch (PersistenceException ex) {
                throw new CustomerExistException();
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessageForCustomer(constraintViolations));
        }
    }

    @WebMethod(operationName = "createCreditCard")
    public CreditCard createCreditCard(@WebParam(name = "creditCard") CreditCard creditCard, @WebParam(name = "customerId") long customerId,
            @WebParam(name = "expiry") String expiry) throws InputDataValidationException, CreditCardExistException {
        LocalDate expiryDate;
        expiryDate = LocalDate.parse(expiry);
        expiryDate = YearMonth.from(expiryDate).atEndOfMonth();
        creditCard.setExpiryDate(expiryDate);

        Set<ConstraintViolation<CreditCard>> constraintViolations = validator.validate(creditCard);

        if (constraintViolations.isEmpty()) {
            return creditCardSessionBeanLocal.createCreditCard(creditCard, customerId);
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessageForCreditCard(constraintViolations));
        }
    }

    @WebMethod(operationName = "retrieveReservation")
    public Reservation retrieveReservation(@WebParam(name = "reservationId") long reservationId) throws ReservationRecordNotFoundException {
        Reservation reservation = reservationSessionBeanLocal.retrieveReservation(reservationId);

        em.detach(reservation);
        reservation.setRentalRateList(null);
        reservation.setCustomer(null);
        reservation.setCar(null);
        reservation.setCarCategory(null);
        reservation.setCarModel(null);
        reservation.setDepartureOutlet(null);
        reservation.setDestinationOutlet(null);

        return reservation;
    }

    @WebMethod(operationName = "retrieveOutlet")
    public Outlet retrieveOutlet(@WebParam(name = "outletId") long outletId) throws ReservationRecordNotFoundException {
        Outlet outlet = outletSessionBeanLocal.retrieveOutlet(outletId);

        em.detach(outlet);
        List<Car> cars = outlet.getCarList();
        
        for (Car c : cars) {
            c.setCurrentOutlet(null);
        }
        
        outlet.setCarList(null);
        outlet.setEmployeeList(null);
        outlet.setInboundTransitDriverDispatchList(null);
        em.flush();

        return outlet;
    }

    @WebMethod(operationName = "reservationStringMaker")
    public String reservationStringMaker(@WebParam(name = "reservationId") long reservationId) {
        System.out.println("started");
        String result = "";
        try {
            Reservation reservation = reservationSessionBeanLocal.retrieveReservation(reservationId);
            /*em.detach(reservation);
            System.out.println("string detach");*/

            System.out.println("string ID: " + reservationId);

            result = ", Category: " + reservation.getCarCategory().getCarCategoryName()
                    + "\nPickup Time: " + reservation.getPickupTime().toString().replace("T", ", ")
                    + " from " + reservation.getDepartureOutlet().getAddress()
                    + "\nReturn Time: " + reservation.getReturnTime().toString().replace("T", ", ")
                    + " to " + reservation.getDestinationOutlet().getAddress();
            System.out.println("string generated");

        } catch (ReservationRecordNotFoundException ex) {
            System.out.println("empty");
        }
        return result;
    }

    @WebMethod(operationName = "cancelReservation")
    public void cancelReservation(@WebParam(name = "reservationId") long reservationId, @WebParam(name = "refund") BigDecimal refund) throws ReservationRecordNotFoundException {
        reservationSessionBeanLocal.cancelReservation(reservationId, refund);
    }

    @WebMethod(operationName = "calculatePenalty")
    public BigDecimal calculatePenalty(@WebParam(name = "totalCost") BigDecimal totalCost, @WebParam(name = "pickupGreg") Date pickupGreg) throws ReservationRecordNotFoundException {
        BigDecimal penalty = new BigDecimal("0.00");

        LocalDateTime pickup = pickupGreg.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (LocalDateTime.now().isBefore(pickup.minusDays(14))) {
            //System.out.println("more than 14 before");
        } else if (LocalDateTime.now().isBefore(pickup.minusDays(7))) {
            penalty = totalCost.multiply(new BigDecimal("0.2"));
            //System.out.println("14 to 7 before");
        } else if (LocalDateTime.now().isBefore(pickup.minusDays(3))) {
            penalty = totalCost.multiply(new BigDecimal("0.5"));
            //System.out.println("7 to 3 before");
        } else if (LocalDateTime.now().isBefore(pickup)) {
            penalty = totalCost.multiply(new BigDecimal("0.7"));
            //System.out.println("3 or less before");
        }

        return penalty.setScale(2);
    }

    private String prepareInputDataValidationErrorsMessageForReservation(Set<ConstraintViolation<Reservation>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    private String prepareInputDataValidationErrorsMessageForCreditCard(Set<ConstraintViolation<CreditCard>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    private String prepareInputDataValidationErrorsMessageForCustomer(Set<ConstraintViolation<Customer>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
