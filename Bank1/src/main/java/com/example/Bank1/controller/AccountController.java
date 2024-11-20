package com.example.Bank1.controller;

import com.example.Bank1.model.Account;
import com.example.Bank1.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/getAllAccounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/createAccount")
    public ResponseEntity<Account> createAccount(
            @RequestParam("customerId") Long customerId) {
        Account newAccount = accountService.createAccount(customerId);
        return ResponseEntity.ok(newAccount);
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<Void> deleteAccount(@RequestParam("accountId") Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAccount")
    public ResponseEntity<Account> getAccount(@RequestParam("accountId") Long accountId) {
        Optional<Account> account = accountService.getAccount(accountId);
        if (account.isPresent()) {return ResponseEntity.ok(account.get());}
        else {return ResponseEntity.notFound().build();}
    }

    @PutMapping("/addMoney")
    public String addMoney(
            @RequestParam("accountId") Long accountId,
            @RequestParam("amount") Long amount) {
        String check = "Fail";
        if (accountService.verifyAccount(accountId)) {
            accountService.addMoney(accountId, amount);
            check = "Success";
        }
        return check;
    }
}