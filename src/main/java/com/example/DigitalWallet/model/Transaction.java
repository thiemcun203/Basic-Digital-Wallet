package com.example.DigitalWallet.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Long sourceAccountId;
    private Long targetAccountId;
    private double amount;
    private String status;
    private LocalDateTime timestamp;

    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(Long sourceAccountId, Long targetAccountId, double amount) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public Long getTransactionId() { return transactionId; }

    public Long getSourceAccountId() { return sourceAccountId; }

    public Long getTargetAccountId() { return targetAccountId; }

    public double getAmount() { return amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
}