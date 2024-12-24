package com.example.Bank1.controller;

import com.example.Bank1.model.Transaction;
import com.example.Bank1.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("api/v1/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/getUserTransactions")
    public ResponseEntity<List<Transaction>> getUserTransactions(@RequestParam Long userId) {
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }    

    @GetMapping("/getAllTransactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/createTransaction")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Map<String, Object> payload) {
        Long sourceId = Long.valueOf(payload.get("sourceId").toString());
        Long targetId = Long.valueOf(payload.get("targetId").toString());
        String targetBankId = "Bank1";
        Long amount = Long.valueOf(payload.get("amount").toString());
    
        Transaction transaction = transactionService.creatTransaction(sourceId, targetId, targetBankId, amount);
        return ResponseEntity.ok(transaction);
    }    
}
