package com.example.Bank1.service;

import com.example.Bank1.model.Account;
import com.example.Bank1.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Bank1.model.Transaction;
import com.example.Bank1.repository.TransactionRepository;

import java.util.stream.Collectors;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        // Fetch all accounts associated with the user
        List<Account> userAccounts = accountRepository.findByCustomerId(userId);

        // Extract all account IDs for the user
        List<Long> accountIds = userAccounts.stream()
                                             .map(Account::getAccountId)
                                             .collect(Collectors.toList());

        // Fetch transactions where sourceId or targetId matches any of the user's account IDs
        return transactionRepository.findBySourceIdInOrTargetIdIn(accountIds, accountIds);
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions;
    }

    public Transaction creatTransaction(Long sourceId, Long targetId, String targetBankId, Long amount) {
        Transaction transaction = new Transaction(sourceId, targetId, targetBankId, amount);
        String check;
        if ("Bank1".equals(targetBankId)) {check = internalTransact(sourceId, targetId, amount);}
        else if ("Bank2".equals(targetBankId)) {check = externalTransact(sourceId, targetId, amount);}
        else {check = "Unknown bank";}
        transaction.setStatus(check);
        return transactionRepository.save(transaction);
    }

    public String internalTransact(Long sourceId, Long targetId, Long amount) {
        String check = "Fail";
        if (accountService.verifyAccount(targetId) && accountService.checkBalance(sourceId, amount)) {
            Account sourceAccount = accountService.getAccount(sourceId).get();
            sourceAccount.decreaseBalance(amount);
            accountRepository.save(sourceAccount);

            Account targetAccount = accountService.getAccount(targetId).get();
            targetAccount.increaseBalance(amount);
            accountRepository.save(targetAccount);

            check = "Success";
        }
        return check;
    }

    public String externalTransact(Long sourceId, Long targetId, Long amount) {
        String check = "Fail";
        if (accountService.checkBalance(sourceId, amount)) {
            check = accountService.requestAddMoney(targetId, amount);
        }
        if (check == "Success") {
            Account sourceAccount = accountService.getAccount(sourceId).get();
            sourceAccount.decreaseBalance(amount);
            accountRepository.save(sourceAccount);
        }
        return check;
    }
}