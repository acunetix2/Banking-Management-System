package com.bankingsystem.bankingsystem.service;

import com.bankingsystem.bankingsystem.model.Account;
import com.bankingsystem.bankingsystem.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class BankService {

    private final AccountRepository repository;
    private final Random random = new Random();

    public BankService(AccountRepository repository) {
        this.repository = repository;
    }

    public long generateAccountNumber() {
        long accountNo;
        do {
            accountNo = 1100000000000L + random.nextLong(900000000000L);
        } while (repository.findById((int) accountNo).isPresent());
        return accountNo;
    }

    public String createAccount(Account account) {
        try {
            repository.save(account);
            return "Account created successfully! Account Number: " + account.getAccountNumber();
        } catch (Exception e) {
            return "Error creating account: " + e.getMessage();
        }
    }

    public String deposit(long accountNo, double amount) {
        if (amount <= 0) {
            return "Deposit amount must be positive!";
        }
        Optional<Account> optional = repository.findById(accountNo);
        if (optional.isPresent()) {
            Account acc = optional.get();
            acc.setAccountBalance(acc.getBalance() + amount);
            repository.update(acc);
            return "Deposit successful. New balance: " + acc.getBalance();
        }
        return "Account not found!";
    }

    public String withdraw(long accountNo, double amount) {
        if (amount <= 0) {
            return "Withdrawal amount must be positive!";
        }
        Optional<Account> optional = repository.findById(accountNo);
        if (optional.isPresent()) {
            Account acc = optional.get();
            if (acc.getBalance() >= amount) {
                acc.setAccountBalance(acc.getBalance() - amount);
                repository.update(acc);
                return "Withdrawal successful. New balance: " + acc.getBalance();
            } else {
                return "Insufficient funds! Current balance: " + acc.getBalance();
            }
        }
        return "Account not found!";
    }

    public String transfer(long fromAccountNo, long toAccountNo, double amount) {
        if (amount <= 0) {
            return "Transfer amount must be positive!";
        }
        if (fromAccountNo == toAccountNo) {
            return "Cannot transfer to the same account!";
        }
        
        Optional<Account> fromOptional = repository.findById(fromAccountNo);
        Optional<Account> toOptional = repository.findById(toAccountNo);
        
        if (!fromOptional.isPresent()) {
            return "Source account not found!";
        }
        if (!toOptional.isPresent()) {
            return "Destination account not found!";
        }
        
        Account fromAccount = fromOptional.get();
        Account toAccount = toOptional.get();
        
        if (fromAccount.getBalance() < amount) {
            return "Insufficient funds! Available balance: " + fromAccount.getBalance();
        }
        
        fromAccount.setAccountBalance(fromAccount.getBalance() - amount);
        toAccount.setAccountBalance(toAccount.getBalance() + amount);
        
        repository.update(fromAccount);
        repository.update(toAccount);
        
        return "Transfer successful! " + amount + " transferred from " + fromAccountNo + 
               " to " + toAccountNo + ". New balance: " + fromAccount.getBalance();
    }

    public Optional<Account> getAccount(long accountNo) {
        return repository.findById(accountNo);
    }

    public List<Account> searchAccountByName(String name) {
        return repository.findByName(name);
    }

    public void deleteAccount(long accountNo) {
        repository.deleteById(accountNo);
    }
}

