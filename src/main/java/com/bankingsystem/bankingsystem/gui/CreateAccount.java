package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.model.Account;
import com.bankingsystem.bankingsystem.model.User;
import com.bankingsystem.bankingsystem.service.BankService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateAccount extends JDialog {

    private JTextField txtAccountName;
    private JComboBox<String> cmbAccountType;
    private JTextField txtInitialBalance;
    private BankService bankService;
    private User createdBy;

    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color GRAY_BG = new Color(245, 245, 245);

    public CreateAccount(BankService bankService, User user) {
        this.bankService = bankService;
        this.createdBy = user;

        setTitle("Create New Account");
        setSize(550, 420);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(GRAY_BG);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel subtitleLabel = new JLabel("Open a new bank account");
        subtitleLabel.setFont(LABEL_FONT);
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(25));

        JPanel formCard = createFormCard();
        mainPanel.add(formCard);

        add(mainPanel);
    }

    private JPanel createFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel nameLabel = new JLabel("Customer Name");
        nameLabel.setFont(LABEL_FONT);
        nameLabel.setForeground(new Color(80, 80, 80));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(8));

        txtAccountName = new JTextField();
        txtAccountName.setFont(FIELD_FONT);
        txtAccountName.setBackground(GRAY_BG);
        txtAccountName.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        txtAccountName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(txtAccountName);
        card.add(Box.createVerticalStrut(18));

        JLabel typeLabel = new JLabel("Account Type");
        typeLabel.setFont(LABEL_FONT);
        typeLabel.setForeground(new Color(80, 80, 80));
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(typeLabel);
        card.add(Box.createVerticalStrut(8));

        cmbAccountType = new JComboBox<>(new String[]{"Savings", "Checking", "Business"});
        cmbAccountType.setFont(FIELD_FONT);
        cmbAccountType.setBackground(GRAY_BG);
        cmbAccountType.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(cmbAccountType);
        card.add(Box.createVerticalStrut(18));

        JLabel balanceLabel = new JLabel("Initial Balance");
        balanceLabel.setFont(LABEL_FONT);
        balanceLabel.setForeground(new Color(80, 80, 80));
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(balanceLabel);
        card.add(Box.createVerticalStrut(8));

        txtInitialBalance = new JTextField();
        txtInitialBalance.setFont(FIELD_FONT);
        txtInitialBalance.setBackground(GRAY_BG);
        txtInitialBalance.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        txtInitialBalance.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(txtInitialBalance);
        card.add(Box.createVerticalStrut(25));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton btnCancel = createButton("Cancel", new Color(158, 158, 158));
        btnCancel.addActionListener(e -> dispose());

        JButton btnCreate = createButton("Create", SUCCESS_COLOR);
        btnCreate.addActionListener(e -> createAccount());

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnCreate);
        card.add(buttonPanel);

        return card;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(BUTTON_FONT);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 40));
        return btn;
    }

    private void createAccount() {
        String accountName = txtAccountName.getText().trim();
        String accountType = (String) cmbAccountType.getSelectedItem();
        String balanceStr = txtInitialBalance.getText().trim();

        if (accountName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Customer name is required.", 
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double initialBalance = Double.parseDouble(balanceStr);

            if (initialBalance < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Initial balance cannot be negative.", 
                    "Invalid Amount", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            long accountNo = bankService.generateAccountNumber();
            Account account = new Account(accountNo, accountType, accountName, initialBalance);
            bankService.createAccount(account);
            
            JOptionPane.showMessageDialog(this, 
                "Account Created Successfully!\n\n" +
                "Account Number: " + accountNo + "\n" +
                "Name: " + accountName + "\n" +
                "Type: " + accountType + "\n" +
                "Balance: " + initialBalance,
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid balance amount.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
