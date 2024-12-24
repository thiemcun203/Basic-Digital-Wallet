package com.example.Bank1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    public Long getTransactionId() {
        return transactionId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetBankId() {
        return targetBankId;
    }

    public void setTargetBankId(String targetBankId) {
        this.targetBankId = targetBankId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
