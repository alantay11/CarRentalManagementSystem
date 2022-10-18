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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Uni
 */
@Entity
public class CarModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carModelId;

    @Column(nullable = false, length = 32)
    private String make;
    @Column(nullable = false, length = 32)
    private String model;
    @Column(nullable = false)
    private boolean enabled;
    
    @OneToMany(mappedBy = "model")
    private List<Car> carList;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarCategory carCategory;


    public CarModel() {
        this.enabled = true;
    }

    public CarModel(String make, String model) {
        this.make = make;
        this.model = model;
    }
    
    

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public Long getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(Long carModelId) {
        this.carModelId = carModelId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carModelId != null ? carModelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carModelId fields are not set
        if (!(object instanceof CarModel)) {
            return false;
        }
        CarModel other = (CarModel) object;
        if ((this.carModelId == null && other.carModelId != null) || (this.carModelId != null && !this.carModelId.equals(other.carModelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Model[ id=" + carModelId + " ]";
    }

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

}
