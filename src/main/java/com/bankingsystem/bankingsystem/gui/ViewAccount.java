package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.model.Account;
import com.bankingsystem.bankingsystem.service.BankService;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class ViewAccount extends JFrame {
    private BankService bankService;
    private JTextField txtSearch;
    private JTextArea txtResult;

    public ViewAccount(BankService bankService) {
        this.bankService = bankService;

        setTitle("View Account");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        createUI();
    }

    private void createUI() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(240, 240, 240));

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(33, 150, 243));
        header.setPreferredSize(new Dimension(0, 60));
        JLabel titleLabel = new JLabel("View Account Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel);

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel searchLabel1 = new JLabel("Search by name or a/c no:");
        searchLabel1.setFont(new Font("Arial", Font.PLAIN, 12));
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(33, 150, 243));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(e -> searchAccount());
        
        searchPanel.add(searchLabel1);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Results Panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(new Color(240, 240, 240));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JLabel resultLabel = new JLabel("Account Information:");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 12));

        txtResult = new JTextArea();
        txtResult.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtResult.setEditable(false);
        txtResult.setLineWrap(true);
        txtResult.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtResult);

        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        // Close Button
        JPanel footer = new JPanel();
        footer.setBackground(new Color(240, 240, 240));
        JButton btnClose = new JButton("Close");
        btnClose.setBackground(new Color(200, 200, 200));
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> dispose());
        footer.add(btnClose);

        // Center panel combining search and results
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(resultPanel, BorderLayout.CENTER);

        main.add(header, BorderLayout.NORTH);
        main.add(centerPanel, BorderLayout.CENTER);
        main.add(footer, BorderLayout.SOUTH);

        add(main);
    }

    private void searchAccount() {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter account number or name", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            StringBuilder result = new StringBuilder();
            //search by account no
            try {
                long accountNo = Long.parseLong(searchText);
                Optional<Account> account = bankService.getAccount(accountNo);
                if (account.isPresent()) {
                    Account acc = account.get();
                    result.append("Account Number: ").append(acc.getAccountNumber()).append("\n");
                    result.append("Account Holder: ").append(acc.getAccountName()).append("\n");
                    result.append("Account Type: ").append(acc.getAccountType()).append("\n");
                    result.append("Balance: Ksh: ").append(String.format("%.2f", acc.getBalance())).append("\n");
                } else {
                    result.append("Account not found."); 
                }
            } catch (NumberFormatException e) {
                // Search by name using generic classes
                java.util.List<Account> accounts = bankService.searchAccountByName(searchText);
                if (accounts.isEmpty()) {
                    result.append("No accounts found for: ").append(searchText);
                } else {
                    result.append("Found ").append(accounts.size()).append(" account(s):\n\n");
                    for (Account acc : accounts) {
                        result.append("Account Number: ").append(acc.getAccountNumber()).append("\n");
                        result.append("Account Holder: ").append(acc.getAccountName()).append("\n");
                        result.append("Account Type: ").append(acc.getAccountType()).append("\n");
                        result.append("Balance: Ksh: ").append(String.format("%.2f", acc.getBalance())).append("\n");
                        result.append("--------------------------\n");
                    }
                }
            }

            txtResult.setText(result.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
