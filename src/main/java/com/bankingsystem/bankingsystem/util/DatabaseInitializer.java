package com.bankingsystem.bankingsystem.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        createTablesIfNotExist();
        addMissingColumns();
    }
//SQL portable statements to create tables
    private void createTablesIfNotExist() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(100) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "email VARCHAR(255)," +
                "full_name VARCHAR(255)," +
                "role VARCHAR(50))";

        String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_no BIGINT PRIMARY KEY," +
                "account_type VARCHAR(50)," +
                "account_name VARCHAR(255)," +
                "account_balance DOUBLE," +
                "created_by INT," +
                "FOREIGN KEY (created_by) REFERENCES users(user_id))";

        try {
            jdbcTemplate.execute(createUsersTable);
            jdbcTemplate.execute(createAccountsTable);
        } catch (Exception e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    private void addMissingColumns() {
        try {
            String checkRoleColumn = "SELECT COLUMN_NAME FROM information_schema.COLUMNS " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users' AND COLUMN_NAME = 'role'";
            try {
                String result = jdbcTemplate.queryForObject(checkRoleColumn, String.class);
                if (result == null) {
                    String addRoleColumn = "ALTER TABLE users ADD COLUMN role VARCHAR(50)";
                    jdbcTemplate.execute(addRoleColumn);
                    System.out.println("Added role column to users table");
                }
            } catch (Exception e) {
                String addRoleColumn = "ALTER TABLE users ADD COLUMN role VARCHAR(50)";
                jdbcTemplate.execute(addRoleColumn);
                System.out.println("Added role column to users table");
            }

            String checkCreatedByColumn = "SELECT COLUMN_NAME FROM information_schema.COLUMNS " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'accounts' AND COLUMN_NAME = 'created_by'";
            try {
                String result = jdbcTemplate.queryForObject(checkCreatedByColumn, String.class);
                if (result == null) {
                    String addCreatedByColumn = "ALTER TABLE accounts ADD COLUMN created_by INT";
                    jdbcTemplate.execute(addCreatedByColumn);
                    System.out.println("Added created_by column to accounts table");
                }
            } catch (Exception e) {
                String addCreatedByColumn = "ALTER TABLE accounts ADD COLUMN created_by INT";
                jdbcTemplate.execute(addCreatedByColumn);
                System.out.println("Added created_by column to accounts table");
            }

            String checkAccountNoType = "SELECT COLUMN_TYPE FROM information_schema.COLUMNS " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'accounts' AND COLUMN_NAME = 'account_no'";
            try {
                String columnType = jdbcTemplate.queryForObject(checkAccountNoType, String.class);
                if (columnType != null && columnType.contains("int")) {
                    String modifyAccountNo = "ALTER TABLE accounts MODIFY COLUMN account_no BIGINT";
                    jdbcTemplate.execute(modifyAccountNo);
                }
            } catch (Exception e) {
                System.out.println("account_no column already BIGINT or table not found");
            }
        } catch (Exception e) {
            System.err.println("Error adding missing columns: " + e.getMessage());
        }
    }
}
