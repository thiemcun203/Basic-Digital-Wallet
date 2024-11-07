package com.example.Bank1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private Long customerId;
    private double balance;
    private String status;

    public Account() {
        this.status = "Open";
        this.balance = 0.0;
    }

    public Account(Long userId) {
        this.customerId = userId;
        this.status = "Open";
        this.balance = 0.0;
    }

    public Long getAccountId() {return this.accountId;}

    public Long getCustomerId() {return this.customerId;}

    public double getBalance() {return balance;}
    public void increaseBalance(double balance) {this.balance += balance;}
    public void decreaseBalance(double balance) {this.balance -= balance;}

    public String getStatus() {return this.status;}
    public void setStatus(String status) {this.status = status;}
}