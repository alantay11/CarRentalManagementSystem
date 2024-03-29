/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumeration.RentalRateEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @NotNull
    @Size(min = 2, max = 32)
    private String rateName;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @Digits(integer = 6, fraction = 2)
    private BigDecimal ratePerDay;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startDate;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endDate;
    @Column(nullable = false)
    @NotNull
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private RentalRateEnum rateType;

    @ManyToOne
    @JoinColumn(nullable = false)
    private CarCategory carCategory;

    @ManyToMany(mappedBy = "rentalRateList")
    private List<Reservation> reservationList;

    public RentalRate() {
        this.enabled = true;
        this.reservationList = new ArrayList<>();
    }

    public RentalRateEnum getRateType() {
        return rateType;
    }

    public void setRateType(RentalRateEnum rateType) {
        this.rateType = rateType;
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
        return "Rental Rate ID: " + this.rentalRateRecordId + ", Name: " + this.rateName + ", Rate: $" + this.ratePerDay
                + "\n"
                + ((startDate == null) ? "Always valid" : ("Starts: " + this.startDate.toString().replace("T", ", ") + ", Ends: " + this.endDate.toString().replace("T", ", ")))
                //+ " for category " + this.carCategory 
                + "\n"
                + ((enabled) ? "ENABLED" : "DISABLED");
    }
}
