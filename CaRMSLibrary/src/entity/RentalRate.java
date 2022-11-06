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
    private LocalDateTime startDate;
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime endDate;
    @Column(nullable = false)
    private boolean enabled;

    @ManyToOne
    @JoinColumn(nullable = false)
    private CarCategory carCategory;

    @ManyToMany(mappedBy = "rentalRateList")
    private List<Reservation> reservationList;

    public RentalRate() {
        this.enabled = true;
        this.reservationList = new ArrayList<>();
    }

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
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
        return "Rental Rate: " + this.rentalRateRecordId + " with name " + this.rateName + ", rate of $" + this.ratePerDay
                + ", start date of " + this.startDate.toString().replace("T", ", ") + " and end date of " + this.endDate.toString().replace("T", ", ")
                //+ " for category " + this.carCategory 
                + ((enabled) ? "" : "DISABLED");
    }
}
