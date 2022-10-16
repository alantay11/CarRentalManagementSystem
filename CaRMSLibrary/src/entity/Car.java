/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Andrea
 */
@Entity
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    @Column(nullable = false, length = 32)
    private String make;
    @Column(nullable = false, length = 32)
    private String model;
    @Column(nullable = false, length = 32)
    private String color;

    @ManyToOne
    private CarCategory category;

    @OneToMany(mappedBy = "car")
    private List<Reservation> reservationList;

    @OneToOne
    private RentalRate rentalRateRecord;
    @OneToOne
    private Outlet currentOutlet;
    @OneToOne
    private TransitDriverDispatch transitDriverDispatchRecord;

    public Car() {
    }

    public Car(String make, String model, String color, CarCategory category) {
        this.make = make;
        this.model = model;
        this.color = color;
        this.category = category;
    }

    public Car(String make, String model, String color, CarCategory category, Outlet currentOutlet) {
        this.make = make;
        this.model = model;
        this.color = color;
        this.category = category;
        this.currentOutlet = currentOutlet;
    }

    public Car(String make, String model, String color, CarCategory category, List<Reservation> reservationList, RentalRate rentalRateRecord, Outlet currentOutlet) {
        this.make = make;
        this.model = model;
        this.color = color;
        this.category = category;
        this.reservationList = reservationList;
        this.rentalRateRecord = rentalRateRecord;
        this.currentOutlet = currentOutlet;
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

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public Outlet getCurrentOutlet() {
        return currentOutlet;
    }

    public void setCurrentOutlet(Outlet currentOutlet) {
        this.currentOutlet = currentOutlet;
    }

    public RentalRate getRentalRateRecord() {
        return rentalRateRecord;
    }

    public void setRentalRateRecord(RentalRate rentalRateRecord) {
        this.rentalRateRecord = rentalRateRecord;
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
