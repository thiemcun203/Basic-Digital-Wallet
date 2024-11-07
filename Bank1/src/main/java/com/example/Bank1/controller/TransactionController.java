package com.example.Bank1.controller;

import com.example.Bank1.model.Account;
import com.example.Bank1.model.Transaction;
import com.example.Bank1.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/getAllTransactions")
    public ResponseEntity<List<Transaction>> getAllAccounts() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/createTransaction")
    public ResponseEntity<Transaction> createTransaction(
            @RequestParam("sourceId") Long sourceId,
            @RequestParam("targetId") Long targetId,
            @RequestParam("targetBankId") String targetBankId,
            @RequestParam("amount") Long amount) {
        Transaction transaction = transactionService.creatTransaction(sourceId, targetId, targetBankId, amount);
        return ResponseEntity.ok(transaction);
    }

}
