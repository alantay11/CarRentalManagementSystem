/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Andrea
 */
@Entity
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long carId;
    
    @Column(nullable = false, length = 32)
    private CarCategory category;
    @Column(nullable = false, length = 32)
    private String make;
    @Column(nullable = false, length = 32)
    private String model;
    @Column(nullable = false, length = 32)
    private String color;
    @Column(nullable = false, length = 32)
    private Reservation rentalAvail;
    @Column(nullable = false, length = 32)
    private RentalRateRec rentalRateRecord;
    @Column(nullable = false, length = 32)
    private Outlet outlet;

    
    public Long getCarId() {
        return carId;
    }
    
    public Car() {
    }

    public Car(Long carId, CarCategory category, String make, String model, String color, Reservation rentalAvail, RentalRateRec rentalRateRecord, Outlet outlet) {
        this.carId = carId;
        this.category = category;
        this.make = make;
        this.model = model;
        this.color = color;
        this.rentalAvail = rentalAvail;
        this.rentalRateRecord = rentalRateRecord;
        this.outlet = outlet;
    }
    
    

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Car[ id=" + carId + " ]";
    }
    
}
