/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
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
    private LocalDateTime pickupTime;
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime returnTime;

    @ManyToMany
    private List<RentalRate> rentalRateList;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Car car;

    @OneToOne
    private Outlet departureOutlet;
    @OneToOne
    private Outlet destinationOutlet;

    /* Not sure if this is necessary
    @OneToOne(optional = true)
    private TransitDriverDispatch transitDriverDispatch;
     */
    public Reservation() {
        this.rentalRateList = new ArrayList<>();
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
        return "Reservation with id " + this.reservationId + ", pick up time" + this.pickupTime.toString().replace("T", ", ") + ", return time " + this.returnTime.toString().replace("T", ", ");
    }

}
