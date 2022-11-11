/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Andrea
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 2, max = 32)
    private String firstName;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 2, max = 32)
    private String lastName;
    @Column(nullable = false, length = 32, unique = true)
    @NotNull
    @Size(min = 1, max = 32)
    private String email;
    @Column(nullable = false, length = 8)
    @NotNull
    @Size(min = 8, max = 8)
    private String contactNumber;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 2, max = 32)
    private String addressLine1;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 2, max = 32)
    private String addressLine2;
    @Column(nullable = false, length = 12)
    @NotNull
    @Size(min = 4, max = 12)
    private String postalCode;
    @Column(nullable = false, length = 32, unique = true)
    @NotNull
    @Size(min = 2, max = 32)
    private String username;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 2, max = 32)
    private String password;

    @OneToOne(optional = true)
    private CreditCard creditCard;

    @OneToMany(mappedBy = "customer")
    private List<Reservation> reservationList;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Partner partner;

    public Customer() {
        this.reservationList = new ArrayList<>();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Long getId() {
        return customerId;
    }

    public void setId(Long id) {
        this.customerId = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Customer: " + this.customerId + " with name " + this.firstName + " " + this.lastName + ", email of " + this.email
                + ", contact number of " + this.contactNumber + ", address of " + this.addressLine1 + "\n" + this.addressLine2
                + ", username of " + this.username + " and password " + this.password;
    }

}
