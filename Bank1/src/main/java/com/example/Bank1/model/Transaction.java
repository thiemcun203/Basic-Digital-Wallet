package com.example.Bank1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private Long sourceId;
    private Long targetId;
    private String targetBankId;
    private Long amount;
    private String status;

    public Transaction() {
        this.status = "Fail";
    }

    public Transaction(Long sourceId, Long targetId, String targetBankId, Long amount) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.targetBankId = targetBankId;
        this.amount = amount;
        this.status = "Fail";
    }

    public void setStatus(String status) {this.status = status;}
}
