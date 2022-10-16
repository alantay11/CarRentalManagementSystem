/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumeration.CarStatusEnum;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    private String licensePlateNum;
    @Column(nullable = false, length = 32)
    private String color;
    @Enumerated(EnumType.STRING)
    private CarStatusEnum carStatus;

    @ManyToOne
    private CarCategory category;
    @ManyToOne
    private Model model;

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

    public String getLicensePlateNum() {
        return licensePlateNum;
    }

    public void setLicensePlateNum(String licensePlateNum) {
        this.licensePlateNum = licensePlateNum;
    }

    public CarStatusEnum getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(CarStatusEnum carStatus) {
        this.carStatus = carStatus;
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

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public TransitDriverDispatch getTransitDriverDispatchRecord() {
        return transitDriverDispatchRecord;
    }

    public void setTransitDriverDispatchRecord(TransitDriverDispatch transitDriverDispatchRecord) {
        this.transitDriverDispatchRecord = transitDriverDispatchRecord;
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
