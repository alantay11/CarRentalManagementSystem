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
import javax.persistence.Temporal;

/**
 *
 * @author Uni
 */
@Entity
public class CreditCard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ccId;

    @Column(nullable = false, length = 32)
    private String nameonCC;
    @Column(nullable = false, length = 16)
    private String ccNumber;
    @Column(nullable = false, length = 3)
    private String cvv;
    @Column
    private Instant expiryDate;

    public Long getCcId() {
        return ccId;
    }

    public CreditCard() {
    }

    public CreditCard(String nameonCC, String ccNumber, String cvv, Instant expiryDate) {
        this.nameonCC = nameonCC;
        this.ccNumber = ccNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    public void setCcId(Long ccId) {
        this.ccId = ccId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ccId != null ? ccId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the ccId fields are not set
        if (!(object instanceof CreditCard)) {
            return false;
        }
        CreditCard other = (CreditCard) object;
        if ((this.ccId == null && other.ccId != null) || (this.ccId != null && !this.ccId.equals(other.ccId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditCard[ id=" + ccId + " ]";
    }

}
