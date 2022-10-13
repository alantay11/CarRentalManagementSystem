/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
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
    private String make;
    @Column(nullable = false, length = 32)
    private String model;
    @Column(nullable = false, length = 32)
    private String color;
    
    @Column(nullable = false, length = 32)
    private CarCategory category;
    
    // not sure whether want to use the class or not, maybe map them
    @Column(nullable = false, length = 32)
    private Reservation rentalAvail;
    @Column(nullable = false, length = 32)
    private RentalRateRecord rentalRateRecord;
    
    @Column(nullable = false, length = 32)
    private Outlet outlet;

    public Car() {
    }

    public Car(CarCategory category, String make, String model, String color, Reservation rentalAvail, RentalRateRecord rentalRateRecord, Outlet outlet) {
        this.category = category;
        this.make = make;
        this.model = model;
        this.color = color;
        this.rentalAvail = rentalAvail;
        this.rentalRateRecord = rentalRateRecord;
        this.outlet = outlet;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Reservation getRentalAvail() {
        return rentalAvail;
    }

    public void setRentalAvail(Reservation rentalAvail) {
        this.rentalAvail = rentalAvail;
    }

    public RentalRateRecord getRentalRateRecord() {
        return rentalRateRecord;
    }

    public void setRentalRateRecord(RentalRateRecord rentalRateRecord) {
        this.rentalRateRecord = rentalRateRecord;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
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
