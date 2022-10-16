/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Uni
 */
@Entity
public class PartnerCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerCustomerId;

    @OneToMany(mappedBy = "partnerCustomerList")
    private Partner partner;

    public PartnerCustomer() {
    }

    public Long getPartnerCustomerId() {
        return partnerCustomerId;
    }

    public void setPartnerCustomerId(Long partnerCustomerId) {
        this.partnerCustomerId = partnerCustomerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerCustomerId != null ? partnerCustomerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerCustomerId fields are not set
        if (!(object instanceof PartnerCustomer)) {
            return false;
        }
        PartnerCustomer other = (PartnerCustomer) object;
        if ((this.partnerCustomerId == null && other.partnerCustomerId != null) || (this.partnerCustomerId != null && !this.partnerCustomerId.equals(other.partnerCustomerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PartnerCustomer[ id=" + partnerCustomerId + " ]";
    }

}
