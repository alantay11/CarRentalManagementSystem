/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Uni
 */
@Entity
public class RentalRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateRecordId;

    @Column(nullable = false, length = 32)
    private String rateName;
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal ratePerDay;
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime startDateTime;
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime endDateTime;
    @Column(nullable = false)
    private boolean enabled;    
    
    @OneToOne
    private CarCategory carCategory;

    public RentalRate() {
        this.enabled = true;
    }
    
    public RentalRate(String rateName, BigDecimal ratePerDay, LocalDateTime startDateTime, LocalDateTime endDateTime, CarCategory carCategory) {
        this.rateName = rateName;
        this.ratePerDay = ratePerDay;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.carCategory = carCategory;
        this.enabled = true;
    }
    
       
    public Long getRentalRateRecordId() {
        return rentalRateRecordId;
    }

    public void setRentalRateRecordId(Long rentalRateRecordId) {
        this.rentalRateRecordId = rentalRateRecordId;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public BigDecimal getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(BigDecimal ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateRecordId != null ? rentalRateRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateRecordId fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.rentalRateRecordId == null && other.rentalRateRecordId != null) || (this.rentalRateRecordId != null && !this.rentalRateRecordId.equals(other.rentalRateRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Rental Rate: " + this.rentalRateRecordId + " with name " + this.rateName + ", rate of $" + this.ratePerDay +
                ", start date of " + this.startDateTime.toString().replace("T", ", ") + " and end date of " + this.endDateTime.toString().replace("T", ", ") +
                " for category " + this.carCategory + ((enabled) ? "" : "DISABLED");
    }
}
