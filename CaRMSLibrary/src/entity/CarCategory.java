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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Uni
 */
@Entity
public abstract class CarCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long carCategoryId;
    
    @Column(nullable = false, length = 32)
    private String carCategoryName;
    
    
    @OneToMany(mappedBy = "Car")
    private List<Car> carList;
    
    @OneToOne
    private RentalRate rentalRateRecord;

    public CarCategory() {
    }

    public CarCategory(String carCategoryName) {
        this.carCategoryName = carCategoryName;
    }

    public CarCategory(String carCategoryName, List<Car> carList) {
        this.carCategoryName = carCategoryName;
        this.carList = carList;
    }

    public CarCategory(String carCategoryName, List<Car> carList, RentalRate rentalRateRecord) {
        this.carCategoryName = carCategoryName;
        this.carList = carList;
        this.rentalRateRecord = rentalRateRecord;
    }

    public String getCarCategoryName() {
        return carCategoryName;
    }

    public void setCarCategoryName(String carCategoryName) {
        this.carCategoryName = carCategoryName;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public RentalRate getRentalRateRecord() {
        return rentalRateRecord;
    }

    public void setRentalRateRecord(RentalRate rentalRateRecord) {
        this.rentalRateRecord = rentalRateRecord;
    }
    
    

    public Long getCarCategoryId() {
        return carCategoryId;
    }

    public void setCarCategoryId(Long carCategoryId) {
        this.carCategoryId = carCategoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carCategoryId != null ? carCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carCategoryId fields are not set
        if (!(object instanceof CarCategory)) {
            return false;
        }
        CarCategory other = (CarCategory) object;
        if ((this.carCategoryId == null && other.carCategoryId != null) || (this.carCategoryId != null && !this.carCategoryId.equals(other.carCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarCategory[ id=" + carCategoryId + " ]";
    }
    
}
