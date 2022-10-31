/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
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
public class Outlet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;

    @Column(nullable = false, length = 32)
    private String address;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalTime openingTime;
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalTime closingTime;

    @OneToMany(mappedBy = "currentOutlet")
    private List<Car> carList;
    @OneToMany(mappedBy = "assignedOutlet")
    private List<Employee> employeeList;
    @OneToMany(mappedBy = "destinationOutlet")
    private List<TransitDriverDispatch> inboundTransitDriverDispatchList;

    public Outlet() {
        this.carList = new ArrayList<>();
        this.employeeList = new ArrayList<>();
        this.inboundTransitDriverDispatchList = new ArrayList<>();
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<TransitDriverDispatch> getInboundTransitDriverDispatchList() {
        return inboundTransitDriverDispatchList;
    }

    public void setInboundTransitDriverDispatchList(List<TransitDriverDispatch> inboundTransitDriverDispatchList) {
        this.inboundTransitDriverDispatchList = inboundTransitDriverDispatchList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof Outlet)) {
            return false;
        }
        Outlet other = (Outlet) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Outlet ID: " + outletId + " " + address + this.carList + this.employeeList + this.inboundTransitDriverDispatchList
                + this.openingTime + this.closingTime;
    }

}
