package com.example.Bank1.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Bank1.model.Account;
import com.example.Bank1.repository.AccountRepository;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public AccountService(AccountRepository accountRepository, RestTemplate restTemplate) {
        this.accountRepository = accountRepository;
        this.restTemplate = restTemplate;
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    public Account createAccount(Long customerId) {
        Account account = new Account(customerId);
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

    public String requestAddMoney(Long accountId, Long amount) {
        String url = String.format("http://localhost:8081/account/addMoney?accountId=%d&amount=%d", accountId, amount);
        String response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class).getBody();
        return response;
    }
}