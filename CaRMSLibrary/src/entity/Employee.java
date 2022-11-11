/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumeration.EmployeeAccessRightEnum;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Uni
 */
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 2, max = 32)
    private String firstName;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 2, max = 32)
    private String lastName;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 2, max = 32)
    private String username;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 2, max = 32)
    private String password;
    @Enumerated(EnumType.STRING)
    @NotNull
    private EmployeeAccessRightEnum accessRight;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet assignedOutlet;

    @OneToOne(optional = true)
    private TransitDriverDispatch transitDriverDispatchRecord;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String username, String password, EmployeeAccessRightEnum accessRight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.accessRight = accessRight;
    }

    public Employee(String firstName, String lastName, String username, String password, EmployeeAccessRightEnum accessRight, Outlet assignedOutlet) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.accessRight = accessRight;
        this.assignedOutlet = assignedOutlet;
    }

    public Employee(String firstName, String lastName, String username, String password, EmployeeAccessRightEnum accessRight, Outlet assignedOutlet, TransitDriverDispatch transitDriverDispatch) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.accessRight = accessRight;
        this.assignedOutlet = assignedOutlet;
        this.transitDriverDispatchRecord = transitDriverDispatch;
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

    public EmployeeAccessRightEnum getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(EmployeeAccessRightEnum accessRight) {
        this.accessRight = accessRight;
    }

    public Outlet getAssignedOutlet() {
        return assignedOutlet;
    }

    public void setAssignedOutlet(Outlet assignedOutlet) {
        this.assignedOutlet = assignedOutlet;
    }

    public TransitDriverDispatch getTransitDriverDispatchRecord() {
        return transitDriverDispatchRecord;
    }

    public void setTransitDriverDispatchRecord(TransitDriverDispatch transitDriverDispatchRecord) {
        this.transitDriverDispatchRecord = transitDriverDispatchRecord;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Employee with id " + this.employeeId + ", name " + this.firstName + " " + this.lastName + ", username " + this.username + ", access right" + this.accessRight;
    }

}
