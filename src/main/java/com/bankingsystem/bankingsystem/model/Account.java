package com.bankingsystem.bankingsystem.model;

public class Account {

    private long account_no;
    private String account_type;
    private String account_name;
    private double account_balance;

    public Account() {}

    public Account(long account_no, String account_type, String account_name, double account_balance) {
        this.account_no = account_no;
        this.account_type = account_type;
        this.account_name = account_name;
        this.account_balance = account_balance;
    }

    public long getAccountNumber() {
        return account_no;
    }

    public void setAccountNumber(long account_no) {
        this.account_no = account_no;
    }

    public String getAccountType() {
        return account_type;
    }

    public void setAccountType(String account_type) {
        this.account_type = account_type;
    }

    public String getAccountName() {
        return account_name;
    }

    public void setAccountName(String account_name) {
        this.account_name = account_name;
    }

    public double getBalance() {
        return account_balance;
    }

    public void setAccountBalance(double account_balance) {
        this.account_balance = account_balance;
    }

    @Override
    public String toString() {
        return String.format("Account{account_no=%d, account_type='%s', account_name='%s', account_balance=%.2f}", account_no, account_type, account_name, account_balance);
    }
}

