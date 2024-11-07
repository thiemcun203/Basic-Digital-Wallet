package com.example.Bank1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "customer")
public class Customer{
    @Id
    @Column(unique = true, nullable = false)
    private Long customerId;
    private String customerName;
    private Long customerPhoneNumber;

    public Customer() {
    }

    public Customer(Long customerId, String customerName, Long customerPhoneNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public Long getCustomerId() {return this.customerId;}
    public void setCustomerId(Long customerId) {this.customerId = customerId;}

    public String getCustomerName() {return this.customerName;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}

    public Long getCustomerPhoneNumber() {return this.customerPhoneNumber;}
    public void setCustomerPhoneNumber(Long customerPhoneNumber) {this.customerPhoneNumber = customerPhoneNumber;}
}
