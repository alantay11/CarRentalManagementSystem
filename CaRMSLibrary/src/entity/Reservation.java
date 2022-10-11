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
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    
    @Column(nullable = false, length = 32)
    private String driverName;
    @Column(nullable = false, length = 16)
    private String car;
    @Column
    private Instant pickUpTime;
    @Column
    private Instant endTime;
    @Column(nullable = false, length = 32)
    private String rentalRec;
    @Column(nullable = false, length = 32)
    private Boolean employeeTransportAvail;
    @Column(nullable = false, length = 32)
    private String startLocation;
    @Column(nullable = false, length = 32)
    private String endLocation;
    
    

    public Long getreservationId() {
        return reservationId;
    }

    public void setId(Long reservationId) {
        this.reservationId = reservationId;
    }
    
    public Reservation() {
    }

    public Reservation(Long reservationId, String driverName, String car, Instant pickUpTime, Instant endTime, String rentalRec, Boolean employeeTransportAvail, String startLocation, String endLocation) {
        this.reservationId = reservationId;
        this.driverName = driverName;
        this.car = car;
        this.pickUpTime = pickUpTime;
        this.endTime = endTime;
        this.rentalRec = rentalRec;
        this.employeeTransportAvail = employeeTransportAvail;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }
    
}
