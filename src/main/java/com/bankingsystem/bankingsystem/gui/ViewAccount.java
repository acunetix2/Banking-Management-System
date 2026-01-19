package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.service.BankService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ViewAccount extends JDialog {

    private JTextField txtSearchInput;
    private JComboBox<String> cmbSearchType;
    private JTextArea txtAccountDetails;
    private BankService bankService;

    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color GRAY_BG = new Color(245, 245, 945);

    public ViewAccount(BankService bankService) {
        this.bankService = bankService;

        setTitle("View Account Details");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(GRAY_BG);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Account Information");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel subtitleLabel = new JLabel("Search and view account details");
        subtitleLabel.setFont(LABEL_FONT);
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(25));

        JPanel searchCard = createSearchCard();
        mainPanel.add(searchCard);

        add(mainPanel);
    }

    private JPanel createSearchCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel typeLabel = new JLabel("Search By");
        typeLabel.setFont(LABEL_FONT);
        typeLabel.setForeground(new Color(80, 80, 80));
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(typeLabel);
        card.add(Box.createVerticalStrut(8));

        cmbSearchType = new JComboBox<>(new String[]{"Account Number", "Customer Name"});
        cmbSearchType.setFont(FIELD_FONT);
        cmbSearchType.setBackground(GRAY_BG);
        cmbSearchType.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(cmbSearchType);
        card.add(Box.createVerticalStrut(18));

        JLabel inputLabel = new JLabel("Search Value");
        inputLabel.setFont(LABEL_FONT);
        inputLabel.setForeground(new Color(80, 80, 80));
        inputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(inputLabel);
        card.add(Box.createVerticalStrut(8));

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        txtSearchInput = new JTextField();
        txtSearchInput.setFont(FIELD_FONT);
        txtSearchInput.setBackground(GRAY_BG);
        txtSearchInput.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(BUTTON_FONT);
        searchBtn.setBackground(PRIMARY_COLOR);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.addActionListener(e -> searchAccount());

        searchPanel.add(txtSearchInput, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        card.add(searchPanel);
        card.add(Box.createVerticalStrut(20));

        JLabel resultsLabel = new JLabel("Account Details");
        resultsLabel.setFont(LABEL_FONT);
        resultsLabel.setForeground(new Color(80, 80, 80));
        resultsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(resultsLabel);
        card.add(Box.createVerticalStrut(8));

        txtAccountDetails = new JTextArea();
        txtAccountDetails.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAccountDetails.setEditable(false);
        txtAccountDetails.setLineWrap(true);
        txtAccountDetails.setWrapStyleWord(true);
        txtAccountDetails.setBackground(GRAY_BG);
        txtAccountDetails.setForeground(new Color(50, 50, 50));
        txtAccountDetails.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JScrollPane scrollPane = new JScrollPane(txtAccountDetails);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        card.add(scrollPane);

        return card;
    }

    private void searchAccount() {
        String searchInput = txtSearchInput.getText().trim();
        String searchType = (String) cmbSearchType.getSelectedItem();
        
        if (searchInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a search value.", 
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if ("Account Number".equals(searchType)) {
            searchByAccountNumber(searchInput);
        } else {
            searchByName(searchInput);
        }
    }

    private void searchByAccountNumber(String accountNoStr) {
        try {
            long accountNo = Long.parseLong(accountNoStr);
            var account = bankService.getAccount(accountNo);
            
            if (account.isPresent()) {
                var acc = account.get();
                txtAccountDetails.setText(
                    "Account Number: " + acc.getAccountNumber() + "\n" +
                    "Account Holder: " + acc.getAccountName() + "\n" +
                    "Account Type: " + acc.getAccountType() + "\n" +
                    "Current Balance: " + acc.getBalance()
                );
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Account not found.", 
                    "Search Result", 
                    JOptionPane.INFORMATION_MESSAGE);
                txtAccountDetails.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid account number format.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchByName(String name) {
        var accounts = bankService.searchAccountByName(name);
        
        if (accounts.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No accounts found with that name.", 
                "Search Result", 
                JOptionPane.INFORMATION_MESSAGE);
            txtAccountDetails.setText("");
        } else if (accounts.size() == 1) {
            var acc = accounts.get(0);
            txtAccountDetails.setText(
                "Account Number: " + acc.getAccountNumber() + "\n" +
                "Account Holder: " + acc.getAccountName() + "\n" +
                "Account Type: " + acc.getAccountType() + "\n" +
                "Current Balance: " + acc.getBalance()
            );
        } else {
            StringBuilder details = new StringBuilder();
            details.append("Found ").append(accounts.size()).append(" accounts:\n\n");
            
            for (int i = 0; i < accounts.size(); i++) {
                var acc = accounts.get(i);
                details.append((i + 1)).append(". ")
                       .append("Number: ").append(acc.getAccountNumber())
                       .append(" | Name: ").append(acc.getAccountName())
                       .append(" | Type: ").append(acc.getAccountType())
                       .append(" | Balance: ").append(acc.getBalance()).append("\n");
            }
            
            txtAccountDetails.setText(details.toString());
        }
    }
}
