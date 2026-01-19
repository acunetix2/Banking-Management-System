package com.bankingsystem.bankingsystem.model;

public enum UserRole {
    AOO("Account Opening Officer"),
    TELLER("Teller"),
    CUSTOMER("Customer");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
