package com.bankingsystem.bankingsystem.repository;

import com.bankingsystem.bankingsystem.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Account> accountRowMapper = (rs, rowNum) -> {
        Account account = new Account();
        account.setAccountNumber(rs.getLong("account_no"));
        account.setAccountType(rs.getString("account_type"));
        account.setAccountName(rs.getString("account_name"));
        account.setAccountBalance(rs.getDouble("account_balance"));
        return account;
    };

    public void save(Account account) {
        String sql = "INSERT INTO accounts (account_no, account_type, account_name, account_balance) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, account.getAccountNumber(), account.getAccountType(), 
                          account.getAccountName(), account.getBalance());
    }

    public Optional<Account> findById(long accountNo) {
        String sql = "SELECT * FROM accounts WHERE account_no = ?";
        List<Account> accounts = jdbcTemplate.query(sql, accountRowMapper, accountNo);
        return accounts.isEmpty() ? Optional.empty() : Optional.of(accounts.get(0));
    }

    public List<Account> findAll() {
        String sql = "SELECT * FROM accounts";
        return jdbcTemplate.query(sql, accountRowMapper);
    }

    public void deleteById(long accountNo) {
        String sql = "DELETE FROM accounts WHERE account_no = ?";
        jdbcTemplate.update(sql, accountNo);
    }

    public void update(Account account) {
        String sql = "UPDATE accounts SET account_type = ?, account_name = ?, account_balance = ? WHERE account_no = ?";
        jdbcTemplate.update(sql, account.getAccountType(), account.getAccountName(), 
                          account.getBalance(), account.getAccountNumber());
    }

    public List<Account> findByName(String accountName) {
        String sql = "SELECT * FROM accounts WHERE account_name LIKE ?";
        return jdbcTemplate.query(sql, accountRowMapper, "%" + accountName + "%");
    }
}

