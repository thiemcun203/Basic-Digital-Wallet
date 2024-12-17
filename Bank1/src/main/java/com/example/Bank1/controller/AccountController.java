package com.example.Bank1.controller;

import com.example.Bank1.model.Account;
import com.example.Bank1.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Endpoint to get all accounts
    @GetMapping("/getAllAccounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    // Updated endpoint to create an account using @RequestBody
    @PostMapping("/createAccount")
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, Object> payload) {
        // Extract values from the request body
        Long customerId = Long.valueOf(payload.get("customerId").toString());
        Double balance = Double.valueOf(payload.get("balance").toString());
        String status = payload.get("status").toString();

        // Call service to create the account
        Account newAccount = accountService.createAccount(customerId, balance, status);
        return ResponseEntity.ok(newAccount);
    }

    // Endpoint to delete an account
    @DeleteMapping("/deleteAccount")
    public ResponseEntity<Void> deleteAccount(@RequestParam("accountId") Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to get details of an account
    @GetMapping("/getAccount")
    public ResponseEntity<Account> getAccount(@RequestParam("accountId") Long accountId) {
        Optional<Account> account = accountService.getAccount(accountId);
        if (account.isPresent()) {
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to add money to an account
    @PutMapping("/addMoney")
    public String addMoney(@RequestBody Map<String, Object> payload) {
        Long accountId = Long.valueOf(payload.get("accountId").toString());
        Long amount = Long.valueOf(payload.get("amount").toString());

        String result = "Fail";
        if (accountService.verifyAccount(accountId)) {
            accountService.addMoney(accountId, amount);
            result = "Success";
        }
        return result;
    }
}
