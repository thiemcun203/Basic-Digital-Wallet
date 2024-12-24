package com.example.Bank1.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Bank1.model.Account;
import com.example.Bank1.repository.AccountRepository;
import com.example.Bank1.model.Transaction;
import com.example.Bank1.repository.TransactionRepository;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, RestTemplate restTemplate) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.restTemplate = restTemplate;
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    public List<Account> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }    

    public Account createAccount(Long customerId, Double balance, String status) {
        Account account = new Account(customerId, balance, status);
        return accountRepository.save(account);
    }

    public Optional<Account> closeAccount(Long accountId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setStatus("Close");
            accountRepository.save(account);
        }
        return accountOpt;
    }

    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    public Optional<Account> getAccount(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public boolean verifyAccount(Long accountId) {
        return accountRepository.existsById(accountId);
    }

    public boolean checkBalance(Long accountId, Long amount) {
        Account account = accountRepository.findById(accountId).get();
        return (account.getBalance() >= amount);
    }

    public void addMoney(Long accountId, Long amount) {
        Account account = accountRepository.findById(accountId).get();
        account.increaseBalance(amount);
        accountRepository.save(account);
    }

    public String addMoneyWithTransaction(Long accountId, Long amount) {
        String result = "Fail";
        if (verifyAccount(accountId)) {
            // Add money to the account
            Account account = getAccount(accountId).get();
            account.increaseBalance(amount);
            accountRepository.save(account);
    
            // Create a transaction record for "Add Funds"
            Transaction transaction = new Transaction();
            transaction.setSourceId(null); // No source account for "add funds"
            transaction.setTargetId(accountId);
            transaction.setTargetBankId("Bank1");
            transaction.setAmount(amount);
            transaction.setStatus("Success"); // Mark the transaction as successful
            transactionRepository.save(transaction);
    
            result = "Success";
        }
        return result;
    }    

    public String requestAddMoney(Long accountId, Long amount) {
        String url = String.format("http://localhost:8081/account/addMoney?accountId=%d&amount=%d", accountId, amount);
        String response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class).getBody();
        return response;
    }
}