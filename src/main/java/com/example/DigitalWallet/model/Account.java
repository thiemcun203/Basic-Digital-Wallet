package com.example.DigitalWallet.model;

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
    private double balance;
    private String state;
    @Column(unique = true, nullable = false)
    private Long userId;
    private String userName;
    private Long userPhoneNumber;

    public Account() {
        this.state = "open";
        this.balance = 0.0;
    }

    public Account(Long userId, String userName, Long userPhoneNumber) {
        this.userId = userId;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.state = "open";
        this.balance = 0.0;
    }

    public Long getAccountId() {return accountId;}

    public Long getUserId() {return userId;}

    public double getBalance() {return balance;}
    public void increaseBalance(double balance) {this.balance += balance;}
    public void decreaseBalance(double balance) {this.balance -= balance;}

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}

    public Long getUserPhoneNumber() {return userPhoneNumber;}
    public void setUserPhoneNumber(Long userPhoneNumber) {this.userPhoneNumber = userPhoneNumber;}
}