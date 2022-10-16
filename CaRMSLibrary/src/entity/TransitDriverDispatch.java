/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Uni
 */
@Entity
public class TransitDriverDispatch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDriverDispatchId;
    
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime pickupTime;    
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime dropoffTime;
    
    @OneToOne(mappedBy = "transitDriverDispatchRecord")
    private Car car;
    @OneToOne(mappedBy = "transitDriverDispatchRecord")
    private Employee employee;
    @ManyToOne
    private Outlet departureOutlet;
    @ManyToOne
    private Outlet destinationOutlet;
    

    public TransitDriverDispatch() {
    }

    public TransitDriverDispatch(LocalDateTime pickupTime, LocalDateTime dropoffTime, Car car, Employee employee, Outlet departureOutlet, Outlet destinationOutlet) {
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
        this.car = car;
        this.employee = employee;
        this.departureOutlet = departureOutlet;
        this.destinationOutlet = destinationOutlet;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDateTime getDropoffTime() {
        return dropoffTime;
    }

    public void setDropoffTime(LocalDateTime dropoffTime) {
        this.dropoffTime = dropoffTime;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
    
    
    

    public Long getTransitDriverDispatchId() {
        return transitDriverDispatchId;
    }

    public void setTransitDriverDispatchId(Long transitDriverDispatchId) {
        this.transitDriverDispatchId = transitDriverDispatchId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDriverDispatchId != null ? transitDriverDispatchId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitDriverDispatchId fields are not set
        if (!(object instanceof TransitDriverDispatch)) {
            return false;
        }
        TransitDriverDispatch other = (TransitDriverDispatch) object;
        if ((this.transitDriverDispatchId == null && other.transitDriverDispatchId != null) || (this.transitDriverDispatchId != null && !this.transitDriverDispatchId.equals(other.transitDriverDispatchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatch[ id=" + transitDriverDispatchId + " ]";
    }
    
}
