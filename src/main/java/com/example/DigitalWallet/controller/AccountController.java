package com.example.DigitalWallet.controller;

import com.example.DigitalWallet.model.Account;
import com.example.DigitalWallet.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @RequestParam Long userId,
            @RequestParam String userName,
            @RequestParam Long userPhoneNumber) {
        Account newAccount = accountService.createAccount(userId, userName, userPhoneNumber);
        return ResponseEntity.ok(newAccount);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccountInformation(
            @PathVariable Long accountId,
            @RequestParam String newState,
            @RequestParam String newUserName,
            @RequestParam Long newUserPhoneNumber) {
        Optional<Account> updatedAccount = accountService.updateAccountInformation(accountId, newState, newUserName, newUserPhoneNumber);
        return updatedAccount.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{accountId}/verify")
    public ResponseEntity<Boolean> verifyAccount(@PathVariable Long accountId) {
        boolean exists = accountService.verifyAccount(accountId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> findAccountById(@PathVariable Long accountId) {
        Optional<Account> account = accountService.findAccount(accountId);
        if (account.isPresent()) {
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/{accountId}")
//    public String findAccountById(@PathVariable Long accountId) {
//        Optional<Account> account = accountService.findAccount(accountId);
//        return "Done";
//    }
}