package com.example.DigitalWallet.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.DigitalWallet.model.Account;
import com.example.DigitalWallet.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    public Account createAccount(Long userId, String userName, Long userPhoneNumber) {
        Account account = new Account(userId, userName, userPhoneNumber);
        return accountRepository.save(account);
    }

    public Optional<Account> updateAccountInformation(Long accountId, String newState, String newUserName, Long newUserPhoneNumber) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setState(newState);
            account.setUserName(newUserName);
            account.setUserPhoneNumber(newUserPhoneNumber);
            accountRepository.save(account);
        }
        return accountOpt;
    }

    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    public boolean verifyAccount(Long accountId) {
        return accountRepository.existsById(accountId);
    }

    public Optional<Account> findAccount(Long accountId) {
        return accountRepository.findById(accountId);
    }
}