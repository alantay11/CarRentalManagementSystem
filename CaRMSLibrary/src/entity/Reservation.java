/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Andrea
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    @NotNull
    @Future
    private LocalDateTime pickupTime;
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    @NotNull
    @Future
    private LocalDateTime returnTime;
    @Column(nullable = false, columnDefinition = "boolean default false")
    @NotNull
    private boolean cancelled;
    @Column(nullable = false, columnDefinition = "boolean default false")
    @NotNull
    private boolean paid;
    @Column(nullable = false, precision = 11, scale = 2)
    @Digits(integer = 6, fraction = 2)
    private BigDecimal price;
    @Column(nullable = false, precision = 11, scale = 2)
    @Digits(integer = 6, fraction = 2)
    private BigDecimal refundAmount;
    
    @Column(length = 32)
    private String pickupString;
    @Column(length = 32)
    private String returnString;

    @ManyToMany
    private List<RentalRate> rentalRateList;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;

    @OneToOne
    private Car car;
    @ManyToOne
    @JoinColumn
    private CarModel carModel;
    @ManyToOne
    @JoinColumn
    private CarCategory carCategory;

    @ManyToOne
    private Outlet departureOutlet;
    @ManyToOne
    private Outlet destinationOutlet;

    @OneToOne(mappedBy = "reservation", optional = true)
    private TransitDriverDispatch transitDriverDispatch;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Partner partner;

    public Reservation() {
        this.rentalRateList = new ArrayList<>();
        this.cancelled = false;
        this.paid = false;
        this.price = new BigDecimal("0.00");
        this.refundAmount = new BigDecimal("0.00");
    }

    public String getPickupString() {
        return pickupString;
    }

    public void setPickupString(String pickupString) {
        this.pickupString = pickupString;
    }

    public String getReturnString() {
        return returnString;
    }

    public void setReturnString(String returnString) {
        this.returnString = returnString;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    public Long getreservationId() {
        return reservationId;
    }

    public void setId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }

    public List<RentalRate> getRentalRateList() {
        return rentalRateList;
    }

    public void setRentalRateList(List<RentalRate> rentalRateList) {
        this.rentalRateList = rentalRateList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Outlet getDepartureOutlet() {
        return departureOutlet;
    }

    public void setDepartureOutlet(Outlet departureOutlet) {
        this.departureOutlet = departureOutlet;
    }

    public Outlet getDestinationOutlet() {
        return destinationOutlet;
    }

    public void setDestinationOutlet(Outlet destinationOutlet) {
        this.destinationOutlet = destinationOutlet;
    }

    public TransitDriverDispatch getTransitDriverDispatch() {
        return transitDriverDispatch;
    }

    public void setTransitDriverDispatch(TransitDriverDispatch transitDriverDispatch) {
        this.transitDriverDispatch = transitDriverDispatch;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + this.reservationId +  ((this.carCategory == null) ? ", Category: " + this.carModel.getCarCategory().getCarCategoryName() : ", Category: " + this.carCategory.getCarCategoryName())
                + "\nPickup Time: " + this.pickupTime.toString().replace("T", ", ")
                + " from " + this.departureOutlet.getAddress()
                + "\nReturn Time: " + this.returnTime.toString().replace("T", ", ")
                + " to " + this.destinationOutlet.getAddress();
    }

}
