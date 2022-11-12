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
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        Partner partner = partnerSessionBeanLocal.partnerLogin(username, password);

        em.detach(partner);
        for (Customer c : partner.getCustomerList()) {
            em.detach(c);
            c.setPartner(null);
        }

        System.out.println("********** PartnerWebService.partnerLogin");

        //partner.setCustomerList(null); don't need?
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
    public boolean searchCarByMakeModel(@WebParam(name = "makeModelId") long makeModelId, @WebParam(name = "pickUpDateTime") String pickUpDateTime,
            @WebParam(name = "returnDateTime") String returnDateTime, @WebParam(name = "pickupOutletId") long pickupOutletId,
            @WebParam(name = "returnOutletId") long returnOutletId) throws OutletIsClosedException {
        return carSessionBeanLocal.searchCarByMakeModel(makeModelId, LocalDateTime.parse(pickUpDateTime), LocalDateTime.parse(returnDateTime), pickupOutletId, returnOutletId);
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
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.partner.partnerId = : partnerId");
        query.setParameter("partnerId", partnerId);

        List<Reservation> reservations = query.getResultList();

        for (Reservation r : reservations) {
            em.detach(r);
            r.setRentalRateList(null);
        }
        return reservations;
    }

    @WebMethod(operationName = "searchCarByCategory")
    public boolean searchCarByCategory(@WebParam(name = "categoryId") long categoryId, @WebParam(name = "pickUpDateTime") String pickUpDateTime,
            @WebParam(name = "returnDateTime") String returnDateTime, @WebParam(name = "pickupOutletId") long pickupOutletId,
            @WebParam(name = "returnOutletId") long returnOutletId) throws OutletIsClosedException {
        return carSessionBeanLocal.searchCarByCategory(categoryId, LocalDateTime.parse(pickUpDateTime), LocalDateTime.parse(returnDateTime), pickupOutletId, returnOutletId);
    }

    @WebMethod(operationName = "calculateTotalCost")
    public BigDecimal calculateTotalCost(@WebParam(name = "reservation") Reservation reservation) throws NoRentalRateAvailableException {
        return rentalRateSessionBeanLocal.calculateTotalCost(reservation);
    }

    @WebMethod(operationName = "createReservation")
    public Reservation createReservation(@WebParam(name = "reservation") Reservation reservation) throws ReservationExistException, InputDataValidationException {
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
            em.persist(customer);
            customer.getPartner().getCustomerList().add(customer);

            em.flush();
            return customer;
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

        reservation.setRentalRateList(null);

        return reservation;
    }

    @WebMethod(operationName = "cancelReservation")
    public void cancelReservation(@WebParam(name = "reservationId") long reservationId, @WebParam(name = "refund") BigDecimal refund) throws ReservationRecordNotFoundException {
        reservationSessionBeanLocal.cancelReservation(reservationId, refund);
    }

    @WebMethod(operationName = "calculatePenalty")
    public BigDecimal calculatePenalty(@WebParam(name = "totalCost") BigDecimal totalCost, @WebParam(name = "reservation") Reservation reservation) throws ReservationRecordNotFoundException {
        BigDecimal penalty = new BigDecimal("0.00");
        LocalDateTime pickup = reservation.getPickupTime();

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
