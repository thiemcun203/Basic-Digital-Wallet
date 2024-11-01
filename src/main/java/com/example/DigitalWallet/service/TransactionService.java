package com.example.DigitalWallet.service;

import com.example.DigitalWallet.model.Account;
import com.example.DigitalWallet.model.Transaction;
import com.example.DigitalWallet.repository.AccountRepository;
import com.example.DigitalWallet.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Transaction createTransaction(Long sourceAccountId, Long targetAccountId, double amount) {
        Optional<Account> sourceAccountOpt = accountRepository.findById(sourceAccountId);
        Optional<Account> targetAccountOpt = accountRepository.findById(targetAccountId);

        Transaction transaction = new Transaction(sourceAccountId, targetAccountId, amount);

        if (sourceAccountOpt.isPresent() && targetAccountOpt.isPresent()) {
            Account sourceAccount = sourceAccountOpt.get();
            Account targetAccount = targetAccountOpt.get();

            if (sourceAccount.getBalance() >= amount) {
                sourceAccount.decreaseBalance(amount);
                targetAccount.increaseBalance(amount);

                accountRepository.save(sourceAccount);
                accountRepository.save(targetAccount);

                transaction.setStatus("Successful");
            } else {
                transaction.setStatus("Unsuccessful");
            }
        } else {
            transaction.setStatus("Unsuccessful");
        }
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }
}

