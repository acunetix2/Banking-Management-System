package com.bankingsystem.bankingsystem.util;

public class InputValidation {

    public static boolean isValidAccountName(String name) {
        if (name == null) return false;
        String trimmed = name.trim();
        return !trimmed.isEmpty() && trimmed.matches("^[a-zA-Z\\s'-]+$");
    }

    public static boolean isValidAccountType(String type) {
        if (type == null) return false;
        String trimmed = type.trim().toLowerCase();
        return trimmed.equals("savings") || trimmed.equals("checking");
    }

    public static boolean isValidAccountNumber(int number) {
        return number > 0;
    }

    public static boolean isValidAmount(double amount) {
        return amount >= 0; 
    }
}
