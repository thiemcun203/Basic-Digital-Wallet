package com.example.DigitalWallet.repository;

import com.example.DigitalWallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {}